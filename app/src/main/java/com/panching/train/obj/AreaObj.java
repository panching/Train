package com.panching.train.obj;

/**
 * Created by panching on 16/4/12.
 */
public class AreaObj
{
    Integer id ;
    String area;
    Integer areaID;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getAreaID() {
        return areaID;
    }

    public void setAreaID(Integer areaID) {
        this.areaID = areaID;
    }

    public AreaObj() {
        super();

    }

    public AreaObj(Integer id, String area, Integer areaID) {
        this.id = id;
        this.area = area;
        this.areaID = areaID;
    }
}
