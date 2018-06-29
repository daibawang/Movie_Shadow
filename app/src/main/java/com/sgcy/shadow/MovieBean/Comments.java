package com.sgcy.shadow.MovieBean;

import org.litepal.crud.LitePalSupport;

/**
 * Created by dai on 2018/6/29.
 */

public class Comments extends LitePalSupport {
    private String ca;
    private String caimg;
    private String cal;
    private int cd;
    private String ce;
    private int cr;
    private boolean isHot;
    private int movieid;
    private int id;

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public String getCaimg() {
        return caimg;
    }

    public void setCaimg(String caimg) {
        this.caimg = caimg;
    }

    public String getCal() {
        return cal;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }

    public int getCd() {
        return cd;
    }

    public void setCd(int cd) {
        this.cd = cd;
    }

    public String getCe() {
        return ce;
    }

    public void setCe(String ce) {
        this.ce = ce;
    }

    public int getCr() {
        return cr;
    }

    public void setCr(int cr) {
        this.cr = cr;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public int getMovieid() {
        return movieid;
    }

    public void setMovieid(int movieid) {
        this.movieid = movieid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
