package com.bixian365.dzc.fragment.car;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
    @BindView(R.id.pad_car_total_price_tv)
    TextView totalPriceTv;
    @BindView(R.id.pad_car_goodsnumber_tv)
    TextView goodsNumberTv;
    @BindView(R.id.pad_car_now_time_tv)
    TextView  nowTimeTv;
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
        nowTimeTv.setText(SXUtils.getInstance(activity).GetNowDateTime()+"");
        mSwipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.pad_car_swipyrefreshlayout);
        SXUtils.getInstance(activity).setColorSchemeResources(mSwipyRefreshLayout);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setEnabled(false);
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
        initHandler();
        steData();

    }
    /**
     * 测试模拟测试数据
     */
    private void steData(){
        ShoppingCartLinesEntity  shoppingCartLinesEntity;
        for (int i=0;i<10;i++){
            shoppingCartLinesEntity = new ShoppingCartLinesEntity();
            shoppingCartLinesEntity.setGoodsName("我是商品名称");
            shoppingCartLinesEntity.setSkuPrice(i+"."+i);
            shoppingCartLinesEntity.setQuantity("1");
            shoppingCartLinesEntity.setGoodsModel("公斤");
            AppClient.padCarGoodsList.add(shoppingCartLinesEntity);
        }
        hand.sendEmptyMessage(1009);
    }
    private void initHandler(){
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1009:
                        simpAdapter = new PadCarGoodsListRecyclerViewAdapter(getActivity(),AppClient.padCarGoodsList);
                        recyclerView.setAdapter(simpAdapter);
                        goodsNumberTv.setText(AppClient.padCarGoodsList.size()+"件");
                        totalPriceTv.setText(simpAdapter.getPadCarTotalMoney()+"元");
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
        if (messageEvent.getTag() == AppClient.PADEVENT00001) {
            goodsNumberTv.setText(AppClient.padCarGoodsList.size()+"件");
            totalPriceTv.setText(simpAdapter.getPadCarTotalMoney()+"元");
        }else if(messageEvent.getTag() == AppClient.PADEVENT00002){
            nowTimeTv.setText(SXUtils.getInstance(activity).GetNowDateTime()+"");
        }
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
            case R.id.pad_car_update_lock_btn:
                LockDailog();
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
        if(simpAdapter.mSelect<0){
            SXUtils.getInstance(activity).ToastCenter("请选择商品进行修改");
            return;
        }
        View convertView = LayoutInflater.from(activity).inflate(R.layout.pad_car_update_value_dialog, null);
        final EditText et = (EditText) convertView.findViewById(R.id.pad_car_update_dialog_edt);
        new AlertDialog.Builder(activity)
//                .setTitle(title+"")
//                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(convertView)
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
    /**
     * 用户锁屏幕操作
     */
    private Dialog LockDialog;
    public void LockDailog() {
        final Dialog LockDialog = new AlertDialog.Builder(activity).create();
        LockDialog.show();
        LockDialog.setCancelable(false);
        LockDialog.setCanceledOnTouchOutside(false);
        Window window = LockDialog.getWindow();
        window.setContentView(R.layout.pad_car_lock_dialog);
       final  EditText inputedt = (EditText) window.findViewById(R.id.pad_car_lock_input_psd_tv);
        TextView cancelTv = (TextView) window.findViewById(R.id.pad_car_lock_cancel_tv);
        TextView unlockTv = (TextView) window.findViewById(R.id.pad_car_unlock_cancel_tv);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
                System.exit(0);
            }
        });
        unlockTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockDialog.dismiss();
                String  psd = inputedt.getText().toString();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }
}
