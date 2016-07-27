package com.panching.train;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.panching.train.obj.AreaObj;
import com.panching.train.obj.FareObj;
import com.panching.train.obj.StationObj;
import com.panching.train.obj.DataObj;
import com.panching.train.obj.TimeInfoObj;
import com.panching.train.obj.TrainInfoObj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by panching on 16/4/12.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.panching.train/databases/";

    private static String DB_NAME = "trainData.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error(e);

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            File file = new File(myPath);
            if (file.exists() && !file.isDirectory())
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }
        Log.d("checkDB",checkDB != null ?"O":"X");
        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;
        Log.d("locataion",outFileName);
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.
    public List getAll(String tableName) {

        List result = new ArrayList<>();
        Cursor cursor = myDataBase.query(
                tableName, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    public AreaObj getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        AreaObj areaObj = new AreaObj();
        areaObj.setId(cursor.getInt(0));
        areaObj.setArea(cursor.getString(1));
        areaObj.setAreaID(cursor.getInt(2));
        // 回傳結果
        return areaObj;
    }
    /*
        取得站名列表
     */
    public List getStationList(int AreaID)
    {
        List list = new ArrayList();
        Cursor cursor = myDataBase.rawQuery("select * from stationInfo where AreaID ='"+AreaID+"'",null);

        while (cursor.moveToNext()) {
            list.add(getStationRecord(cursor));
        }

        cursor.close();
        return list;
    }

    public StationObj getStationRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        StationObj stationObj = new StationObj();
        stationObj.setId(cursor.getInt(0));
        stationObj.setAreaID(cursor.getInt(1));
        stationObj.setStation(cursor.getInt(2));
        stationObj.setNameTW(cursor.getString(3));
        stationObj.setNameEN(cursor.getString(4));
        // 回傳結果
        return stationObj;
    }
    /*
        取得查詢時刻表
     */
    public Hashtable getTrainList(String startStationID,String endStationID,String Date,String Time)
    {
        String queryTimeListSql = "select f3.name_tw as StartStation,f6.name_tw as EndStation ,f1.train,f4.LineDir,f1.DEPtime as StartTime,f2.arrtime as EndTime,f5.name_tw,f4.* from timeinfo as f1 inner join timeinfo as f2 on f1.train = f2.train" +
                " left outer join stationinfo as f3 on f1.station = f3.station"+
                " left outer join stationinfo as f6 on f2.station = f6.station"+
                " left outer join traininfo as f4 on f1.train = f4.train"+
                " left outer join carclassinfo as f5 on f4.carclass = f5.carclass"+
                " where (f1.station='"+startStationID+"' and  f2.station ='"+endStationID+"')" +
                " and f1.Date='" +Date.replace("/","")+"'"+
                " and(f1.orderID<f2.orderID) order by f1.arrtime,f1.orderID ";

        String queryFareSql = "select DISTINCT f1.* from fareinfo as f1" +
                " left outer join timeinfo as f2 on f2.station = f1.st_idA" +
                " where (f1.st_idA = '"+startStationID+"' and f1.st_idB='"+endStationID+"')";

        Hashtable table = new Hashtable();
        Hashtable fareTable = new Hashtable();
        List timeList = new ArrayList();
        List trainList = new ArrayList();

        Cursor FareCursor = myDataBase.rawQuery(queryFareSql,null);

        while (FareCursor.moveToNext()) {
            FareObj fareObj = getFare(FareCursor);
            fareTable.put((Integer) fareObj.getLineDir(), fareObj);
        }
        FareCursor.close();
        Cursor cursor = myDataBase.rawQuery(queryTimeListSql,null);


        while (cursor.moveToNext()) {
            DataObj dataObj = getTimeList(cursor,Time);
            if(dataObj!=null)
            {
                FareObj fareObj = (FareObj)fareTable.get((Integer)dataObj.getiLineDir());
                if(dataObj.getsCarClassName().indexOf("區間")!=-1)
                    dataObj.setiFare(fareObj.getFuHsing());
                else if(dataObj.getsCarClassName().indexOf("復興")!=-1)
                    dataObj.setiFare(fareObj.getFuHsing());
                else if(dataObj.getsCarClassName().indexOf("莒光")!=-1)
                    dataObj.setiFare(fareObj.getChuKuang());
                else if(dataObj.getsCarClassName().indexOf("自強") != -1)
                    dataObj.setiFare(fareObj.getTzeChiang());
                else
                    dataObj.setiFare(fareObj.getTzeChiang());
                timeList.add(dataObj);
                trainList.add(cursor.getInt(0));   //取得班次
            }
        }

        table.put("timeList", timeList);
        cursor.close();
        return table;
    }

    public List getTimeList (int train)
    {
        String queryTimeListSql = "select f2.name_tw,f1.arrtime,f1.deptime,f1.orderId from timeinfo as f1 inner join stationinfo as f2 on f1.station =f2.station where train = '"+train+"'";
        Cursor cursor = myDataBase.rawQuery(queryTimeListSql,null);
        List timeList = new ArrayList();

        while (cursor.moveToNext())
        {
            TimeInfoObj timeInfoObj = new TimeInfoObj();
            timeInfoObj.setStationName(cursor.getString(0));
            timeInfoObj.setArrTime(cursor.getString(1));
            timeInfoObj.setDeption(cursor.getString(2));
            timeInfoObj.setOrderID(cursor.getInt(3));
            timeList.add(timeInfoObj);
        }

        return timeList;
    }

    public DataObj getTimeList(Cursor cursor,String Time)
    {
        Time = Time+":00";
        String queryTime = cursor.getString(4);
        Date date1=new Date("2016/1/1 "+Time);
        Date date2=new Date("2016/1/1 "+queryTime);
        if (date2.after(date1))
        {
            // 準備回傳結果用的物件
            DataObj dataObj = new DataObj();
            dataObj.setsTitle(cursor.getString(0) + " , " + cursor.getString(1));
            dataObj.setTrain(cursor.getInt(2));
            dataObj.setiLineDir(cursor.getInt(3));
            dataObj.setStartTime(cursor.getString(4));
            dataObj.setEndTime(cursor.getString(5));
            dataObj.setsCarClassName(cursor.getString(6));
    //        dataObj.setsRoute(cursor.getString(7));
            dataObj.setiLine(cursor.getInt(11));

    //        dataObj.setiLineDir(cursor.getInt(9));
    //        dataObj.setsOverNightStn(cursor.getString(10));
            dataObj.setsCripple(cursor.getString(14));
    //        dataObj.setsPackage(cursor.getString(12));
    //        dataObj.setsDinning(cursor.getString(13));
            dataObj.setiType(cursor.getInt(17));
            dataObj.setsBreastFeed(cursor.getString(18));
            dataObj.setsBike(cursor.getString(19));
            dataObj.setsNote(cursor.getString(20));
    //        dataObj.setsNoteEng(cursor.getString(18));
            // 回傳結果
            return dataObj;
        }
        else return null;
    }
    public FareObj getFare(Cursor cursor)
    {
        FareObj fareObj =new FareObj();
        fareObj.setLineDir(cursor.getInt(1));
        fareObj.setTzeChiang(cursor.getInt(2));
        fareObj.setChuKuang(cursor.getInt(3));
        fareObj.setFuHsing(cursor.getInt(4));
        fareObj.setLocalFare(cursor.getInt(5));
        fareObj.setSt_NameA(cursor.getString(6));
        fareObj.setSt_IDA(cursor.getInt(7));
        fareObj.setSt_NameB(cursor.getString(8));
        fareObj.setSt_IDB(cursor.getInt(9));

        return fareObj;
    }

    public TrainInfoObj getTrainList(Cursor cursor) {
        // 準備回傳結果用的物件
        TrainInfoObj trainInfoObj = new TrainInfoObj();
        trainInfoObj.setId(cursor.getLong(0));
        trainInfoObj.setiTrain(cursor.getInt(1));
        trainInfoObj.setiCarClass(cursor.getInt(2));
        trainInfoObj.setsRoute(cursor.getString(3));
        trainInfoObj.setiLine(cursor.getInt(4));
        trainInfoObj.setiLineDir(cursor.getInt(5));
        trainInfoObj.setsOverNightStn(cursor.getString(6));
        trainInfoObj.setsCripple(cursor.getString(7));
        trainInfoObj.setsPackage(cursor.getString(8));
        trainInfoObj.setsDinning(cursor.getString(9));
        trainInfoObj.setiType(cursor.getInt(10));
        trainInfoObj.setsBreastFeed(cursor.getString(11));
        trainInfoObj.setsBike(cursor.getString(12));
        trainInfoObj.setsNote(cursor.getString(13));
        trainInfoObj.setsNoteEng(cursor.getString(14));
        trainInfoObj.setiOrderID(cursor.getInt(15));
        trainInfoObj.setsStationName(cursor.getString(16));
        trainInfoObj.setsCarClassName(cursor.getString(17));

        // 回傳結果
        return trainInfoObj;
    }

}