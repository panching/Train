package com.panching.train.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.panching.train.TimeActivity;
import com.panching.train.TrainListActivity;
import com.panching.train.obj.TimeInfoObj;
import com.panching.train.obj.TrainInfoObj;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by panching on 16/4/30.
 */
public class TimeAsyncTask extends AsyncTask<Void,Void,Void>{

    String mUrl;
    Context mContext ;
    ProgressDialog mProgressDialog;
    Activity mActivity;
    TrainInfoObj mTrainInfoObj;

    ArrayList timeList = new ArrayList();
    String stateList = "";
    public TimeAsyncTask() {
    }

    public TimeAsyncTask(String mUrl, Context mContext, ProgressDialog mProgressDialog, Activity mActivity,TrainInfoObj trainInfoObj) {
        this.mUrl = mUrl;
        this.mContext = mContext;
        this.mProgressDialog = mProgressDialog;
        this.mActivity = mActivity;
        this.mTrainInfoObj = trainInfoObj;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setTitle("請稍候！");
        mProgressDialog.setMessage("資料讀取中..");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try
        {
            TimeInfoObj timeInfoObj =null;
            List infoList = null;
            String html = senGetDataToInternet(mUrl);
            // Connect to the web site
            Document document = Jsoup.parse(html);
            // Using Elements to get the Meta data
            Element table = document.getElementById("ResultGridView");
            Elements trs = table.getElementsByTag("tr");
            for (int i = 1; i < trs.size(); i++)
            {
                timeInfoObj =  new TimeInfoObj();
                Elements tds = trs.get(i).getElementsByTag("td");
                for(int j = 0;j < tds.size();j++)
                {
                    switch(j)
                    {
                        case 0:
                            timeInfoObj.setOrderID(Integer.parseInt(tds.get(j).text()));
                            break;
                        case 1:
                            timeInfoObj.setStationName(tds.get(j).text());
                            break;
                        case 2:
                            timeInfoObj.setArrTime(tds.get(j).text()+":00");
                            break;
                        case 3:
                            timeInfoObj.setDeption(tds.get(j).text()+":00");
                            break;
                        default:
                            break;
                    }
                }
                if(timeInfoObj.getArrTime().equals(":00"))
                {
                    timeInfoObj.setArrTime(timeInfoObj.getDeption());
                }
                else if(timeInfoObj.getDeption().equals(":00"))
                {
                    timeInfoObj.setDeption(timeInfoObj.getArrTime());
                }
                timeList.add(timeInfoObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {

        Intent intent = new Intent(mContext,TimeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle = new Bundle();
        if(timeList.size()>0)
        {
            bundle.putSerializable("title",mTrainInfoObj.getsStartStation()+","+mTrainInfoObj.getsEndSatation());
            bundle.putSerializable("train",mTrainInfoObj.getiTrain());
            bundle.putSerializable("carClass",mTrainInfoObj.getsCarClassName());
            bundle.putSerializable("timeList",timeList);
            bundle.putSerializable("stateList",mTrainInfoObj.getStationList());
            bundle.putString("sStartSatation", ((TimeInfoObj) timeList.get(0)).getStationName());
            bundle.putString("sEndSatation", ((TimeInfoObj)timeList.get(timeList.size()-1)).getStationName());
            intent.putExtras(bundle);
            mContext.startActivity(intent);
            mProgressDialog.dismiss();
        }

    }

    private String senGetDataToInternet(String url){
        String result ="";
        HttpClient client=null;
        try{
            client = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);
            HttpResponse response = client.execute(get);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                result = EntityUtils.toString(resEntity);
            }
        }catch (Exception e){
            Log.d("Tag", "xxxxxxxxxx", e);
        }
        finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }
}
