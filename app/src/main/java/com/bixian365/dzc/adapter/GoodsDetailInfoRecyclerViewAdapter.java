package com.bixian365.dzc.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bixian365.dzc.R;
import com.bixian365.dzc.utils.httpClient.AppClient;

/**
 * 商品详情中的商品信息说明图
 * @author mfx
 */
public class GoodsDetailInfoRecyclerViewAdapter
        extends RecyclerView.Adapter<GoodsDetailInfoRecyclerViewAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();

    private int mBackground;
    private String[] mValues;
    private Context context;
    private   int imgheight=0;
    private   int imgwidth=0;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
      public ImageView itemImg;//
        public TextView name; //商品名称商品图片
        public TextView modelPrice;
        public TextView model;//售卖模式
        public TextView num;//数量
        public TextView tatol; //总金额
        public TextView markPrice;//市场价
        public TextView shopPrice;//商铺售价

        public ViewHolder(View view) {
            super(view);
            mView = view;
            itemImg = (ImageView) mView.findViewById(R.id.item_add_img);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
    public GoodsDetailInfoRecyclerViewAdapter(Context context, String[] items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.img_item_add_layout, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        Glide.with(context).load(mValues[position]).asBitmap().centerCrop().into(holder.itemImg);


//        Glide.with(context).load(mValues[position]).asBitmap()
//                .fitCenter()
////                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        holder.itemImg.setImageBitmap(resource);
//                    }
//                });

        Glide.with(context)
                .load(mValues[position]).asBitmap()
                .priority( Priority.HIGH )//优先级
                .placeholder(R.mipmap.loading_img)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap,GlideAnimation<? super Bitmap> glideAnimation) {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        ViewGroup.LayoutParams lp =  holder.itemImg.getLayoutParams();
                        lp.width = AppClient.fullWidth;
                        float tempHeight=height * ((float)lp.width / width);
                        lp.height =(int)tempHeight ;
                        holder.itemImg.setLayoutParams(lp);
                        holder.itemImg.setImageBitmap(bitmap);
//                        img_content.addView(img);
                    }
                });
//        Glide
//                .with(context)
//                .load(mValues[position])
//                .override(600, 200)
//                .fitCenter()
//                .into(holder.itemImg);
        //部分手机不显示
//        SXUtils.getInstance(context).GlidFullSetImg(mValues[position],holder.itemImg);

//        Glide.with(context).load(mValues[position]).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                int imageWidth = resource.getWidth();
//                int imageHeight = resource.getHeight();
//                int height = AppClient.WIDTH * imageHeight / imageWidth;
//                ViewGroup.LayoutParams para = holder.itemImg.getLayoutParams();
//                para.height = height;
//                para.width = AppClient.WIDTH;
//                holder.itemImg.setImageBitmap(resource);
//            }
//        });


//        loadIntoUseFitWidth(context, mValues[position], R.mipmap.loading_img, holder.itemImg);
//        Glide.with(context)
//                .load(mValues[position])
//                .placeholder(R.mipmap.loading_img)
//                .error(R.mipmap.loading_img)
//                .fitCenter()
//                .into(holder.itemImg);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent( holder.mView.getContext(), GoodsDetailActivity.class);
//                intent.putExtra("cno",gcpurchase.getId());
//                holder.mView.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mValues.length;
    }
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
    public static void loadIntoUseFitWidth(Context context, final String imageUrl, int errorImageId, final ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .placeholder(errorImageId)
                .error(errorImageId)
                .into(imageView);
    }
}