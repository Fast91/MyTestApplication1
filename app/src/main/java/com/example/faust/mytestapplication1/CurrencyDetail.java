package com.example.faust.mytestapplication1;

/**
 * Created by faust on 20/04/2017.
 */

public class CurrencyDetail
{
    private String mSymbol;
    private String mShortSymbol;
    //private String mName;

    public CurrencyDetail()
    {
        this("EUR");
    }

    public CurrencyDetail(String symbol)
    {
        mSymbol = symbol;
        mShortSymbol = CurrencyEditor.getShortSymbolFromSymbol(symbol);
    }

    public String getSymbol() {
        return mSymbol;
    }

    public void setSymbol(String symbol) {
        mSymbol = symbol;
    }

    public String getShortSymbol() {
        return mShortSymbol;
    }

    public void setShortSymbol(String shortSymbol) {
        mShortSymbol = shortSymbol;
    }

    /*public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }*/

    @Override
    public String toString() {
        return getShortSymbol();
    }
}
