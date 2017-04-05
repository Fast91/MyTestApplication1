package com.example.faust.mytestapplication1;


import android.icu.util.Currency;
import android.os.Build;

import java.util.Locale;
import java.util.UUID;

/**
 * Created by faust on 05/04/2017.
 */

public class DetailEntry {
    private UUID mId;
    private String mUserName;
    private String mAmount;
    private String mCurrency;

    public DetailEntry()
    {
        //TODO prendere dal db il nome dell'utente e i soldi che deve

        mId = UUID.randomUUID();
        mAmount = "0";
        mCurrency = "€";
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

    public String getCurrency() {
        return mCurrency;
    }
    public void setCurrency(String currency) {
        mCurrency = currency;
    }
}