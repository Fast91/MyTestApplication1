package com.example.faust.mytestapplication1;

/**
 * Created by robertospaziani on 03/04/17.
 */

public class NomeDovuto {
    public String Name;
    public Double Dovuto;

    public NomeDovuto(String name, Double dovuto) {
        this.Name = name;
        this.Dovuto = dovuto;
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
}
