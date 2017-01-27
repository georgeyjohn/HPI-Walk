package hpi.com.hpifitness.entity;

import java.util.ArrayList;

/**
 * Created by Georgey on 26-01-2017.
 */

public class User {
    private String username;

    private String password;

    private String profileid;

    private String fullname;

    private ArrayList<Walk> walk;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileid() {
        return profileid;
    }

    public void setProfileid(String profileid) {
        this.profileid = profileid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public ArrayList<Walk> getWalk() {
        return walk;
    }

    public void setWalk(ArrayList<Walk> walk) {
        this.walk = walk;
    }
}
