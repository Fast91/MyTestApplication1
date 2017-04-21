package com.example.faust.mytestapplication1;

import java.util.ArrayList;

/**
 * Created by mauriziospaziani on 21/04/17.
 */

 public class Categories {

    public ArrayList<String> lista = new ArrayList<>();

    public Categories (){

/*
        lista.add("Luce");
        lista.add("Gas");
        lista.add("Internet");
        lista.add("Regalo");
        lista.add("Alimentari");
        lista.add("Generale");
        */



    }

    public void setItem(String x){
        lista.add(x);
    }



    public  ArrayList<String> getList(){

        return lista;
    }

}
