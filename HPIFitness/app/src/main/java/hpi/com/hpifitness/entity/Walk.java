package hpi.com.hpifitness.entity;

/**
 * Created by Georgey on 26-01-2017.
 */

public class Walk {
    private String milestone;

    private String distance;

    private String duration;

    private String date;

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ClassPojo [milestone = " + milestone + ", distance = " + distance + ", duration = " + duration + ", date = " + date + "]";
    }
}
