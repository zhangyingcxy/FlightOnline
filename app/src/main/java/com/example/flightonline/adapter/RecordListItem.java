package com.example.flightonline.adapter;

public class RecordListItem {
    private String flightID;
    private String startTime;
    private String endTime;
    private String startAirport;
    private String endAirport;
    private String date;
    private int price;
    private int plus;//默认为0，表示当日达，如果是第二天则为1
    private String name;
    private String startCity;
    private String endCity;
    private String teleNumber;
    private String idNumber;


    public RecordListItem(String flightID,String startTime,String endTime,String startAirport,String endAirport,String date,int price,
                          int plus,String startCity,String endCity,String name,String teleNumber,String idNumber){
        this.flightID=flightID;
        this.startTime=startTime;
        this.endTime=endTime;
        this.startAirport=startAirport;
        this.endAirport=endAirport;
        this.date=date;
        this.plus=plus;
        this.price=price;
        this.startCity=startCity;
        this.endCity=endCity;
        this.name=name;
        this.teleNumber=teleNumber;
        this.idNumber=idNumber;
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

    public String getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }

    public int getPlus() {
        return plus;
    }

    public String getStartCity() {
        return startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public String getName() {
        return name;
    }

    public String getTeleNumber() {
        return teleNumber;
    }

    public String getIdNumber() {
        return idNumber;
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

    public void setDate(String date) {
        this.date = date;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPlus(int plus) {
        this.plus = plus;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeleNumber(String teleNumber) {
        this.teleNumber = teleNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
}
