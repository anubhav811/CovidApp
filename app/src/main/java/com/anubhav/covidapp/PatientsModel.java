package com.anubhav.covidapp;

public class PatientsModel {

    String name;
    long number;
    String address;
    double o2_level;
    double pulse;
    double temperature;
    double lung_cap;

    private PatientsModel(String name,long number,String address,double o2_level,double pulse,double temperature,double lung_cap){
    }
    PatientsModel(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getO2_level() {
        return o2_level;
    }

    public void setO2_level(double o2_level) {
        this.o2_level = o2_level;
    }

    public double getPulse() {
        return pulse;
    }

    public void setPulse(double pulse) {
        this.pulse = pulse;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getLung_cap() {
        return lung_cap;
    }

    public void setLung_cap(double lung_cap) {
        this.lung_cap = lung_cap;
    }
}