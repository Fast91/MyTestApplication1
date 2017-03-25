package com.example.faust.mytestapplication1;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by robertospaziani on 25/03/17.
 */

public class MyActivity {
    private String name;
    private int ImageId;
    private double balance;
    private Date data;
    private String category;


    public MyActivity(){

    }

    public MyActivity(String s,int id,double b){
        name=s;
        ImageId=id;
        balance=b;
    }

    public MyActivity(String s,int id,double b, Date data , String category){
        name=s;
        ImageId=id;
        balance=b;
        this.data=data;
        this.category=category;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    private ArrayList<User> users;



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
