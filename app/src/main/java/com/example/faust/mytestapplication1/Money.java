package com.example.faust.mytestapplication1;

/**
 * Created by faust on 20/04/2017.
 */

public class Money
{
    private Double mAmount;
    private CurrencyDetail mCurrencyDetail;


    public Money()
    {
        mCurrencyDetail = new CurrencyDetail();
    }

    public Money(double amount)
    {
        this();
        mAmount = amount;
    }


    public double getAmount() {
        return mAmount;
    }

    public void setAmount(Double amount) {
        mAmount = amount;
    }

    public String getCurrencySymbol()
    {
        return mCurrencyDetail.getSymbol();
    }

    /*public String getCurrencySymbol()
    {

    }

    public String getCurrencySymbol()
    {

    }*/

    public void setCurrencySymbol(String symbol)
    {
        mCurrencyDetail.setSymbol(symbol);
    }

    /*public void setCurrencySymbol()
    {

    }

    public void setCurrencySymbol()
    {

    }*/
}
