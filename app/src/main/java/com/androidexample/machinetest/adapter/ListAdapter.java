package com.androidexample.machinetest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.swipe.R;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<ListGetSet> {
	ArrayList<ListGetSet> actorList;
	LayoutInflater vi;
	int Resource;
	ViewHolder holder;

	Context mContext;
	public ListAdapter(Context context, int resource, ArrayList<ListGetSet> objects) {
		super(context, resource, objects);

		mContext = context;

		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		actorList = objects;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// convert view = design

        Log.e("tag","set text");
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);
			holder.id = (TextView) v.findViewById(R.id.id);
			holder.title = (TextView) v.findViewById(R.id.title);
            holder.property_title = (TextView)v.findViewById(R.id.property_title);
            holder.border_layout = (LinearLayout) v.findViewById(R.id.border_layout);
			v.setTag(holder);
		}
        else {
			holder = (ViewHolder) v.getTag();
		}

		holder.id.setText(actorList.get(position).getId());
		holder.title.setText(actorList.get(position).getTitle());
        holder.property_title.setText(actorList.get(position).getProperty_title());
        if((position%2)== 0)
        {
            holder.border_layout.setBackgroundColor(Color.parseColor("#660000"));
        }
        else
        {
            holder.border_layout.setBackgroundColor(Color.parseColor("#CCCC00"));
        }
       return v;

	}

	static class ViewHolder {
		public TextView id;
		public TextView title;
        public TextView property_title;
        public LinearLayout border_layout;

	}


}