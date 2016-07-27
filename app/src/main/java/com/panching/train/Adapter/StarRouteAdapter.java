package com.panching.train.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.panching.train.Fragment.MyStartRouteFragment;
import com.panching.train.R;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by panching on 16/5/3.
 */
public class StarRouteAdapter extends BaseAdapter {

    private List mList;
    private Context mContext;
    ViewHolder viewHolder = null;
    Boolean mRemoveFlag;
    View view;
    public StarRouteAdapter() {
        super();
    }
    public void refresh(ArrayList list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public StarRouteAdapter(List mList, Context mContext,Boolean removeFlag) {
        this.mList = mList;
        this.mContext = mContext;
        this.mRemoveFlag = removeFlag;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        view = convertView;
        if (null == convertView)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.start_route_item, null);

            viewHolder = new ViewHolder();
            viewHolder.tvFormToStationTitle = (TextView) convertView.findViewById(R.id.fromToStationTitle);
            viewHolder.tvSettingIndex = (TextView) convertView.findViewById(R.id.settingIndex);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(mList.size()>0){
            //des :高雄:1238,臺北:1008 & 臺北:1008,高雄:1238
            final String startRouteStr = (String)mList.get(position);

            //fromStation
            String formInfo = startRouteStr.split(",")[0];
            //toStation
            String toInfo = startRouteStr.split(",")[1];

            final String startStationName = formInfo.split(":")[0].trim();
            final String startStationID = formInfo.split(":")[1].trim();
            final String endStationIName = toInfo.split(":")[0].trim();
            final String endStationID = toInfo.split(":")[1].trim();
            if(!mRemoveFlag)
                viewHolder.tvSettingIndex.setText("設成首頁");
            else
                viewHolder.tvSettingIndex.setText("刪除路線");

            viewHolder.tvFormToStationTitle.setText(startStationName+" > "+endStationIName);
            viewHolder.tvSettingIndex.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!mRemoveFlag)
                    {
                        SharedPreferences pre = mContext.getSharedPreferences("mainIndex", Context.MODE_PRIVATE);
                        SharedPreferences.Editor preEdit = pre.edit();
                        preEdit.putString("0", startRouteStr.trim());
                        preEdit.commit();
                        //how do I set MainActivity stationName & endStationName & startStationID & endStationID when I click Item in starRouteAdapter????
                        Toast.makeText(mContext,"已設成首頁！",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        List list = new ArrayList();
                        String stationInfo =  startStationName+":"+startStationID+","+endStationIName+":"+endStationID;
                        //取得所有常用車站
                        SharedPreferences pre =  mContext.getSharedPreferences("starRoute", mContext.MODE_APPEND);
                        SharedPreferences.Editor preEdit = pre.edit();

                        for(int i=0;i<pre.getAll().size();i++)
                        {
                            if(stationInfo.equals(pre.getString(String.valueOf(i), "")))
                            {
                                preEdit.remove(String.valueOf(i));
                                continue;
                            }

                            list.add(pre.getString(String.valueOf(i), ""));
                            preEdit.remove(String.valueOf(i));
                        }
                        for (int i=0;i<list.size();i++)
                        {
                            Log.d("list", list.get(i) + "");
                            preEdit.putString(String.valueOf(i), (String) list.get(i));

                        }
                        preEdit.commit();
                        MyStartRouteFragment myStartRouteFragment = new MyStartRouteFragment(){};
                        myStartRouteFragment.removeListItem(position);
                        Toast.makeText(mContext,"刪除成功！",Toast.LENGTH_LONG).show();

                    }
                }
            });
        }

        return convertView;
    }

    private final class ViewHolder {

        TextView tvFormToStationTitle ;
        TextView tvSettingIndex;
    }
}
