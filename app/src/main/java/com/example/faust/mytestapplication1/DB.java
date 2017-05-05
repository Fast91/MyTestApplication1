package com.example.faust.mytestapplication1;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertospaziani on 30/03/17.
 */

public class DB {

    private static List<User> mUsers = new ArrayList<>();
    private static List<MyGroup> mGroups = new ArrayList<>();
    private static List<MyActivity> mActivities = new ArrayList<>();


    public static List<User> getmUsers() {
        return mUsers;
    }

    public static void setmUsers(List<User> mUsers) {
        DB.mUsers = mUsers;
    }

    public static List<MyGroup> getmGroups() {
        return mGroups;
    }

    public static void setmGroups(List<MyGroup> mGroups) {
        DB.mGroups = mGroups;
    }

    public static List<MyActivity> getmActivities() {
        return mActivities;
    }

    public static void setmActivities(List<MyActivity> mActivities) {
        DB.mActivities = mActivities;
    }

    public static void setUser(User u) {
        mUsers.add(u);
    }


    public static void setGroup(MyGroup u) {
        mGroups.add(u);
    }


    public static void setActivity(MyActivity u, String namegroup) {


        MyGroup group = null;
        for (MyGroup g : mGroups) {
            if (g.getName().equals(namegroup)) {
                group = g;
            }
        }
        mGroups.remove(group);
        group.setActivity(u);
        u.setGroup(group);
        mActivities.add(u);
        mGroups.add(group);


    }

    public static void replaceGroup(int index, MyGroup group) {

        MyGroup g = mGroups.get(index);
        mGroups.remove(g);
        mGroups.add(index, group);

    }

    public static List<User> getUsersofGroup(String name) {


        MyGroup group = null;
        for (MyGroup g : mGroups) {
            if (g.getName().equals(name)) {
                group = g;
            }
        }
        /*
        List<User> users = new ArrayList<>();
        for( User x : users_in_group){

            if(x.getGroups().contains(group)){
                users.add(x);
            }

        }*/


        return group.getUsers_in_group();


    }


    public static List<MyActivity> getActivityofGroup(String name) {

        MyGroup group = null;
        for (MyGroup g : mGroups) {
            if (g.getName().equals(name)) {
                group = g;
            }
        }
        /*
        List<MyActivity> activities = new ArrayList<>();
        for( MyActivity x : activity_in_group){

            if(x.getGroups().contains(group)){
                activities.add(x);
            }

        }
        */

        return group.getActivity_in_group();


    }


    public static void init() {


        String[] uids = {"1", "2", "3", "4", "5"};
        String[] unames = {"Roberto", "Pasquale", "Fausto", "Omar", "Marco"};
        int[] uimages = {R.drawable.profilecircle, R.drawable.profilecircle, R.drawable.profilecircle, R.drawable.profilecircle, R.drawable.profilecircle};
        double[] ubalances = {25.00, 20.00, 25.00, -4.00, -3.00};

        String[] gids = {"1", "2"};
        String[] gnames = {"G1", "G2"};
        int[] gimages = {R.drawable.profilecircle, R.drawable.profilecircle};
        double[] gbalances = {70.00, -7.00};

        String[] aids = {"1", "2", "3", "4"};
        String[] anames = {"Luce", "Gas", "Cena", "Pranzo"};
        int[] aimages = {R.drawable.profilecircle, R.drawable.profilecircle, R.drawable.profilecircle, R.drawable.profilecircle};
        double[] abalances = {100.00, 25.00, 12.00, 6.00};


        for (int i = 0; i < gnames.length; i++) {
            MyGroup u = new MyGroup(gids[i], gnames[i], gimages[i], gbalances[i]);

            mGroups.add(u);
        }


        for (int i = 0; i < unames.length; i++) {
            User u = new User(unames[i], uimages[i], ubalances[i]);

            if (i < 3) {//G1
                u.addGrouponUser(mGroups.get(0));

                MyGroup g = mGroups.get(0);
                mGroups.remove(g);
                g.addUserinGroup(u);
                mGroups.add(0, g);


            } else {//G2
                u.addGrouponUser(mGroups.get(1));

                MyGroup g = mGroups.get(1);
                mGroups.remove(g);
                g.addUserinGroup(u);
                mGroups.add(1, g);
            }

            mUsers.add(u);
        }


        for (int i = 0; i < anames.length; i++) {
            MyActivity u = new MyActivity(anames[i], aimages[i], abalances[i]);
            if (i < 2) {
                u.setGroup(mGroups.get(0));

                MyGroup g = mGroups.get(0);
                mGroups.remove(g);
                g.setActivity(u);
                mGroups.add(0, g);

            } else {
                u.setGroup(mGroups.get(1));

                MyGroup g = mGroups.get(1);
                mGroups.remove(g);
                g.setActivity(u);
                mGroups.add(1, g);
            }

            mActivities.add(u);
        }


    }









}

