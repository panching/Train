package com.panching.train.Adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.panching.train.AsyncTask.OrderTicketAsyncTask;
import com.panching.train.AsyncTask.TimeAsyncTask;
import com.panching.train.OrderTicketWebView;
import com.panching.train.R;
import com.panching.train.TrainListActivity;
import com.panching.train.obj.OrderInfoObj;

/**
 * Created by panching on 16/4/23.
 */
public final class ListContentAdapter extends BaseAdapter {
    private Context mContext;
    private List<Map<String, Object>> mList;
    private ViewHolder viewHolder = null;
    HashMap imgTextMap = new HashMap();
    HashMap headerMap;
    OrderInfoObj mOredrInfoObj;
    public ArrayList trainInfoList = null;
    String mFlag;
    // 用户记住CheckBox的状态
    private Map<Integer, Boolean> selectStaus;

    public static Hashtable shareTime = new Hashtable();

    public ListContentAdapter(){
        super();
    }
    public ListContentAdapter(Context context, List<Map<String, Object>> list,OrderInfoObj orderInfoObj,String onTimeFlag) {
        this.mContext = context;
        this.mList = list;
        this.mOredrInfoObj = orderInfoObj;
        this.mFlag = onTimeFlag;
//        this.headerMap = map;

        selectStaus = new HashMap<Integer, Boolean>();
        // 初始状态全部为不选中状态，故设为false
        for (int i = 0; i < list.size(); i++) {
            // 使用当前的位置作为key，boolean作为value
            selectStaus.put(i, false);
        }
    }

    // 在绘制ListView时调用该方法
    @Override
    public int getCount() {
        return mList.size();
    }

    // 返回指定位置的Item
    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    // 不指定id,一般为当前的位置
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 自定义Adapter的最重要的方法: 使用ViewHolder作为一个缓存的对象，可以减少findViewById();
     * 假设一个ListView的空间一次加载10个item，也就是有对应的View
     * 容器ConvertView10个，每次使viewHolder指向该ConvertView,第一个加载时convertView
     * 为null，以后就不是null了； 当滚动ListView时，convertView不为null，从viewholder获取到缓存的视图数据,
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


            if (null == convertView) {
                if(mFlag==null)
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.time_list, null);
                else
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.ontime_list, null);
                viewHolder = new ViewHolder();
                viewHolder.tvTrain = (TextView) convertView.findViewById(R.id.tvTrain);
                viewHolder.tvCarclass = (TextView) convertView.findViewById(R.id.tvCarclass);
                viewHolder.tvArriveTime = (TextView) convertView.findViewById(R.id.tvArriveTime);
                viewHolder.tvTotalTime = (TextView) convertView.findViewById(R.id.tvTotalTime);
                viewHolder.tvMoney = (TextView) convertView.findViewById(R.id.tvMoney);
                viewHolder.imgText = (TextView) convertView.findViewById(R.id.img);
                viewHolder.tvLine = (TextView) convertView.findViewById(R.id.tvLine);
                viewHolder.imgBreastFeed = (ImageView) convertView.findViewById(R.id.imgBreastFeed);
                viewHolder.imgCripple = (ImageView) convertView.findViewById(R.id.imgCripple);
                viewHolder.imgBike = (ImageView) convertView.findViewById(R.id.imgBike);
                viewHolder.imgEveryDay = (ImageView)convertView.findViewById(R.id.imgEveryday);
                viewHolder.imgOrderTicket = (ImageView)convertView.findViewById(R.id.imgOrderTicket);
                viewHolder.tvOrderTicket = (TextView)convertView.findViewById(R.id.tvOrderTicket);
                viewHolder.imgText.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Log.i("viewHolder.setButton", " click " + position);
                    }
                });

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvTrain.setText(mList.get(position).get("tvTrain") + "");
            viewHolder.tvCarclass.setText(mList.get(position).get("tvCarclass") + "");
            viewHolder.tvArriveTime.setText(mList.get(position).get("tvArriveTime") + "");
            viewHolder.tvTotalTime.setText(mList.get(position).get("tvTotalTime") + "");
            viewHolder.tvMoney.setText(mList.get(position).get("tvMoney") + "");
            viewHolder.imgText.setText(mList.get(position).get("tvImgText") + "");
            viewHolder.imgText.setBackgroundResource((int) mList.get(position).get("tvImgBackground"));
            viewHolder.tvLine.setText(mList.get(position).get("tvLine") + "");
            viewHolder.imgBreastFeed.setBackgroundResource(!mList.get(position).get("imgBreastFeed").equals("N") ? (int) mList.get(position).get("imgBreastFeed") : 0);

            viewHolder.imgBike.setBackgroundResource(!mList.get(position).get("imgBike").equals("N") ? (int) mList.get(position).get("imgBike") : 0);

            viewHolder.imgCripple.setBackgroundResource(!mList.get(position).get("imgCripple").equals("N")?(int)mList.get(position).get("imgCripple"):0);

            viewHolder.imgEveryDay.setBackgroundResource(!mList.get(position).get("imgNote").equals("N") ? (int) mList.get(position).get("imgNote") : 0);

            viewHolder.imgOrderTicket.setBackgroundResource(!mList.get(position).get("imgOrderTicket").equals("N") ? (int) mList.get(position).get("imgOrderTicket") : 0);
            if(!mList.get(position).get("imgOrderTicket").equals("N"))
            {
                viewHolder.tvOrderTicket.setText("訂票");
            }
            else
                viewHolder.tvOrderTicket.setText("");

            if (selectStaus.get(position))
            {
                viewHolder.imgText.setText("✓");
                viewHolder.imgText.setBackgroundResource(R.drawable.checked);
            }

            final TextView imgCheck = viewHolder.imgText;
            final TextView tvTrainCheck = viewHolder.tvTrain;
            final TextView tvCarclassCheck = viewHolder.tvCarclass;
            final TextView tvArriveTimeCheck = viewHolder.tvArriveTime;
            final TextView tvTotalTimeCheck = viewHolder.tvTotalTime;
            final TextView tvMoneyCheck = viewHolder.tvMoney;
            final ImageView imgOrderTicket = viewHolder.imgOrderTicket;
            if(selectStaus.get(position))
            {
                imgTextMap.put(position, viewHolder.imgText.getText());
                imgCheck.setText("✓");
                imgCheck.setBackgroundResource(R.drawable.checked);
                selectStaus.put(position, true);
            }
            else
            {
                imgCheck.setText(mList.get(position).get("tvImgText") + "");
                imgCheck.setBackgroundResource((int)mList.get(position).get("tvImgBackground"));
                selectStaus.put(position, false);
            }
            final Animation am = AnimationUtils.loadAnimation(mContext, R.anim.animationset);

            //监听器只能放在这外面才行
            viewHolder.imgText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectStaus.get(position)) {
                        imgCheck.setText(mList.get(position).get("tvImgText") + "");
                        imgCheck.setBackgroundResource((int) mList.get(position).get("tvImgBackground"));
                        imgTextMap.remove(position);
                        shareTime.remove(position);
                        selectStaus.put(position, false);
                        imgCheck.startAnimation(am);
                    } else {
                        shareTime.put(position, "No." + tvTrainCheck.getText() + "," + tvCarclassCheck.getText() + "," + tvArriveTimeCheck.getText() + "," +tvTotalTimeCheck.getText() + "," + tvMoneyCheck.getText() + "元");
                        imgTextMap.put(position, viewHolder.imgText.getText());
                        imgCheck.setText("✓");
                        imgCheck.setBackgroundResource(R.drawable.checked);
                        selectStaus.put(position, true);
                        imgCheck.startAnimation(am);
                    }
                }
            });

            imgOrderTicket.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri= Uri.parse("http://twtraffic.tra.gov.tw/twrail/Ticketing.aspx?" +
                            "from_station=" + mOredrInfoObj.getStartStationID() + "&to_station=" + mOredrInfoObj.getEndStationID() +
                            "&getin_date=" +  mOredrInfoObj.getDate() + "&train_no=" + tvTrainCheck.getText());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);

//                    Intent intent = new Intent(mContext,OrderTicketWebView.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("train",tvTrainCheck.getText()+"");
//                    bundle.putString("fromID",mOredrInfoObj.getStartStationID());
//                    bundle.putString("toID",mOredrInfoObj.getEndStationID());
//                    bundle.putString("date", mOredrInfoObj.getDate());
//                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
;//                    OrderTicketAsyncTask orderTicketAsyncTask = new OrderTicketAsyncTask();
//                    orderTicketAsyncTask.execute();
                }
            });
        return convertView;
    }

    private final class ViewHolder {
         TextView tvTrain ;
         TextView tvCarclass;
         TextView tvArriveTime ;
         TextView tvTotalTime ;
         TextView tvMoney ;
         TextView imgText;
         TextView tvLine;
         TextView tvOrderTicket;
         ImageView imgBreastFeed;
         ImageView imgCripple;
         ImageView imgBike;
         ImageView imgEveryDay;
         ImageView imgOrderTicket;

    }

    public ArrayList getTrainInfo()
    {
        return this.trainInfoList ;
    }
}
