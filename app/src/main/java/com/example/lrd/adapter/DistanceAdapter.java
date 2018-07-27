package com.example.lrd.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lrd.R;
import com.example.lrd.ui.DropDownMenuActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By LRD
 * on 2018/7/24  notes：
 */
public class DistanceAdapter extends BaseAdapter {
	private Activity activity;
	private List<String> data;
	private int currentPosition = -1;//定义选中状态

	public DistanceAdapter(Activity Activity, List<String> strings) {
		activity = Activity;
		this.data = strings;
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(activity).inflate(R.layout.item_all, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else {
			holder =(ViewHolder) convertView.getTag();
		}
		holder.itemTextSelect.setText(data.get(position));
		if (currentPosition != -1){
			if (currentPosition == position){
				holder.itemTextSelect.setTextColor(activity.getResources().getColor(R.color.red));
				holder.itemImgSelect.setVisibility(View.VISIBLE);
			}else {
				holder.itemTextSelect.setTextColor(activity.getResources().getColor(R.color.black));
				holder.itemImgSelect.setVisibility(View.GONE);
			}
		}else {
			if (position == 0){
				holder.itemTextSelect.setTextColor(activity.getResources().getColor(R.color.red));
				holder.itemImgSelect.setVisibility(View.VISIBLE);
			}
		}
		return convertView;
	}

	public void setSelectItem(int position){
		currentPosition = position;
		notifyDataSetChanged();
	}

	static class ViewHolder {
		@BindView(R.id.item_textSelect)
		TextView itemTextSelect;
		@BindView(R.id.item_imgSelect)
		ImageView itemImgSelect;

		ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}
