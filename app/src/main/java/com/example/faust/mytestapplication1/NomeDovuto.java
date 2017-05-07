package com.example.faust.mytestapplication1;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by robertospaziani on 03/04/17.
 */

public class NomeDovuto implements  Comparable{
    public String Name;
    public Double Dovuto;
    private String mCurrencySymbol;
    public String Name_Group;
    public String Id_Group;
    public String Category;
    public Date date;
    public String pagatoda;


    public String Id;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }



    public String getName_Group() {
        return Name_Group;
    }

    public void setName_Group(String name_Group) {
        Name_Group = name_Group;
    }

    public String getId_Group() {
        return Id_Group;
    }

    public void setId_Group(String id_Group) {
        Id_Group = id_Group;
    }



    public NomeDovuto(String name) {
        this.Name = name;
        
    }

    public NomeDovuto(String id, String name) {
        this.Name = name;
        this.Id = id;
    }

    public NomeDovuto(String name, Double dovuto) {
        this.Name = name;
        this.Dovuto = dovuto;
    }

    public String getId() {
        return Id;
    }

    public String getCurrencySymbol() {
        return mCurrencySymbol;
    }

    public void setCurrencySymbol(String currency) {
        mCurrencySymbol = currency;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public Double getDovuto() {
        return Dovuto;
    }

    public void setDovuto(Double dovuto) {
        this.Dovuto = dovuto;
    }

        @Override
    public String toString(){
        return Name;
    }



    @Override
    public int compareTo(@NonNull Object o) {
        //0 uguale
        //1 o + grande

        if(((NomeDovuto)o).getDate()!=null){

            if (((NomeDovuto) o).getDate().compareTo(date) > 0) {
                return 1;
            }
            if (((NomeDovuto) o).getDate().compareTo(date) < 0) {
                return -1;
            }

        }

        return 0;
    }

    public void setPagatoDa(String a){
        pagatoda=a;
    }


    public String pagatoda() {
        return pagatoda;
    }
}
