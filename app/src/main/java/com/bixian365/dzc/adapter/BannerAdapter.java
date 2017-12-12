package com.bixian365.dzc.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.main.SpecialsEntity;

import java.util.List;

/**
 * ***************************
 * 首页广告
 * @author mfx
 * ***************************
 */
public class BannerAdapter extends BaseAdapter {
	private List<SpecialsEntity> result;
	private final LayoutInflater mLayoutInflater;
	private Activity mContext;
	public BannerAdapter(Activity context, List<SpecialsEntity> result) {
		mLayoutInflater = LayoutInflater.from(context);
		this.result = result;
		this.mContext = context;
	}
	public int getCount() {
		return result.size();// 返回很大的值使得getView中的position不断增大来实现循环
	}
	public Object getItem(int position) {
		return position;
	}
	public long getItemId(int position) {
		return position;
	}
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.banner_item_img, null);
			vh.image = (ImageView) convertView.findViewById(R.id.banner_item_iv);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Glide.with(mContext)
				.load(result.get(position).getImgUrl())
				.placeholder(R.mipmap.default_big_load_img)
				.error(R.mipmap.default_big_load_img)
				.fitCenter()
				.into(vh.image);
//		SXUtils.getInstance(mContext).GlidFullSetImg(result.get(position).getImgUrl(),vh.image);
		return convertView;
	}
	class ViewHolder{
		ImageView image;
	}
}