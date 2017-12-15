package com.bixian365.dzc.fragment.car;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bixian365.dzc.R;
import com.bixian365.dzc.adapter.PadCarGoodsListRecyclerViewAdapter;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.car.ShoppingCartLinesEntity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.view.SwipyRefreshLayout;
import com.bixian365.dzc.utils.view.SwipyRefreshLayoutDirection;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PadCarFragment extends Fragment {
    private RecyclerView recyclerView;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private View  view;
    private Activity activity;
    private Handler hand;
    private Unbinder unbinder;
    private PadCarGoodsListRecyclerViewAdapter simpAdapter;
    @BindView(R.id.pad_car_update_number_btn)
    Button updateNumBtn;
    public PadCarFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pad_car, null);
        activity = getActivity();
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }
    private void  initView(){

        mSwipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.pad_car_swipyrefreshlayout);
        SXUtils.getInstance(activity).setColorSchemeResources(mSwipyRefreshLayout);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(direction == SwipyRefreshLayoutDirection.TOP){
//                    indexPage = 0;
//                    initData();
                }else{
//                    indexPage ++;
//                    initData();
//                    HttpLiveSp(indexPage);
                }
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.pad_car_goodslist_recyclv);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ShoppingCartLinesEntity  shoppingCartLinesEntity = new ShoppingCartLinesEntity();
        shoppingCartLinesEntity.setGoodsName("1111");
        shoppingCartLinesEntity.setSkuPrice("12.3");
        shoppingCartLinesEntity.setQuantity("2");
        shoppingCartLinesEntity.setGoodsModel("公斤");
        AppClient.padCarGoodsList.add(shoppingCartLinesEntity);
        simpAdapter = new PadCarGoodsListRecyclerViewAdapter(getActivity(),AppClient.padCarGoodsList);
        recyclerView.setAdapter(simpAdapter);
        initHandler();
    }
    private void initHandler(){
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1009:
//                        billlist = (List<BillDataSetEntity>) msg.obj;
//                        if(billlist == null || billlist.size()<=0) {
//                            noDataLin.setVisibility(View.VISIBLE);
//                            if(tabLayout != null)
//                                tabLayout.removeAllTabs();
//                            if(billlist.size()<1){
//                                billlist.clear();
//                                recyclerView.setAdapter(null);
//                            }
//                            break;
//                        }
//                        noDataLin.setVisibility(View.GONE);
//                        if(billlist.size() >9){
//                            mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
//                        }else{
//                            mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
//                        }
//                        initViewPager(billlist);
                        break;
                    case AppClient.ERRORCODE:
                        String str = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(str+"");
                        break;
                }
                if(mSwipyRefreshLayout != null){
                    mSwipyRefreshLayout.setRefreshing(false);
                }
                SXUtils.DialogDismiss();
                return true;
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        //发货和确认采购订单成功，刷新列表
//        if (messageEvent.getTag() == AppClient.EVENT10002) {
//            initData();
//        }
//        if (messageEvent.getTag() == AppClient.EVENT100026) {
//            simpAdapter.notifyDataSetChanged();
//        }
    }
    @OnClick({ R.id.pad_car_update_number_btn, R.id.pad_car_update_price_btn, R.id.pad_car_update_del_btn, R.id.pad_car_update_clear_btn,
    R.id.pad_car_update_lock_btn,R.id.pad_car_topay_btn
    })
    public void OnclickBtn(Button button) {
        switch (button.getId()) {
            case R.id.pad_car_update_number_btn:
                InputDailog("0","请输入修改数量");
                break;
            case R.id.pad_car_update_price_btn:
                InputDailog("1","请输入修改价格");
                break;
            case R.id.pad_car_update_del_btn:
                simpAdapter.removeData();
                break;
            case R.id.pad_car_update_clear_btn:
                break;
            default:
                break;
        }
    }
    /**
     * 输入框弹窗
     * @param isprice  0 修改数量  1 修改价格
     * @param title
     */
    public void InputDailog(final String isprice , final String title){
        final EditText et = new EditText(activity);
       View convertView = LayoutInflater.from(activity).inflate(R.layout.bank_card_item, null);
        new AlertDialog.Builder(activity).setTitle(title+"")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(activity, title+"" + input, Toast.LENGTH_LONG).show();
                        }
                        else {
                           simpAdapter.updateData(isprice,input);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }
}
