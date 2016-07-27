package com.panching.train.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import com.panching.train.obj.ContextObj;
import com.panching.train.obj.TrainInfoObj;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by panching on 16/4/30.
 */
public class OrderWebViewAsyncTask extends AsyncTask<Void,Void,Void>{

    String mUrl;
    Context mContext;
    CookieManager cookieManager;
    WebView mView;
    String htmlContent ;
    Cookie cookie1 = null;
    Cookie cookie2 = null;

    public OrderWebViewAsyncTask() {
    }

    public OrderWebViewAsyncTask(String mUrl, WebView mView,Context context) {
        this.mUrl = mUrl;
        this.mView = mView;
        this.mContext = context;
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
            htmlContent = senGetDataToInternet(mUrl);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        Cookie sessionCookie1 = cookie1;
        Cookie sessionCookie2 = cookie2;
        CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager = CookieManager.getInstance();
        if (sessionCookie1 != null) {
            cookieManager.removeSessionCookie();

            String cookieString1 = sessionCookie1.getName() + "=" + sessionCookie1.getValue() + "; domain=" + sessionCookie1.getDomain()+ "; path=" + sessionCookie1.getPath()+ "; expiry=" + sessionCookie1.getExpiryDate();
            String cookieString2 = sessionCookie2.getName() + "=" + sessionCookie2.getValue() + "; domain=" + sessionCookie2.getDomain()+ "; path=" + sessionCookie2.getPath()+ "; expiry=" + sessionCookie2.getExpiryDate();

            cookieManager.setCookie(sessionCookie1.getDomain(), cookieString1);
            cookieManager.setCookie(sessionCookie2.getDomain(), cookieString2);
            CookieSyncManager.getInstance().sync();
            Log.d("cookieString1", cookieString1);
            Log.d("cookieString2",cookieString2);

        }
        mView.getSettings().setJavaScriptEnabled(true);
        mView.loadUrl(mUrl);


    }

    public String senGetDataToInternet(String url){
        String result ="";
        DefaultHttpClient client=null;
        try{
            client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://railway.hinet.net/check_ctno1.jsp");
            HttpGet get = new HttpGet(url);

            HttpRequest request =  new HttpGet(url);
            HttpResponse postResponse = client.execute(post);
            {
                List cookies = client.getCookieStore().getCookies();
                if (!cookies.isEmpty()) {
                    for (int i = 0; i < 1; i++) {
                        cookie1 = (Cookie)cookies.get(i);
                        Log.d("cookie1",cookie1+"");
                    }
                }
            }
            HttpResponse response = client.execute(get);
            {
                List cookies = client.getCookieStore().getCookies();
                if (!cookies.isEmpty()) {
                    for (int i = 1; i < cookies.size(); i++) {
                        cookie2 = (Cookie)cookies.get(i);
                        Log.d("cookie2",cookie2+"");
                    }
                }
            }
//            Object Obj[] = request.getAllHeaders();
//
//            for (Object headerObj:Obj)
//            {
//                String sHeader = String.valueOf(headerObj);
//                Log.d("sHeader",sHeader);
//            }
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
