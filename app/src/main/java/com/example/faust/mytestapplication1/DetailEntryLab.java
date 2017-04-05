package com.example.faust.mytestapplication1;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by faust on 05/04/2017.
 */

public class DetailEntryLab
{
    //private static DetailEntryLab sDetailEntryLab;

    private List<DetailEntry> mDetailEntries;

    /*public static DetailEntryLab get(Context context)
    {
        if(sDetailEntryLab ==null)
        {
            sDetailEntryLab = new DetailEntryLab(context);
        }
        return sDetailEntryLab;
    }*/

    //private DetailEntryLab(Context context)
    public DetailEntryLab()
    {
        // TO-DO
        // qui prendo i dettagli dal db

        mDetailEntries = new ArrayList<>();
        DetailEntry detailEntry = new DetailEntry();
        detailEntry.setUserName("Barbara D'Urso");
        detailEntry.setAmount("-100");
        detailEntry.setCurrency("€");
        //aggiungo altri dettagli
        mDetailEntries.add(detailEntry);
        detailEntry = new DetailEntry();
        detailEntry.setUserName("Simona Ventura");
        //aggiungo altri dettagli
        detailEntry.setAmount("100");
        detailEntry.setCurrency("€");
        mDetailEntries.add(detailEntry);
    }

    public List<DetailEntry> getDetailEntries()
    {
        return mDetailEntries;
    }

    public DetailEntry getDetailEntry(UUID id)
    {
        for(DetailEntry detailEntry : mDetailEntries)
        {
            if(detailEntry.getId().equals(id))
            {
                return detailEntry;
            }
        }
        return null;
    }
}
