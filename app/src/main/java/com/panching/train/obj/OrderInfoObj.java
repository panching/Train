package com.panching.train.obj;

import java.io.Serializable;

import javax.crypto.SecretKey;

/**
 * Created by panching on 16/5/11.
 */
public class OrderInfoObj implements Serializable {
    String StartStationID;
    String EndStationID;
    String Date;
    String TrainNo;

    public OrderInfoObj() {
        super();
    }

    public OrderInfoObj(String startStationID, String endStationID, String date, String trainNo) {
        StartStationID = startStationID;
        EndStationID = endStationID;
        Date = date;
        TrainNo = trainNo;
    }

    public String getStartStationID() {
        return StartStationID;
    }

    public void setStartStationID(String startStationID) {
        StartStationID = startStationID;
    }

    public String getEndStationID() {
        return EndStationID;
    }

    public void setEndStationID(String endStationID) {
        EndStationID = endStationID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTrainNo() {
        return TrainNo;
    }

    public void setTrainNo(String trainNo) {
        TrainNo = trainNo;
    }
}
