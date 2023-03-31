package com.swiftsync.data;

public class ProcessDuration {

    private int days;
    private int hours;
    private int minutes;
    private int seconds;
    private double milliseconds;

    public ProcessDuration(){
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
        this.milliseconds = 0;
    }

    public void setHours(int hr){
        this.hours = hr;
    }
    public void setMinutes(int min){
        this.minutes = min;
    }
    public void setSeconds(int sec){
        this.seconds = sec;
    }
    public void setMilliseconds(double ms){
        this.milliseconds = ms;
    }
    public void setDays(int da){
        this.days = da;
    }

    public String toString(){
        return this.days + ":" + this.hours + ":" + this.minutes + ":" + this.seconds + "::" + this.milliseconds;
    }

}
