package com.example.faust.mytestapplication1;

/**
 * Created by robertospaziani on 25/03/17.
 */

import java.util.ArrayList;

public class User {
    private String name;
    private int ImageId;
    private double balance;
    private ArrayList<MyGroup> groups;
    private ArrayList<MyActivity> activity;

    public User(){
        groups= new ArrayList<>();
        activity= new ArrayList<>();
    }

    public User(String s,int id){
        name=s;
        ImageId=id;
        groups= new ArrayList<>();
        activity= new ArrayList<>();
    }

    public User(String s,int id,double b){
        name=s;
        ImageId=id;
        balance=b;
        groups= new ArrayList<>();
        activity= new ArrayList<>();
    }

    public void addGrouponUser(MyGroup g){
        groups.add(g);
    }

    public void addActivityonUser(MyActivity g){
        activity.add(g);
    }

    public ArrayList<MyGroup> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<MyGroup> groups) {
        this.groups = groups;
    }

    public ArrayList<MyActivity> getActivity() {
        return activity;
    }

    public void setActivity(ArrayList<MyActivity> activity) {
        this.activity = activity;
    }

    public String getName(){
        return name;
    }

    public int getImageId(){
        return ImageId;
    }

    public void setName(String s){
        name=s;
    }

    public double getBalance(){
        return balance;
    }

    public void setBalance(Double s){
        balance=s;
    }

    public void setImageId(int id){
        ImageId=id;
    }


}
