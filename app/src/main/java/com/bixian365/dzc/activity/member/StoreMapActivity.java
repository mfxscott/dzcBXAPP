package com.bixian365.dzc.activity.member;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.adapter.StoreMapListAdapter;
import com.bixian365.dzc.entity.SearchHotWordEntity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;
import com.bixian365.dzc.utils.view.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

import static com.bixian365.dzc.R.id.map;

public class StoreMapActivity extends BaseActivity implements AMap.OnMyLocationChangeListener,AMap.OnMapClickListener
        ,AMap.OnMarkerDragListener, AMap.OnMapLoadedListener , AMap.OnCameraChangeListener,View.OnClickListener,PoiSearch.OnPoiSearchListener{
    private MapView mapView;
    private AMap aMap;
    private Activity activity;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private MyLocationStyle myLocationStyle;
    public LatLng locationNow;//第一次定位得到的经纬度
    private LatLonPoint lp;  //移动后的中心点经纬度
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;
    private StoreMapListAdapter storeMapListAdapter;
    private Handler hand;
    private ListViewForScrollView maplist;
    private ProgressBar proBar;
    private List<PoiItem> poItem;//每次移动地图，获取中心点周边店铺信息
    private EditText inputInfo;//输入门店信息
    private String userId;
    private RegeocodeAddress lastLocationAddress;//最后光标所在位置地址，用于没有找到相关店铺使用
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_map);
        mapView = (MapView) findViewById(map);
        //必须要写
        mapView.onCreate(savedInstanceState);
        activity = this;
        userId = this.getIntent().getStringExtra("userID");
        //这里以ACCESS_COARSE_LOCATION为例

        init();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(activity,StoreMapActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
                finish();
            } else
            {
                // Permission Denied
                Toast.makeText(activity, "权限被拒绝,将无法定位功能.", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //可在此继续其他操作。
    }
    /**
     * 初始化
     */
    private void init() {
        registerBack();
        setTitle("选择门店");
        LinearLayout allTitleGobackLinlay = (LinearLayout) findViewById(R.id.all_title_goback_linlay);
        allTitleGobackLinlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SXUtils.getInstance(activity).IntentMain(activity);
            }
        });


        inputInfo = (EditText) findViewById(R.id.store_map_input_info_edt);
        proBar = (ProgressBar) findViewById(R.id.store_map_probar);
        ScrollView scrollv = (ScrollView) findViewById(R.id.store_map_scrollv);
        scrollv.scrollTo(0,0);
        maplist = (ListViewForScrollView) findViewById(R.id.store_map_listv);
        maplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                storeMapListAdapter.changeSelected(position);
            }
        });
        Button btn = (Button) findViewById(R.id.map_store_comfirm_btn);

        btn.setOnClickListener(this);
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
//        mGPSModeGroup = (RadioGroup) findViewById(R.id.gps_radio_group);
//        mGPSModeGroup.setVisibility(View.GONE);
//        mLocationErrText = (TextView)findViewById(R.id.location_errInfo_text);
//        mLocationErrText.setVisibility(View.GONE);
        //设置SDK 自带定位消息监听
        aMap.setOnMyLocationChangeListener(this);
        aMap.setOnCameraChangeListener(this);
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Logs.i("===="+event.getAction());
                return false;
            }
        });
//        startLocationValue();


        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        if(poItem != null && poItem.size()>0)
                            poItem.clear();
                        poItem = (List<PoiItem>) msg.obj;
                        Logs.i(poItem.size()+"======="+poItem);
                        storeMapListAdapter = new StoreMapListAdapter(activity,poItem);
                        maplist.setAdapter(storeMapListAdapter);
                        break;
                    case 1000:
                        Intent intent = new Intent(activity, LoginNameActivity.class);
                        startActivity(intent);
                        finish();
//                        SXUtils.getInstance(activity).finishActivity();
                        break;
                    case AppClient.ERRORCODE:
                        String errormsg = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(errormsg+"");
                        if(poItem != null) {
                            poItem.clear();
                            storeMapListAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                proBar.setVisibility(View.GONE);
                return true;
            }
        });
    }
    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 如果要设置定位的默认状态，可以在此处进行设置
        myLocationStyle = new MyLocationStyle();
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setOnMarkerDragListener(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        setupLocationStyle();
//        startLocation();

    }
    /**
     * 设置自定义定位蓝点
     */
    private void setupLocationStyle(){
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.mipmap.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE));
//        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW));

        //根据经纬度转为地理位置
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener(){

            @Override
            public void onGeocodeSearched(GeocodeResult result, int rCode) {
            }
            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                lastLocationAddress = result.getRegeocodeAddress();
                Logs.i("formatAddress=======", "formatAddress:"+lastLocationAddress.getFormatAddress());
                Log.e("formatAddress====", "rCode:"+rCode);

            }});
    }
    GeocodeSearch geocoderSearch;
    private void addMarkersToMap() {
        aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(
                                getResources(), R.mipmap.map_point)))
                .position(locationNow));

        // 文字显示标注，可以设置显示内容，位置，字体大小颜色，背景色旋转角度
//        TextOptions textOptions = new TextOptions()
//                .position(locationNow)
//                .text("Text")
//                .fontColor(Color.BLACK)
//                .backgroundColor(Color.BLUE)
//                .fontSize(30)
//                .rotate(20)
//                .align(Text.ALIGN_CENTER_HORIZONTAL, Text.ALIGN_CENTER_VERTICAL)
//                .zIndex(1.f).typeface(Typeface.DEFAULT_BOLD);
//        aMap.addText(textOptions);

//
//        Marker marker = aMap.addMarker(new MarkerOptions()
//                .position(locationNow)
//                .title("当前位置")
//                .icon(BitmapDescriptorFactory
//                        .defaultMarker(R.mipmap.add_img))
//                .draggable(true));
//        marker.setRotateAngle(90);// 设置marker旋转90度
//        marker.setPositionByPixels(100, 100);
//        marker.showInfoWindow();// 设置默认显示一个infowinfow
    }
    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
//        keyWord = mSearchText.getText().toString().trim();

//        currentPage = 0;
        query = new PoiSearch.Query("餐馆", "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);// 设置查第一页

        if (lp != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(lp, 500, true));//
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    /**
     * 移动定位获取到当前经纬度
     * @param location
     */
    @Override
    public void onMyLocationChange(Location location) {
        // 定位回调监听
        if(location != null) {
            Logs.i("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if(bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
                locationNow= new LatLng(location.getLatitude(), location.getLongitude());
                lp = new LatLonPoint(location.getLatitude(), location.getLongitude());
                addMarkersToMap();
                /*
                errorCode
                errorInfo
                locationType
                */
                Logs.i("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType );
            } else {
                Logs.i("amap", "定位信息， bundle is null ");

            }

        } else {
            Logs.i("amap", "定位失败");
        }
    }
    @Override
    public void onMapClick(LatLng latLng) {
//点击地图后清理图层插上图标，在将其移动到中心位置
//        aMap.clear();
//        latitude = latLng.latitude;
//        longitude = latLng.longitude;
        Logs.i(latLng.latitude+"===="+latLng.longitude);
        MarkerOptions otMarkerOptions = new MarkerOptions();
        otMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.close));
        otMarkerOptions.position(latLng);
        aMap.addMarker(otMarkerOptions);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
    }
    @Override
    public void onMapLoaded() {
    }
    /**
     * 监听拖动marker时事件回调
     */
    @Override
    public void onMarkerDrag(Marker marker) {
        String curDes = marker.getTitle() + "拖动时当前位置:(lat,lng)\n("
                + marker.getPosition().latitude + ","
                + marker.getPosition().longitude + ")";
        SXUtils.getInstance(activity).ToastCenter(curDes);
//        markerText.setText(curDes);
    }

    /**
     * 监听拖动marker结束事件回调
     */
    @Override
    public void onMarkerDragEnd(Marker marker) {
        SXUtils.getInstance(activity).ToastCenter("停止拖动");
    }

    /**
     * 监听开始拖动marker事件回调
     */
    @Override
    public void onMarkerDragStart(Marker marker) {
        SXUtils.getInstance(activity).ToastCenter("开始拖动");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.map_store_comfirm_btn:
                String  InfoStr = inputInfo.getText().toString().trim();
                String checkInfo="";//最终选中的店铺地址
                int isInputLocation=0;
//                if(storeMapListAdapter != null && storeMapListAdapter.mSelect != -1 && TextUtils.isEmpty(InfoStr)){
//                    //选择店铺和输入同时输入了详细信息
//                    PoiItem    map = poItem.get(storeMapListAdapter.mSelect);
//                    String storeName = map.getTitle();
//                    checkInfo =map.getTitle()+","
//                            +map.getProvinceName().toString().trim()+","+
//                            map.getCityName().toString().trim()+","+
//                            map.getAdName().toString().trim()+","+
//                            map.getSnippet().toString().trim()+"";
//
//                }else
                if(TextUtils.isEmpty(InfoStr) &&  poItem == null ){
                    SXUtils.getInstance(activity).ToastshowDialogView(activity,"温馨提示","请选择或者输入店铺信息");
                    return;
                }else if(!TextUtils.isEmpty(InfoStr)){
                    isInputLocation = 1;
                    checkInfo = InfoStr;
                }else if(storeMapListAdapter != null && storeMapListAdapter.mSelect != -1 && TextUtils.isEmpty(InfoStr)){
                    PoiItem    map = poItem.get(storeMapListAdapter.mSelect);
                    checkInfo =map.getTitle()+","
                            +map.getProvinceName().toString().trim()+","+
                            map.getCityName().toString().trim()+","+
                            map.getAdName().toString().trim()+","+
                            map.getSnippet().toString().trim()+"";
                }
                getStoreAddresshHttp(checkInfo,isInputLocation);
//                Intent mainintent = new Intent(activity, MainFragmentActivity.class);
//                startActivity(mainintent);
//                finish();
                break;
        }
    }
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
//        aMap.clear();
    }
    //移动地图中心地理位置，显示当前移动地址
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        LatLng target = cameraPosition.target;
//        Logs.i(target.latitude + "经纬度纬度------" + target.longitude);
        LatLonPoint latlp = new LatLonPoint(target.latitude,target.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latlp, 200,GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
        lp = new LatLonPoint(target.latitude,target.longitude);
        proBar.setVisibility(View.VISIBLE);
        doSearchQuery();
    }

    // 将之前被点击的marker置为原来的状态
    private void resetlastmarker(List<PoiItem> mapList) {
        Message msg = new Message();
        msg.what = 0;
        msg.obj = mapList;
        hand.sendMessage(msg);


//        int index = poiOverlay.getPoiIndex(mlastMarker);
//        if (index < 10) {
//            mlastMarker.setIcon(BitmapDescriptorFactory
//                    .fromBitmap(BitmapFactory.decodeResource(
//                            getResources(),
//                            markers[index])));
//        }else {
//            mlastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(
//                    BitmapFactory.decodeResource(getResources(), R.mipmap.location_mark)));
//        }
//        mlastMarker = null;

    }
    private myPoiOverlay poiOverlay;// poi图层
    private PoiResult poiResult; // poi返回的结果
    private List<PoiItem> poiItems;// poi数
    private Marker mlastMarker;
    private int[] markers = {R.mipmap.location_mark
            ,R.mipmap.location_mark
            ,R.mipmap.location_mark
            ,R.mipmap.location_mark
            ,R.mipmap.location_mark
            ,R.mipmap.location_mark
            ,R.mipmap.location_mark
            ,R.mipmap.location_mark
            ,R.mipmap.location_mark
    };
    /**
     * 自定义PoiOverlay
     *
     */

    private class myPoiOverlay {
        private AMap mamap;
        private List<PoiItem> mPois;
        private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();

        public myPoiOverlay(AMap amap, List<PoiItem> pois) {
            mamap = amap;
            mPois = pois;
        }
        /**
         * 添加Marker到地图中。
         * @since V2.1.0
         */
        public void addToMap() {
            for (int i = 0; i < mPois.size(); i++) {
                Marker marker = mamap.addMarker(getMarkerOptions(i));
                PoiItem item = mPois.get(i);
                marker.setObject(item);
                mPoiMarks.add(marker);
            }
        }
        /**
         * 去掉PoiOverlay上所有的Marker。
         *
         * @since V2.1.0
         */
        public void removeFromMap() {
            for (Marker mark : mPoiMarks) {
                mark.remove();
            }
        }
        /**
         * 移动镜头到当前的视角。
         * @since V2.1.0
         */
        public void zoomToSpan() {
            if (mPois != null && mPois.size() > 0) {
                if (mamap == null)
                    return;
                LatLngBounds bounds = getLatLngBounds();
                mamap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        }
        private LatLngBounds getLatLngBounds() {
            LatLngBounds.Builder b = LatLngBounds.builder();
            for (int i = 0; i < mPois.size(); i++) {
                b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
                        mPois.get(i).getLatLonPoint().getLongitude()));
            }
            return b.build();
        }
        private MarkerOptions getMarkerOptions(int index) {
            return new MarkerOptions()
                    .position(
                            new LatLng(mPois.get(index).getLatLonPoint()
                                    .getLatitude(), mPois.get(index)
                                    .getLatLonPoint().getLongitude()))
                    .title(getTitle(index)).snippet(getSnippet(index))
                    .icon(getBitmapDescriptor(index));
        }
        protected String getTitle(int index) {
            return mPois.get(index).getTitle();
        }

        protected String getSnippet(int index) {
            return mPois.get(index).getSnippet();
        }

        /**
         * 从marker中得到poi在list的位置。
         *
         * @param marker 一个标记的对象。
         * @return 返回该marker对应的poi在list的位置。
         * @since V2.1.0
         */
        public int getPoiIndex(Marker marker) {
            for (int i = 0; i < mPoiMarks.size(); i++) {
                if (mPoiMarks.get(i).equals(marker)) {
                    return i;
                }
            }
            return -1;
        }
        protected BitmapDescriptor getBitmapDescriptor(int arg0) {
            if (arg0 < 10) {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), markers[arg0]));
                return icon;
            }else {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), R.mipmap.location_mark));
                return icon;
            }
        }
    }

    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if (poiItems != null && poiItems.size() > 0) {
                        //清除POI信息显示
//                        whetherToShowDetailInfo(false);
//                        //并还原点击marker样式
//                        if (mlastMarker != null) {
                        resetlastmarker(poiItems);
//                        }
//                        //清理之前搜索结果的marker
//                        if (poiOverlay !=null) {
//                            poiOverlay.removeFromMap();
//                        }
//                        List<Map<String ,String >>  mapList=null;
//                        for(int i=0;i<poiItems.size();i++){
//                            mapList = new ArrayList<>();
//                            Map<String ,String > map =new  HashMap<String,String>();
//                            map.put("title",poiItems.get(i).getTitle()+"");
//                            map.put("address",poiItems.get(i).getProvinceName()+poiItems.get(i).getCityName()+poiItems.get(i).getAdName()+poiItems.get(i).getSnippet());
////                            Logs.i("==========="+poiItems.get(i).getTitle()+"标题和地址"+poiItems.get(i).getDirection());
////                            Logs.i("==========="+poiItems.get(i).getCityName());
////                            Logs.i("==========="+poiItems.get(i).getAdName());
////                            Logs.i("==========="+poiItems.get(i).getProvinceName());
////                            Logs.i("==========="+poiItems.get(i).getSnippet());
//
////                            Logs.i("==========="+poiItems.get(i).getTypeDes());
////                            Logs.i("==========="+poiItems.get(i).getTel());
////                            Logs.i("==========="+poiItems.get(i).getProvinceCode());
//                            mapList.add(map);
//                            Logs.i("================================"+i);
//                        }
//                        Logs.i("===========执行完成=====================");

//                        aMap.clear();
//                        poiOverlay = new myPoiOverlay(aMap, poiItems);
//                        poiOverlay.addToMap();
//                        poiOverlay.zoomToSpan();

//                        aMap.addMarker(new MarkerOptions()
//                                .anchor(0.5f, 0.5f)
//                                .icon(BitmapDescriptorFactory
//                                        .fromBitmap(BitmapFactory.decodeResource(
//                                                getResources(), R.mipmap.map_point)))
//                                .position(new LatLng(lp.getLatitude(), lp.getLongitude())));

//                        aMap.addCircle(new CircleOptions()
//                                .center(new LatLng(lp.getLatitude(),
//                                        lp.getLongitude())).radius(5000)
//                                .strokeColor(Color.BLUE)
//                                .fillColor(Color.argb(50, 1, 1, 1))
//                                .strokeWidth(2));
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        Message msg = new Message();
                        msg.what = AppClient.ERRORCODE;
                        msg.obj = "没有查询到相关数据";
                        hand.sendMessage(msg);
                    }
                }
            } else {
                Message msg = new Message();
                msg.what = AppClient.ERRORCODE;
                msg.obj = "没有查询到相关数据";
                hand.sendMessage(msg);

            }
        } else  {
//            Toast.makeText(activity,rcode+"",Toast.LENGTH_LONG).show();
//            Message msg = new Message();
//            msg.what = AppClient.ERRORCODE;
//            msg.obj = rcode+"";
//            hand.sendMessage(msg);
        }
    }
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        Toast.makeText(activity,infomation+"",Toast.LENGTH_LONG).show();
    }

    /**
     * 获取到店铺信息及为店铺添加店铺地址
     */
    public void getStoreAddresshHttp(String Str,int isInput){
        if(TextUtils.isEmpty(Str)){
            SXUtils.getInstance(activity).ToastCenter("请输入店铺信息店铺信息");
            return;
        }
        HttpParams httpParams = new HttpParams();
        if(isInput ==1){
//            httpParams.put("id","");
            httpParams.put("shopName",Str);
            httpParams.put("name",Str);
            if(lastLocationAddress != null) {
                httpParams.put("province", lastLocationAddress.getProvince());
                httpParams.put("city", lastLocationAddress.getCity());
                httpParams.put("district", lastLocationAddress.getDistrict());
                httpParams.put("addr", lastLocationAddress.getTownship());
            }else{
                httpParams.put("province", "广东省");
                httpParams.put("city", "深圳市");
                httpParams.put("district", "罗湖区");
                httpParams.put("addr", "");
            }
            httpParams.put("manager","滨海大道");
            httpParams.put("userId",userId);
            httpParams.put("shopLogoFile","");
        }else {
            String[] strs = Str.split(",");

            httpParams.put("name",strs[0]);
            httpParams.put("shopName",strs[0]);
            httpParams.put("province",strs[1]);
            httpParams.put("city",strs[2]);
            httpParams.put("district",strs[3]);
            httpParams.put("addr",strs[4]);
            httpParams.put("manager","");
            httpParams.put("userId",userId);
            httpParams.put("shopLogoFile","");
        }
        HttpUtils.getInstance(activity).requestPost(false, AppClient.ADD_STORE_INFO, httpParams, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("店铺选择店铺名成功返回参数=======",jsonObject.toString());
                List<SearchHotWordEntity> goodsTypeList = ResponseData.getInstance(activity).parseJsonArray(jsonObject.toString(), SearchHotWordEntity.class);
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = goodsTypeList;
                hand.sendMessage(msg);
            }
            @Override
            public void onResponseError(String strError) {
                Message msg = new Message();
                msg.what = AppClient.ERRORCODE;
                msg.obj = strError;
                hand.sendMessage(msg);
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
//            SXUtils.getInstance(activity).IntentMain();
//            SXUtils.getInstance(activity).finishActivity();
            return false;
        }
        return true;
    }
}
