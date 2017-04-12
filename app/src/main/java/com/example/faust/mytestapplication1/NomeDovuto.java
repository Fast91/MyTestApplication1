package com.example.faust.mytestapplication1;

/**
 * Created by robertospaziani on 03/04/17.
 */

public class NomeDovuto {
    public String Name;
    public Double Dovuto;
    private String Currency;
    public String Name_Group;
    public String Id_Group;


    public String Id;

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

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
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
}
