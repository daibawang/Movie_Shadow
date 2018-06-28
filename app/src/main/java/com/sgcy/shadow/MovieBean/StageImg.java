package com.sgcy.shadow.MovieBean;

import org.litepal.crud.LitePalSupport;

/**
 * Created by dai on 2018/6/25.
 */

public class StageImg extends LitePalSupport {
    private int imgId;
    private String imgUrl;
    private int id;
    private long moviedid;
    public long getMoviedid() {
        return moviedid;
    }

    public void setMoviedid(long moviedid) {
        this.moviedid = moviedid;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}