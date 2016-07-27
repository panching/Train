package com.panching.train.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.panching.train.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by panching on 16/5/19.
 */
public class SettingItemAdapter extends BaseAdapter {

    List mIconList;
    List mDecList;
    Context mContext;
    private ViewHolder viewHolder;

    public SettingItemAdapter() {
    }

    public SettingItemAdapter(List decList,List iconList, Context mContext) {
        this.mIconList = iconList;
        this.mDecList = decList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mIconList.size();
    }

    @Override
    public Object getItem(int position) {
        return  mIconList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (null == convertView)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.setting_item, null);
            viewHolder = new ViewHolder();
            viewHolder.iconImg = (ImageView)convertView.findViewById(R.id.itemIcon);
            viewHolder.itemDec = (TextView)convertView.findViewById(R.id.itemDec);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.iconImg.setBackgroundResource((int)mIconList.get(position));
        viewHolder.itemDec.setText((String)mDecList.get(position));
        return convertView;
    }

    private final class ViewHolder {
        ImageView iconImg;
        TextView itemDec;
    }
}
