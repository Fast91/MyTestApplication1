package com.example.faust.mytestapplication1;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by faust on 05/04/2017.
 */

public class DetailLab
{
    private static DetailLab sDetailLab;

    private List<Detail> mDetails;

    public static DetailLab get(Context context)
    {
        if(sDetailLab==null)
        {
            sDetailLab = new DetailLab(context);
        }
        return sDetailLab;
    }

    private DetailLab(Context context)
    {
        // TO-DO
        // qui prendo i dettagli dal db

        mDetails = new ArrayList<>();
        Detail detail = new Detail();
        detail.setUserName("Barbara D'Urso");
        detail.setAmount("-100€");
        //aggiungo altri dettagli
        mDetails.add(detail);
        detail = new Detail();
        detail.setUserName("Simona Ventura");
        //aggiungo altri dettagli
        detail.setAmount("100€");
        mDetails.add(detail);
    }

    public List<Detail> getDetails()
    {
        return mDetails;
    }

    public Detail getDetail(UUID id)
    {
        for(Detail detail : mDetails)
        {
            if(detail.getId().equals(id))
            {
                return detail;
            }
        }
        return null;
    }
}
