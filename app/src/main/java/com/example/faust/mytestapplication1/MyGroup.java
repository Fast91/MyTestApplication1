package com.example.faust.mytestapplication1;

/**
 * Created by robertospaziani on 25/03/17.
 */

public class MyGroup {
    private String name;
    private int ImageId;
    private double balance;

    public MyGroup(){

    }

    public MyGroup(String s,int id,double b){
        name=s;
        ImageId=id;
        balance=b;
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
