package com.oservice.mina.util;

import java.io.Serializable;

/**
 * Date: 15-1-27
 * Time: 下午6:35
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class DateWeekBean implements Serializable{
    private String date;
    private String week;
    private boolean ispast;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public boolean getIspast() {
        return ispast;
    }

    public void setIspast(boolean ispast) {
        this.ispast = ispast;
    }
}
