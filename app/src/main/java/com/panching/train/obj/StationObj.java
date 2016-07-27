package com.panching.train.obj;

/**
 * Created by panching on 16/4/13.
 */
public class StationObj
{
    Integer id;
    Integer areaID;
    Integer station;
    String nameTW;
    String nameEN;


    public StationObj() {
        super();
    }

    public StationObj(Integer id, Integer areaID, Integer station, String nameTW, String nameEN) {
        this.id = id;
        this.areaID = areaID;
        this.station = station;
        this.nameTW = nameTW;
        this.nameEN = nameEN;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAreaID() {
        return areaID;
    }

    public void setAreaID(Integer areaID) {
        this.areaID = areaID;
    }

    public Integer getStation() {
        return station;
    }

    public void setStation(Integer station) {
        this.station = station;
    }

    public String getNameTW() {
        return nameTW;
    }

    public void setNameTW(String nameTW) {
        this.nameTW = nameTW;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }
}
