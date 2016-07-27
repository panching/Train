package com.panching.train.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.panching.train.R;
import com.panching.train.obj.TimeInfoObj;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by panching on 16/4/25.
 */
public final class TimeContentAdapter extends BaseAdapter {

    private ViewHolder viewHolder = null;
    private Context mContext;
    private List<Map<String, Object>> mList;
    private String mStartStation;
    private String mEndStation;
    private Hashtable selectStaus = new Hashtable();
    private String mCarClass;
    String[] colorArray = {"#005ab5","#F75000","#FF8000","#CE0000"};
    String setColor;

    public TimeContentAdapter() {
        super();
    }
    public TimeContentAdapter(Context context,List list,String title,String CarClassName) {
        this.mContext = context;
        this.mList = list;
        this.mStartStation = title.split(",")[0].trim();
        this.mEndStation = title.split(",")[1].trim();
        this.mCarClass = CarClassName;
        if(mCarClass.indexOf("區間") != -1)
        {
            setColor = colorArray[0];
        }
        else if(mCarClass.indexOf("復興") != -1)
        {
            setColor = colorArray[0];
        }
        else if(mCarClass.indexOf("莒光") != -1)
        {
            setColor = colorArray[2];
        }
        else if(mCarClass.indexOf("自強") != -1)
        {
            setColor = colorArray[1];
        }
        else if(mCarClass.indexOf("太魯閣") != -1)
        {
            setColor = colorArray[3];
        }
        else
        {
            setColor = colorArray[3];
        }

        for(int i=0;i< list.size();i++)
        {
            TimeInfoObj timeInfoObj = (TimeInfoObj)list.get(i);
            selectStaus.put(i,timeInfoObj.getStationName());
        }
    }

    @Override
    public int getCount() {
        return  mList.size();
    }

    @Override
    public Object getItem(int position) {
        return  mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.time_station,null);
            viewHolder = new ViewHolder();
            viewHolder.arrTime = (TextView)convertView.findViewById(R.id.tvArriveTimeInTimeStation);
            viewHolder.depTime = (TextView)convertView.findViewById(R.id.tvDepTimeInTimeStation);
            viewHolder.station = (TextView)convertView.findViewById(R.id.tvStationName);
            viewHolder.myStation = (TextView)convertView.findViewById(R.id.tvMyStart);
//            viewHolder.state =(ImageView)convertView.findViewById(R.id.imgStation);
            viewHolder.route = (ImageView)convertView.findViewById(R.id.imgRoute);
            viewHolder.arrow = (ImageView)convertView.findViewById(R.id.arrow);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TimeInfoObj timeInfoObj = (TimeInfoObj)mList.get(position);

        String sArrTime = ((String) timeInfoObj.getArrTime()).length()>5?((String) timeInfoObj.getArrTime()).substring(0, 5):(String) timeInfoObj.getArrTime();
        String sDepTime = ((String) timeInfoObj.getDeption()).length()>5?((String) timeInfoObj.getDeption()).substring(0, 5):(String) timeInfoObj.getDeption();
        viewHolder.arrow.setColorFilter(Color.parseColor(setColor));
        viewHolder.route.setColorFilter(Color.parseColor(setColor));
        viewHolder.arrTime.setText(sArrTime);
        viewHolder.depTime.setText(sDepTime);
        if(timeInfoObj.getStationName().indexOf("臺")==0)timeInfoObj.setStationName(timeInfoObj.getStationName().replace("臺","台"));
        viewHolder.station.setText(timeInfoObj.getStationName());
        final TextView stationCheck = viewHolder.station;
        final TextView myStationCheck = viewHolder.myStation;
        if (stationCheck.getText().equals(mEndStation)||stationCheck.getText().equals(mStartStation))
        {
            if(stationCheck.getText().equals(mStartStation))
            {
                myStationCheck.setText("我的起站");
                myStationCheck.setBackgroundResource(R.drawable.rectangle);
            }
            else
            {
                myStationCheck.setText("我的訖站");
                myStationCheck.setBackgroundResource(R.drawable.rectangle);
            }
            stationCheck.setTextColor(Color.parseColor("#2980b9"));
        }
        else
        {
            myStationCheck.setText("");
            myStationCheck.setBackgroundResource(0);
            stationCheck.setTextColor(Color.parseColor("#8d8d8d"));
        }


        return convertView;
    }
    //設定不可點選複寫下兩個方法
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
    private final class ViewHolder
    {
        TextView station ;
        TextView arrTime;
        TextView depTime;
        TextView myStation;
        ImageView state;
        ImageView route;
        ImageView arrow;
    }
}