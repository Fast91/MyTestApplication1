package com.example.faust.mytestapplication1;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by robertospaziani on 25/03/17.
 */

public class MyActivity {
    private String ID;
    private String version;
    private String name;
    private int ImageId;
    private double balance;
    private Date data;
    private String category;
    private User owner;
    private List<User> usersin;
    private HashMap<User,Integer> divide;
    private MyGroup group;


    public MyActivity(){
        usersin= new ArrayList<>();
        divide= new HashMap<>();

    }

    public MyActivity(String s,int id,double b){
        name=s;
        ImageId=id;
        balance=b;
        usersin= new ArrayList<>();
        divide= new HashMap<>();
    }

    public MyActivity(String s,int id,double b, Date data , String category){
        name=s;
        ImageId=id;
        balance=b;
        this.data=data;
        this.category=category;
        usersin= new ArrayList<>();
        divide= new HashMap<>();
    }

    public MyActivity(String aid, String s,int id,double b, Date data , String category){
        this.ID=aid;
        this.version="0.1";
        name=s;
        ImageId=id;
        balance=b;
        this.data=data;
        this.category=category;
        usersin= new ArrayList<>();
        divide= new HashMap<>();
    }

    public MyGroup getGroup() {
        return group;
    }

    public void setGroup(MyGroup group) {
        this.group = group;
    }


    public String getID()
    {
        return ID;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getUsersin() {
        return usersin;
    }

    public void setUsersin(ArrayList<User> usersin) {
        this.usersin = usersin;
    }

    public void addUserin(User u){

        if(group.getUsers_in_group().contains(u)) {

            this.usersin.add(u);

        }
    }

    public HashMap<User, Integer> getDivide() {
        return divide;
    }

    public void setDivide(HashMap<User, Integer> divide) {
        this.divide = divide;
    }

    public void addDivideforUser(User u,int x){
        divide.put(u,x);
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
