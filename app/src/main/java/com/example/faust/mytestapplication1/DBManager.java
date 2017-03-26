package com.example.faust.mytestapplication1;

import android.content.res.Resources;
import android.util.Log;

/*
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBManager
{
    private static class MyDB //qui ci sarà il vero db, per ora solo roba ammucchiata a cazzo
    {
        private static class UserSamples
        {
            private static String[] ids = {"1", "2", "3", "4", "5"};
            private static String[] names= {"Roberto", "Pasquale", "Fausto", "Omar", "Marco"};
            private static int[] images= {R.drawable.profilecircle,R.drawable.profilecircle,R.drawable.profilecircle,R.drawable.profilecircle,R.drawable.profilecircle};
            private static double[] balances = {25.00,20.00,25.00,-4.00,-3.00};
        }
        private static class GroupSamples
        {
            private static String[] ids = {"1", "2"};
            private static String[] names = {"G1", "G2"};
            private static int[] images = {R.drawable.profilecircle, R.drawable.profilecircle};
            private static double[] balances = {70.00, -7.00};
        }
        private static class ActivitySamples
        {
            private static String[] ids = {"1", "2", "3", "4"};
            private static String[] names = {"Luce", "Gas" , "Cena", "Pranzo"};
            private static int[] images = {R.drawable.profilecircle, R.drawable.profilecircle, R.drawable.profilecircle, R.drawable.profilecircle};
            private static double[] balances = {100.00, 25.00 , 12.00, 6.00};
        }
        private static List<User> mUsers = new ArrayList<>();
        private static List<MyGroup> mGroups = new ArrayList<>();
        private static List<MyActivity> mActivities = new ArrayList<>();

        private static void initUsersExample()
        {
            for(int i=0;i<ActivitySamples.names.length;i++){
                User u=new User(ActivitySamples.names[i],ActivitySamples.images[i],ActivitySamples.balances[i]);
                if(i==0 || i==1 || i==2){
                    u.addGrouponUser(new MyGroup("G1"));
                }
                else{
                    u.addGrouponUser(new MyGroup("G2"));
                }
                mUsers.add(u);
            }
        }
        private static void initGroupsExample()
        {
            for (int i = 0; i < GroupSamples.names.length; i++) {
                MyGroup u = new MyGroup(GroupSamples.names[i], GroupSamples.images[i], GroupSamples.balances[i]);
                u.setIdgroup(i+1);
                if(i==0){
                    u.addUserinGroup(new User ("Roberto",R.drawable.profilecircle,25.00));
                    u.addUserinGroup(new User ("Pasquale",R.drawable.profilecircle,20.00));
                    u.addUserinGroup(new User ("Fausto",R.drawable.profilecircle,25.00));
                    MyActivity a1 = new MyActivity("Luce",R.drawable.energia,100.00);
                    a1.setOwner(new User("Possessore App",R.drawable.realphoto));
                    u.addActivityinGroup(a1);
                    MyActivity a2 = new MyActivity("Gas",R.drawable.logogas,25.00);
                    a2.setOwner(new User("Pasquale",R.drawable.profilecircle));
                    u.addActivityinGroup(a2);
                }
                else{
                    u.addUserinGroup(new User ("Omar",R.drawable.profilecircle,-4.00));
                    u.addUserinGroup(new User ("Marco",R.drawable.profilecircle,-3.00));
                    MyActivity a1 = new MyActivity("Cena",R.drawable.cibo,12.00);
                    a1.setOwner(new User("Omar",R.drawable.profilecircle));
                    u.addActivityinGroup(a1);
                    MyActivity a2 = new MyActivity("Pranzo",R.drawable.cibo,6.00);
                    a2.setOwner(new User("Marco",R.drawable.profilecircle));
                    u.addActivityinGroup(a2);
                }
                mGroups.add(u);
            }
        }
        private static void initActivityExample()
        {
            for (int i = 0; i < ActivitySamples.names.length; i++) {
                MyActivity u = new MyActivity(ActivitySamples.names[i], ActivitySamples.images[i], ActivitySamples.balances[i]);
                if(i==0){//Luce

                }
                else if(i==1){

                }
                else if(i==2){

                }
                else{//Pranzo

                }

                mActivities.add(u);
            }
        }
        private static List<User> getUsers()
        {
            return mUsers; // in realtà dovrebbe chiedere al db e aggiornare la lista locale
        }
        private static List<MyGroup> getGroups()
        {
            return mGroups; // in realtà dovrebbe chiedere al db e aggiornare la lista locale
        }
        private static List<MyActivity> getActivities()
        {
            return mActivities; // in realtà dovrebbe chiedere al db e aggiornare la lista locale
        }
        private static boolean addUser(User user)
        {
            return mUsers.add(user); // in realtà dovrebbe chiedere al db e aggiornare la lista locale
        }
        private static boolean removeUser(User user)
        {
            return mUsers.remove(user); // in realtà dovrebbe chiedere al db e aggiornare la lista locale
        }
        private static boolean addGroup(MyGroup group)
        {
            return mGroups.add(group); // in realtà dovrebbe chiedere al db e aggiornare la lista locale
        }
        private static boolean removeGroup(MyGroup group)
        {
            return mGroups.remove(group); // in realtà dovrebbe chiedere al db e aggiornare la lista locale
        }
        private static boolean addActivity(MyActivity activity)
        {
            return mActivities.add(activity); // in realtà dovrebbe chiedere al db e aggiornare la lista locale
        }
        private static boolean removeActivity(MyActivity activity)
        {
            return mActivities.remove(activity); // in realtà dovrebbe chiedere al db e aggiornare la lista locale
        }
        public static void updateUser(User oldUser, User newUser)
        {
            mUsers.remove(oldUser);
            mUsers.add(newUser);
        }
        public static void updateGroup(MyGroup oldGroup, MyGroup newGroup)
        {
            mGroups.remove(oldGroup);
            mGroups.add(newGroup);
        }
        public static void updateActivity(MyActivity oldActivity, MyActivity newActivity)
        {
            mActivities.remove(oldActivity);
            mActivities.add(newActivity);
        }
        public static void updateUser(User newUser)
        {
            for (User u:mUsers)
            {
                //if(u.getID()==newUser.getID())
                if(u.getID().equals(newUser.getID())) // se l'ID è una stringa
                {
                    u=newUser;
                    break;
                }
            }
        }
        public static void updateGroup(MyGroup newGroup)
        {
            for (MyGroup g:mGroups)
            {
                //if(g.getID()==newGroup.getID())
                if(g.getID().equals(newGroup.getID())) // se l'ID è una stringa
                {
                    g = newGroup;
                    break;
                }
            }
        }
        public static void updateActivity(MyActivity newActivity)
        {
            for (MyActivity a:mActivities)
            {
                //if(a.getID()==newActivity.getID())
                if(a.getID().equals(newActivity.getID())) // se l'ID è una stringa
                {
                    a=newActivity;
                    break;
                }
            }
        }
    }



    public static void init()
    {
        MyDB.initUsersExample();
        MyDB.initGroupsExample();
        MyDB.initActivityExample();
    }

    // I METODI QUI SOTTO SONO FATTI PER NON ESSERE MODIFICATI DOPO L'AGGIUNTA DEL VERO DB

    public static List<User> getUsers() throws Exception // sostituire con le exception adatte
    {
        //DBManager dbm = new DBManager();
        List<User> res = new ArrayList<>();
        if(true)
        {
            res = MyDB.getUsers();
            return Collections.unmodifiableList(res);
        }
        else
        {
            throw new Exception(); // sostituire con le exception adatte
        }
    }

    public static boolean addUser(User user) throws Exception
    {
        return MyDB.addUser(user);
    }

    public static boolean removeUser(User user) throws Exception
    {
        return MyDB.removeUser(user);
    }

    public static List<MyGroup> getGroups() throws Exception // sostituire con le exception adatte
    {
        //DBManager db = new DBManager();
        List<MyGroup> res = new ArrayList<>();
        if(true)
        {
            res = MyDB.getGroups();
            return Collections.unmodifiableList(res);
        }
        else
        {
            throw new Exception(); // sostituire con le exception adatte
        }
    }

    public static boolean addGroup(MyGroup group) throws Exception
    {
        return MyDB.addGroup(group);
    }

    public static boolean removeGroup(MyGroup group) throws Exception
    {
        return MyDB.removeGroup(group);
    }

    public static List<MyActivity> getActivities() throws Exception // sostituire con le exception adatte
    {
        //DBManager db = new DBManager();
        List<MyActivity> res = new ArrayList<>();
        if(true)
        {
            res = MyDB.getActivities();
            return Collections.unmodifiableList(res);
        }
        else
        {
            throw new Exception(); // sostituire con le exception adatte
        }
    }

    public static boolean addActivity(MyActivity activity) throws Exception
    {
        return MyDB.addActivity(activity);
    }

    public static boolean removeActivity(MyActivity activity) throws Exception
    {
        return MyDB.removeActivity(activity);
    }

    // METODO NON USATO
    public static void updateUser(User oldUser, User newUser)
    {
        MyDB.updateUser(oldUser, newUser);
    }

    public static void updateUser(User newUser)
    {
        MyDB.updateUser(newUser);
    }

    // METODO NON USATO
    public static void updateGroup(MyGroup oldGroup, MyGroup newGroup)
    {
        MyDB.updateGroup(oldGroup, newGroup);
    }

    public static void updateGroup(MyGroup newGroup)
    {
        MyDB.updateGroup(newGroup);
    }

    // METODO NON USATO
    public static void updateActivity(MyActivity oldActivity, MyActivity newActivity)
    {
        MyDB.updateActivity(oldActivity, newActivity);
    }

    public static void updateActivity(MyActivity newActivity)
    {
        MyDB.updateActivity(newActivity);
    }
}
