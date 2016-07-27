package com.panching.train;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.panching.train.obj.AreaObj;

import java.util.ArrayList;
import java.util.List;

public class AreaActivity extends AppCompatActivity {

    ListView listView;
    List list;
    ArrayAdapter arrayAdapter;
    ArrayList areaList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_reply_white_18dp);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AreaActivity.this.finish();
            }
        });

        findViews();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        final String onTimeFlag = getIntent().getStringExtra("onTimeFlag");

        try
        {
            dataBaseHelper.openDataBase();
            list = dataBaseHelper.getAll("AreaInfo");
            for(int i =0;i<list.size();i++)
            {
                AreaObj areaObj = (AreaObj)list.get(i);

                areaList.add(areaObj.getArea());
            }
            arrayAdapter = new ArrayAdapter(this,R.layout.arrayadapter_layout,R.id.listTextView1,areaList);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent intent = new Intent(AreaActivity.this, StationActivity.class);
                    Bundle mainBundle = new Bundle();
                    mainBundle.putInt("AreaID", position);//傳遞
                    mainBundle.putString("onTimeFlag", onTimeFlag);
//                    //將Bundle物件傳給intent
                    intent.putExtras(mainBundle);

//requestCode(識別碼) 型別為 int ,從B傳回來的物件將會有一樣的requestCode
                    startActivityForResult(intent, 1);
                    if(onTimeFlag!=null)
                    {
                        AreaActivity.this.finish();
                    }
                }
            });

        }
        catch (Exception e)
        {
            Log.d("exception","=",e);
        }
    }
    public void findViews(){
        listView = (ListView)findViewById(R.id.lvArea);
    }
    //接收回傳值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(resultCode){//resultCode是剛剛妳A切換到B時設的resultCode
            case 1://當B傳回來的Intent的requestCode 等於當初A傳出去的話
            {
                if(data != null)
                {
                    String stationName = data.getExtras().getString("stationName");
                    String stationID = data.getExtras().getString("stationID");
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putString("stationName",stationName);
                    bundle.putString("stationID",stationID);
                    intent.putExtras(bundle);
                    setResult(0, intent); //requestCode需跟A.class的一樣
                    AreaActivity.this.finish();
                }

                break;
            }



        }

    }
}
