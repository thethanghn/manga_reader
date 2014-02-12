package com.nttconsulting.ui;

import com.nttconsulting.manga_reader.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainDrawerAdapter extends ArrayAdapter<String> {

	private Context context;
	private String[] items;
	private int layout;
	private String[] icons;
	private int selected;

	public MainDrawerAdapter(Context context, int resource, String[] items, String[] icons) {
		super(context, resource, items);
		this.context = context;
		this.layout = resource;
		
		this.items = items;
		this.icons = icons;
		this.selected = 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = (View) inflater.inflate(this.layout, null);
		ImageView imageView = (ImageView)  itemView.findViewById(R.id.imageView1);
		TextView textView = (TextView) itemView.findViewById(R.id.textView1);
		String itemName = items[position];
		int itemIcon = context.getResources().getIdentifier(icons[position], "drawable", context.getPackageName());
		imageView.setImageResource(itemIcon);
		textView.setText(itemName);
		
		//
		if (this.selected == position) {
			itemView.setBackgroundColor(context.getResources().getColor(R.color.main_drawer_selected));
		}
		
		return itemView;
	}
	
	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return this.items[position];
	}
	
	public void setSelected(int selected) {
		this.selected = selected;
	}
}
