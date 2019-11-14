package com.example.sidh.quakereport;

/**
 * Created by sidh on 1/30/2017.
 */
public class Information
{
    private String loc;
    private double mag;
    private long time;
    private String url;
    public Information(double mag,String loc,long time,String url)
    {
        this.mag=mag;
        this.loc=loc;
        this.time=time;
        this.url=url;
    }
    public double getMag()
    {
        return mag;
    }
    public String getLoc()
    {
        return loc;
    }
    public long getTime()
    {
        return time;
    }
    public String getUrl() { return url;}
}
