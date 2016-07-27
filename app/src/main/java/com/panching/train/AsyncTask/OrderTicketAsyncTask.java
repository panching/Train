package com.panching.train.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.panching.train.TimeActivity;
import com.panching.train.obj.TimeInfoObj;
import com.panching.train.obj.TrainInfoObj;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by panching on 16/4/30.
 */
public class OrderTicketAsyncTask extends AsyncTask<Void,Void,Void>{

    String mUrl;
    Context mContext ;
    ProgressDialog mProgressDialog;
    Activity mActivity;
    TrainInfoObj mTrainInfoObj;
    HashMap hashMap ;

    ArrayList timeList = new ArrayList();
    String stateList = "";
    public OrderTicketAsyncTask() {
    }

    public OrderTicketAsyncTask(HashMap map) {
        this.hashMap = map;
    }
    public OrderTicketAsyncTask(String mUrl, Context mContext, ProgressDialog mProgressDialog, Activity mActivity, TrainInfoObj trainInfoObj) {
        this.mUrl = mUrl;
        this.mContext = mContext;
        this.mProgressDialog = mProgressDialog;
        this.mActivity = mActivity;
        this.mTrainInfoObj = trainInfoObj;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        mProgressDialog = new ProgressDialog(mActivity);
//        mProgressDialog.setTitle("請稍候！");
//        mProgressDialog.setMessage("資料讀取中..");
//        mProgressDialog.setIndeterminate(false);
//        mProgressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try
        {
            sendPostDataToInternet();
//            String html = sendPostDataToInternet();
//            Document document = Jsoup.parse(html);
//            // Using Elements to get the Meta data
//            Element table = document.getElementById("idRandomPic");
//            Log.d("ooo",table.getElementsByAttribute("src").toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {


    }

    private String sendPostDataToInternet()
    {
        HttpPost httpRequest = new HttpPost("http://railway.hinet.net/ctno1.htm?from_station=100&to_station=185&getin_date=2016/05/10&train_no=175");
        HttpPost httpRequest2 = new HttpPost("http://railway.hinet.net/check_ctno1.jsp");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("from_station", "100"));
        params.add(new BasicNameValuePair("to_station", "185"));
        params.add(new BasicNameValuePair("getin_date", "2016/05/11"));
        params.add(new BasicNameValuePair("train_no", "125"));
        try
        {
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);

            if (httpResponse.getStatusLine().getStatusCode() == 200)
            {
                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                params2.add(new BasicNameValuePair("person_id", ""));
                params2.add(new BasicNameValuePair("from_station", "100"));
                params2.add(new BasicNameValuePair("to_station", "185"));
                params2.add(new BasicNameValuePair("getin_date", "2016%2F05%2F11-00"));
                params2.add(new BasicNameValuePair("train_no", "125"));
                params2.add(new BasicNameValuePair("order_qty_str", "1"));
                params2.add(new BasicNameValuePair("t_order_qty_str", "0"));
                params2.add(new BasicNameValuePair("n_order_qty_str", "0"));
                params2.add(new BasicNameValuePair("d_order_qty_str", "0"));
                params2.add(new BasicNameValuePair("b_order_qty_str", "0"));
                params2.add(new BasicNameValuePair("z_order_qty_str", "0"));
                params2.add(new BasicNameValuePair("returnTicket", "0"));
                httpRequest2.setEntity(new UrlEncodedFormEntity(params2, HTTP.UTF_8));

                HttpResponse httpResponse2 = new DefaultHttpClient().execute(httpRequest2);
                Header header[] = httpResponse2.getAllHeaders();
                for (int i = 0;i<header.length;i++)
                {
                    Log.d("strResult",header[i]+"＝＝＝");
                }

                String strResult = "";
                if (httpResponse2.getStatusLine().getStatusCode() == 200)
                {
                    Log.d("httpResponse2",httpResponse2.getEntity().getContentLength()+"");
                    HttpEntity resEntity = httpResponse2.getEntity();

                    if (resEntity != null) {
                        strResult = EntityUtils.toString(resEntity);
                    }
                    Log.d("xxx","xxx");
                    Log.d("strResult",strResult+"-----");
//                    return strResult;
                }
                else
                {
                    HttpEntity resEntity = httpResponse2.getEntity();

                    if (resEntity != null) {

                        strResult = EntityUtils.toString(resEntity);
                    }
                    Log.d("strResult",strResult);
                    Log.d("yyy","yyy");
                }

            }
            else
            {
                Log.d("xxx","xxx");
            }
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
//            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return null;
    }

}
