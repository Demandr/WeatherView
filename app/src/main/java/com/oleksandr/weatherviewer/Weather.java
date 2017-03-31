package com.oleksandr.weatherviewer;

/**
 * Created by Oleksandr on 28.03.2017.
 */

    public class Weather {
    private String icon = new String();
    private String date = new String();
    private String time = new String();
    private String temp = new String();
    private String humidity = new String();
    private String pressure = new String();
    private String cloudiness = new String();

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(String cloudiness) {
        this.cloudiness = cloudiness;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }
}
