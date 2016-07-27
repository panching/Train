package com.panching.train.Fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.panching.train.Adapter.SettingItemAdapter;
import com.panching.train.R;

import java.util.ArrayList;

/**
 * Created by panching on 16/5/19.
 */
public class SettingFragment extends Fragment
{
    SettingItemAdapter settingItemAdapter ;
    ArrayList itemDecList = new ArrayList();
    ArrayList itemIconList = new ArrayList();
    View view;
    ListView listView ;
    Item[] items ;
    ListAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, container);
    }
    private View initView(LayoutInflater inflater, ViewGroup container)
    {
        view = inflater.inflate(R.layout.setting_frgment, container, false);

        //防止fragment穿透 預設ontouch return false 改 true
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        findViews(view);

        setSettingItem(0);
        setSettingItem(1);
        settingItemAdapter = new SettingItemAdapter(itemDecList,itemIconList,getActivity().getApplicationContext());
        listView.setAdapter(settingItemAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id)
            {
                switch ((int) id) {
                    case 0:
                        items = new Item[]
                                {
                                    new Item("離線查", R.drawable.offline),
                                    new Item("線上查", R.drawable.online)
                                };
                        break;
                    case 1:
                        items = new Item[]
                                {
                                    new Item("手動更新", R.drawable.none),
                                    new Item("每天更新", R.drawable.every),
                                    new Item("每週更新", R.drawable.week)
                                };
                        break;
                    case 2:
                        items = new Item[]
                                {
                                    new Item("顏色一", R.drawable.color1),
                                    new Item("顏色二", R.drawable.color2),
                                    new Item("顏色三", R.drawable.color3)
                                };
                        break;
                    default:
                        break;
                }

                adapter = new ArrayAdapter<Item>(getActivity(),android.R.layout.select_dialog_item,android.R.id.text1,items){
                    public View getView(int position, View convertView, ViewGroup parent) {
                        //Use super class to create the View
                        View v = super.getView(position, convertView, parent);
                        TextView tv = (TextView)v.findViewById(android.R.id.text1);

                        //Put the image on the TextView
                        tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);

                        //Add margin between image and text (support various screen densities)
                        int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                        tv.setCompoundDrawablePadding(dp5);

                        return v;
                    }
                };

                new AlertDialog.Builder(getActivity()).setTitle((String) itemDecList.get((int) id))
                        .setAdapter(adapter, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if((int) id == 0)
                                {
                                    SharedPreferences pre = getActivity().getSharedPreferences("se", getActivity().MODE_PRIVATE);
                                    SharedPreferences.Editor preEdit = pre.edit();
                                    Toast.makeText(getActivity(), items[which]+" 模式設定成功！", Toast.LENGTH_SHORT).show();
                                }
                                else if((int) id == 1)
                                {
                                    if(which==0)
                                        Toast.makeText(getActivity(), items[which]+" 更新成功！", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(getActivity(), items[which]+" 模式設定成功！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setPositiveButton("取消", null).show();
            }
        });
        return view;
    }

    private void findViews(View view)
    {
        listView = (ListView)view.findViewById(R.id.settingListView);
    }
    private void setSettingItem(int type)
    {
        if(type == 0)
        {
            itemDecList.add("時刻表搜尋設定");
            itemDecList.add("資料庫更新");
//            itemDecList.add("背景顏色更改");
        }
        else
        {
            itemIconList.add(R.drawable.search_100);
            itemIconList.add(R.drawable.database_100);
//            itemIconList.add(R.drawable.color_100);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.removeItem(R.id.star);
    }

    public static class Item{
        public final String text;
        public final int icon;
        public Item(String text, Integer icon) {
            this.text = text;
            this.icon = icon;
        }
        @Override
        public String toString() {
            return text;
        }
    }
}
