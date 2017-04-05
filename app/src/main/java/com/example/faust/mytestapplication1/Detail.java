package com.example.faust.mytestapplication1;

import java.util.UUID;

/**
 * Created by faust on 05/04/2017.
 */

public class Detail {
    private UUID mId;
    private String mUserName;
    private String mAmount;

    public Detail()
    {
        //TO-DO
        //prendere dal db il nome dell'utente e i soldi che deve

        mId = UUID.randomUUID();
        mAmount = "100€";
    }

    public UUID getId() {
        return mId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getAmount() {
        return mAmount;
    }

    public void setAmount(String amount) {
        mAmount = amount;
    }
}