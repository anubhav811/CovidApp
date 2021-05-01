package com.anubhav.covidapp;

public class AvailablePatientModel {
    private String name;
    private String o2;
    private String pulse;
    private String temperature;
    private String lung_capacity;

    private AvailablePatientModel() {
    }

    private AvailablePatientModel(String name, String o2, String lung_capacity ,String pulse,String temperature) {
        this.name = name;
        this.o2 = o2;
        this.lung_capacity= lung_capacity;
        this.pulse = pulse;
        this.temperature = temperature;
    }

    public String getO2() {
        return o2;
    }

    public void setO2(String o2) {
        this.o2 = o2;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getLung_capacity() {
        return lung_capacity;
    }

    public void setLung_capacity(String lung_capacity) {
        this.lung_capacity = lung_capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
