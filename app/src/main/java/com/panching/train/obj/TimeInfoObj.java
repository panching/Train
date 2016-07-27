package com.panching.train.obj;

import java.io.Serializable;

/**
 * Created by panching on 16/4/25.
 */
public class TimeInfoObj implements Serializable
{
    String stationName;
    String train;
    String arrTime;
    String deption;
    int orderID;

    public TimeInfoObj(){
        super();
    }
    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getTrain() {
        return train;
    }

    public void setTrain(String train) {
        this.train = train;
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public String getDeption() {
        return deption;
    }

    public void setDeption(String deption) {
        this.deption = deption;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public TimeInfoObj(String stationName, String train, String arrTime, String deption, int orderID) {

        this.stationName = stationName;
        this.train = train;
        this.arrTime = arrTime;
        this.deption = deption;
        this.orderID = orderID;
    }
}
