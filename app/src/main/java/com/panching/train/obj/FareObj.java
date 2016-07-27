package com.panching.train.obj;

/**
 * Created by panching on 16/4/18.
 */
public class FareObj
{
    Integer id;
    Integer LineDir;
    Integer TzeChiang;
    Integer ChuKuang;
    Integer FuHsing;
    Integer LocalFare;
    String st_NameA;
    Integer st_IDA;
    String st_NameB;
    Integer st_IDB;

    public FareObj(){super();}
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLineDir() {
        return LineDir;
    }

    public void setLineDir(Integer lineDir) {
        LineDir = lineDir;
    }

    public Integer getTzeChiang() {
        return TzeChiang;
    }

    public void setTzeChiang(Integer tzeChiang) {
        TzeChiang = tzeChiang;
    }

    public Integer getChuKuang() {
        return ChuKuang;
    }

    public void setChuKuang(Integer chuKuang) {
        ChuKuang = chuKuang;
    }

    public Integer getFuHsing() {
        return FuHsing;
    }

    public void setFuHsing(Integer fuHsing) {
        FuHsing = fuHsing;
    }

    public Integer getLocalFare() {
        return LocalFare;
    }

    public void setLocalFare(Integer localFare) {
        LocalFare = localFare;
    }

    public String getSt_NameA() {
        return st_NameA;
    }

    public void setSt_NameA(String st_NameA) {
        this.st_NameA = st_NameA;
    }

    public Integer getSt_IDA() {
        return st_IDA;
    }

    public void setSt_IDA(Integer st_IDA) {
        this.st_IDA = st_IDA;
    }

    public String getSt_NameB() {
        return st_NameB;
    }

    public void setSt_NameB(String st_NameB) {
        this.st_NameB = st_NameB;
    }

    public Integer getSt_IDB() {
        return st_IDB;
    }

    public void setSt_IDB(Integer st_IDB) {
        this.st_IDB = st_IDB;
    }

    public FareObj(Integer id, Integer lineDir, Integer tzeChiang, Integer chuKuang, Integer fuHsing, Integer localFare, String st_NameA, Integer st_IDA, String st_NameB, Integer st_IDB) {
        this.id = id;
        LineDir = lineDir;
        TzeChiang = tzeChiang;
        ChuKuang = chuKuang;
        FuHsing = fuHsing;
        LocalFare = localFare;
        this.st_NameA = st_NameA;
        this.st_IDA = st_IDA;
        this.st_NameB = st_NameB;
        this.st_IDB = st_IDB;
    }
}
