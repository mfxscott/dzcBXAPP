package com.bixian365.dzc.fragment;

import android.annotation.SuppressLint;
import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bixian365.dzc.R;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("NewApi")
public class XHPresentation extends Presentation
{
    private Banner banner;
    ViewPager  viewPager;
    private int[] imgIdArray ;
    private  Context outerContexts;
    private List<View> viewContainter = new ArrayList<>();
    private ImageView pad_fp_img;
    private int tag =1;
    public XHPresentation(Context outerContext, Display display)
    {
        super(outerContext, display);
        this.outerContexts = outerContext;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.screen_second);

        banner = (Banner) findViewById(R.id.pad_banner);
        setBanner();
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        pad_fp_img = (ImageView) findViewById(R.id.pad_fp_img);


        imgIdArray = new int[]{R.mipmap.start_1, R.mipmap.start_2};
        //设置Adapter
//        viewPager.setCurrentItem((mImageViews.length) * 10);
        View view1 = LayoutInflater.from(outerContexts).inflate(R.layout.guide, null);
        View view2 = LayoutInflater.from(outerContexts).inflate(R.layout.guide2, null);
//        View view3 = LayoutInflater.from(this).inflate(R.layout.guide3, null);
//        View view4 = LayoutInflater.from(this).inflate(R.layout.guide4, null);
//        timer.schedule(task, 5000, 1000); // 1s后执行task,经过1s再次执行
        new TimeThread().start();
        viewContainter.add(view1);
        viewContainter.add(view2);

        viewPager.setAdapter(new PagerAdapter() {
            //viewpager中的组件数量
            @Override
            public int getCount() {
                return viewContainter.size();
            }

            //滑动切换的时候销毁当前的组件
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                ((ViewPager) container).removeView(viewContainter.get(position));
            }

            //每次滑动的时候生成的组件
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(viewContainter.get(position));
//                if(position == 1){
//                    RelativeLayout tv = (RelativeLayout) viewContainter.get(position).findViewById(R.id.guide_lin_intent_rel);
//                    tv.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    });
//                }
                return viewContainter.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return null;
            }
        });
    }
    /**
     * 首页轮播图
     */
    private void setBanner(){
        try {

            if(null == banner)
                return ;
//        List<String> images = new ArrayList<String>();
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1497598051&di=136b6c564a6d8d59e77ce349616996e9&imgtype=jpg&er=1&src=http%3A%2F%2Fm.qqzhi.com%2Fupload%2Fimg_0_72213646D1378690088_23.jpg");
//        images.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3145185115,3541103163&fm=26&gp=0.jpg");
//        images.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4280343775,3437702687&fm=26&gp=0.jpg");
            List<Integer> images = new ArrayList<>();
//            for(int i=0;i<goodsTypeList.size();i++){
//                images.add(goodsTypeList.get(i).getImgUrl()+"");
//            }
//        banner.setOnBannerListener(new OnBannerListener() {
//            @Override
//            public void OnBannerClick(int position) {
//                Intent intent = new Intent( activity, GoodsDetailActivity.class);
////                intent.putExtra("cno",goodsTypeList.get);
//                startActivity(intent);
//            }
//        });
            images.add(R.mipmap.pad_start);
            images.add(R.mipmap.pad_start);
            images.add(R.mipmap.pad_start);
//        List<String> titlestr = new ArrayList<String>();
//        titlestr.add("我是第一个图片");
//        titlestr.add("我是第2个图片");
//        titlestr.add("我是第3个图片");
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader());
//        //显示标题样式水平显示
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
//        //设置标题文本
//        banner.setBannerTitles(titlestr);
            //设置图片集合
            banner.setImages(images);
            banner.setDelayTime(5000);
            //设置banner动画效果
            //DepthPag折叠
            banner.setBannerAnimation(Transformer.Default);
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }catch (Exception e){

        }
    }
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */
            //Glide 加载图片简单用法
            Glide.with(context).load(path).placeholder(R.mipmap.default_big_load_img).error(R.mipmap.default_big_load_img).into(imageView);
            //Picasso 加载图片简单用法
//            Picasso.with(context).load(path).into(imageView);

            //用fresco加载图片简单用法，记得要写下面的createImageView方法
//            Uri uri = Uri.parse((String) path);
//            imageView.setImageURI(uri);
        }
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                tag++;
                if(tag%2==0){
                    pad_fp_img.setImageResource(R.mipmap.start_logo_1);
                }else if(tag%3==0){
                    pad_fp_img.setImageResource(R.mipmap.start_logo_2);
                }else{
                    pad_fp_img.setImageResource(R.mipmap.start_logo_3);
                }

            }
            super.handleMessage(msg);
        };
    };
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };
    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(5000);
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }
}
