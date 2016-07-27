package com.panching.train;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.panching.train.AsyncTask.OrderTicketAsyncTask;
import com.panching.train.AsyncTask.OrderWebViewAsyncTask;
import com.panching.train.AsyncTask.TimeAsyncTask;
import com.panching.train.obj.ContextObj;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class OrderTicketWebView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_ticket_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("");

        String fromStation = getIntent().getStringExtra("fromID");
        String toStation = getIntent().getStringExtra("toID");
        String date = getIntent().getStringExtra("date");
        String trainNo = getIntent().getStringExtra("train");

        WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.requestFocus();

        myWebView.loadUrl("http://twtraffic.tra.gov.tw/twrail/Ticketing.aspx?" +
                "from_station=" + fromStation + "&to_station=" + toStation +
                "&getin_date=" + date + "&train_no=" + trainNo);

//        myWebView.setWebViewClient(new MyWebViewClient());
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyWebViewClient extends WebViewClient {
        //        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String urlConection) {
            OrderWebViewAsyncTask orderWebViewAsyncTask = new OrderWebViewAsyncTask(urlConection,view,getApplicationContext());
            orderWebViewAsyncTask.execute();
            return true;
        }

    }
}

