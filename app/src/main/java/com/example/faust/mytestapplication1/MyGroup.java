package com.example.faust.mytestapplication1;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by robertospaziani on 25/03/17.
 */

public class MyGroup {
    private String name;
    private int ImageId;
    private double balance;
    private ArrayList<User> users_in_group;
    private ArrayList<MyActivity> activity_in_group;
    private int idgroup;

    public int getIdgroup() {
        return idgroup;
    }

    public void setIdgroup(int idgroup) {
        this.idgroup = idgroup;
    }



    public MyGroup(){
        users_in_group=new ArrayList<>();
        activity_in_group=new ArrayList<>();

    }

    public MyGroup(String s){
        name=s;
        users_in_group=new ArrayList<>();
        activity_in_group=new ArrayList<>();
    }

    public MyGroup(String s,int id,double b){
        name=s;
        ImageId=id;
        balance=b;
        users_in_group=new ArrayList<>();
        activity_in_group=new ArrayList<>();
    }

    public void addActivityinGroup(MyActivity a){
        activity_in_group.add(a);
    }


    public ArrayList<MyActivity> getActivity_in_group() {
        return activity_in_group;
    }

    public void setActivity_in_group(ArrayList<MyActivity> activity_in_group) {
        this.activity_in_group = activity_in_group;
    }


    public ArrayList<User> getUsers_in_group() {
        return users_in_group;
    }

    public void setUsers_in_group(ArrayList<User> users_in_group) {
        this.users_in_group = users_in_group;
    }

    public void addUserinGroup(User u){
        users_in_group.add(u);
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
