package com.zws.wifiboarddetect;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
	private ArrayList<?> list;
	private LayoutInflater mInflater;

	public ListAdapter(Context context, ArrayList<?> list) {
		this.list = list;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		MyListItem item = (MyListItem) list.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, null);
			viewHolder = new ViewHolder(
					(TextView) convertView.findViewById(R.id.ssid),
					(TextView) convertView.findViewById(R.id.level));
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.ssid.setText(item.ssid);
		viewHolder.level.setText(item.level + "%");

		return convertView;
	}

	class ViewHolder {
		protected TextView ssid;
		protected TextView level;

		public ViewHolder(TextView ssid, TextView level) {
			this.ssid = ssid;
			this.level = level;

		}
	}
}
