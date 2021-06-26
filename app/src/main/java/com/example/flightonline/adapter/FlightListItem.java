package com.example.flightonline.adapter;

public class FlightListItem {
    private String flightID;
    private String startTime;
    private String endTime;
    private String startAirport;
    private String endAirport;
    private int price;
    private int plus;//默认为0，表示当日达，如果是第二天则为1

    public FlightListItem(String flightID,String startTime,String endTime,String startAirport,String endAirport,int price,int plus){
        this.flightID=flightID;
        this.startTime=startTime;
        this.endTime=endTime;
        this.startAirport=startAirport;
        this.endAirport=endAirport;
        this.plus=plus;
        this.price=price;
    }


    public String getFlightID() {
        return flightID;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartAirport() {
        return startAirport;
    }

    public String getEndAirport() {
        return endAirport;
    }

    public int getPrice() {
        return price;
    }

    public int getPlus() {
        return plus;
    }

    public void setFlightID(String flightID) {
        this.flightID = flightID;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setStartAirport(String startAirport) {
        this.startAirport = startAirport;
    }

    public void setEndAirport(String endAirport) {
        this.endAirport = endAirport;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPlus(int plus) {
        this.plus = plus;
    }
}
