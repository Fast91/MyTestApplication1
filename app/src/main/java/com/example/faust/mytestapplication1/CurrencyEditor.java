package com.example.faust.mytestapplication1;

import android.icu.util.Currency;
import android.icu.util.ULocale;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import yahoofinance.YahooFinance;
import yahoofinance.quotes.fx.FxQuote;

/**
 * Created by faust on 16/04/2017.
 */

public class CurrencyEditor
{
    private enum CurrencyEnum {
        AED("AED"), AFN("AFN"), ALL("ALL"), AMD("AMD"), ANG("ANG"), AOA("AOA"), ARS("ARS"), AUD("AUD"), AWG("AWG"), AZN("AZN"), BAM("BAM"), BBD("BBD"),
        BDT("BDT"), BGN("BGN"), BHD("BHD"), BIF("BIF"), BMD("BMD"), BND("BND"), BOB("BOB"), BOV("BOV"), BRL("BRL"), BSD("BSD"), BTN("BTN"), BWP("BWP"),
        BYN("BYN"), BYR("BYR"), BZD("BZD"), CAD("CAD"), CDF("CDF"), CHE("CHE"), CHF("CHF"), CHW("CHW"), CLF("CLF"), CLP("CLP"), CNY("CNY"), COP("COP"),
        COU("COU"), CRC("CRC"), CUC("CUC"), CUP("CUP"), CVE("CVE"), CZK("CZK"), DJF("DJF"), DKK("DKK"), DOP("DOP"), DZD("DZD"), EGP("EGP"), ERN("ERN"),
        ETB("ETB"), EUR("EUR"), FJD("FJD"), FKP("FKP"), GBP("GBP"), GEL("GEL"), GHS("GHS"), GIP("GIP"), GMD("GMD"), GNF("GNF"), GTQ("GTQ"), GYD("GYD"),
        HKD("HKD"), HNL("HNL"), HRK("HRK"), HTG("HTG"), HUF("HUF"), IDR("IDR"), ILS("ILS"), INR("INR"), IQD("IQD"), IRR("IRR"), ISK("ISK"), JMD("JMD"),
        JOD("JOD"), JPY("JPY"), KES("KES"), KGS("KGS"), KHR("KHR"), KMF("KMF"), KPW("KPW"), KRW("KRW"), KWD("KWD"), KYD("KYD"), KZT("KZT"), LAK("LAK"),
        LBP("LBP"), LKR("LKR"), LRD("LRD"), LSL("LSL"), LYD("LYD"), MAD("MAD"), MDL("MDL"), MGA("MGA"), MKD("MKD"), MMK("MMK"), MNT("MNT"), MOP("MOP"),
        MRO("MRO"), MUR("MUR"), MVR("MVR"), MWK("MWK"), MXN("MXN"), MXV("MXV"), MYR("MYR"), MZN("MZN"), NAD("NAD"), NGN("NGN"), NIO("NIO"), NOK("NOK"),
        NPR("NPR"), NZD("NZD"), OMR("OMR"), PAB("PAB"), PEN("PEN"), PGK("PGK"), PHP("PHP"), PKR("PKR"), PLN("PLN"), PYG("PYG"), QAR("QAR"), RON("RON"),
        RSD("RSD"), RUB("RUB"), RWF("RWF"), SAR("SAR"), SBD("SBD"), SCR("SCR"), SDG("SDG"), SEK("SEK"), SGD("SGD"), SHP("SHP"), SLL("SLL"), SOS("SOS"),
        SRD("SRD"), SSP("SSP"), STD("STD"), SYP("SYP"), SZL("SZL"), THB("THB"), TJS("TJS"), TMT("TMT"), TND("TND"), TOP("TOP"), TRY("TRY"), TTD("TTD"),
        TWD("TWD"), TZS("TZS"), UAH("UAH"), UGX("UGX"), USD("USD"), USN("USN"), UYI("UYI"), UYU("UYU"), UZS("UZS"), VEF("VEF"), VND("VND"), VUV("VUV"),
        WST("WST"), XAF("XAF"), XAG("XAG"), XAU("XAU"), XBA("XBA"), XBB("XBB"), XBC("XBC"), XBD("XBD"), XCD("XCD"), XDR("XDR"), XFU("XFU"), XOF("XOF"),
        XPD("XPD"), XPF("XPF"), XPT("XPT"), XSU("XSU"), XTS("XTS"), XUA("XUA"), XXX("XXX"), YER("YER"), ZAR("ZAR"), ZMW("ZMW");
        private String iso4217Code = "";

        CurrencyEnum(String code) {
            this.iso4217Code = code;
        }

        @Override
        public String toString() {
            return iso4217Code;
        }
    }

    /*
     * Returns the default number of fraction digits that should be displayed for the default currency for given locale.
     * @param locale
     * @return
     */
    /*@RequiresApi(api = Build.VERSION_CODES.N)
    private static int getLocalDefaultFractionDigits(ULocale locale){
        if (locale == null) {
            locale=ULocale.getDefault();
        }
        android.icu.util.Currency currency=android.icu.util.Currency.getInstance(locale);
        if (currency != null) {
            return currency.getDefaultFractionDigits();
        }
        return 2;
    }*/

    /*
     * Returns the default number of fraction digits that should be displayed for the default currency for given locale.
     * @param locale
     * @return
     */
    /*@Deprecated
    private static int getLocalDefaultFractionDigits(Locale locale){
        if (locale == null) {
            locale=Locale.getDefault();
        }
        java.util.Currency currency=java.util.Currency.getInstance(locale);
        if (currency != null) {
            return currency.getDefaultFractionDigits();
        }
        return 2;
    }*/

    public static String getShortSymbolFromSymbol(String symbol, String defaultSimbol)
    {
        String temp = getShortSymbolFromSymbol(symbol);
        if(temp!=null)
        {
            return temp;
        }
        return defaultSimbol;
    }

    public static String getShortSymbolFromSymbol(String symbol)
    {
        String shortSymbol = symbol;
        if (symbol != null) {
            if (symbol.equals("EUR")) {
                shortSymbol="€";
            }
            else if (symbol.equals("GBP")) {
                shortSymbol="£";
            }
            else if (symbol.equals("XXX")) {
                shortSymbol="¤";
            }
            else if (symbol.equals("USD")) {
                shortSymbol="$";
            }
            return shortSymbol;
        }
        return shortSymbol;
    }

    /*
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getLocalCurrencyShortSymbol(ULocale locale){
        android.icu.text.NumberFormat format=android.icu.text.NumberFormat.getCurrencyInstance(locale);
        android.icu.util.Currency currency=format.getCurrency();
        if (currency != null) {
            String symbol=currency.getSymbol(locale);
            if (symbol.equals("EUR")) {
                symbol="€";
            }
            else if (symbol.equals("GBP")) {
                symbol="£";
            }
            else if (symbol.equals("XXX")) {
                symbol="¤";
            }
            else if (symbol.equals("USD")) {
                symbol="$";
            }
            else if (symbol == null) {
                symbol="€";
            }
            return symbol;
        }
        return "€";
    }

    @Deprecated
    public static String getLocalCurrencyShortSymbol(Locale locale){
        java.text.NumberFormat format=java.text.NumberFormat.getCurrencyInstance(locale);
        java.util.Currency currency=format.getCurrency();
        if (currency != null) {
            String symbol=currency.getSymbol(locale);
            if (symbol.equals("EUR")) {
                symbol="€";
            }
            else if (symbol.equals("GBP")) {
                symbol="£";
            }
            else if (symbol.equals("XXX")) {
                symbol="¤";
            }
            else if (symbol.equals("USD")) {
                symbol="$";
            }
            else if (symbol == null) {
                symbol="€";
            }
            return symbol;
        }
        return "€";
    }*/

    /*
     * Creates the currency converter from the source currency to the target currency.
     * @param source the source currency.
     * @param target the target currency.
     * @param factor the multiplier factor from source to target.
     * @return the corresponding converter.
     */
    /*@SuppressWarnings("unchecked") public CurrencyConverter(CurrencyUnit<?> source,Unit<IMoney> target,Number factor){
        if (target instanceof CurrencyUnit<?>) {
            rate=new ExchangxeRate(source,(CurrencyUnit<IMoney>)target,factor);
        }
        else {
            Currency defCurrency=Currency.getInstance(ULocale.getDefault());
            rate=new ExchangeRate(defCurrency,defCurrency,factor);
        }
    }*/

    public static double convertCurrency(Double amount, String sourceSymbol, String destSymbol) throws IOException
    {
        double rate = getRate(sourceSymbol, destSymbol);
        return amount * rate;
    }

    private static double getRate(String fromSymbol, String toSymbol) throws IOException
    {
        // symbols examples: EUR, USD...
        /*URL url = new URL("http://finance.yahoo.com/d/quotes.csv?f=l1&s=" + fromSymbol + toSymbol + "=X");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line = reader.readLine();
        if(line.length() > 0)
        {
            double result;
            result = Double.parseDouble(line);
            reader.close();
            return result;
        }
        reader.close();
        return 0;*/
        // proviamo con quest'altro...
        /*FxQuote usdgbp = YahooFinance.getFx(fromSymbol+toSymbol+"=X");
        return usdgbp.getPrice().doubleValue();*/
        // NON FUNZIONA STA MERDA
        // fanculo uso const per ora...
        if(fromSymbol.equals("USD") && toSymbol.equals("EUR"))
        {
            return 0.929749;
        }
        else if(fromSymbol.equals("EUR") && toSymbol.equals("USD"))
        {
            return 1.07554;
        }
        return 1; // ritornavi 2 non so perchè e non andava
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Set<android.icu.util.Currency> getCurrencies()
    {
        Set<android.icu.util.Currency> toret = new HashSet<android.icu.util.Currency>();
        Locale[] locs = Locale.getAvailableLocales();

        for(Locale loc : locs) {
            try {
                android.icu.util.Currency currency = android.icu.util.Currency.getInstance( loc );

                if ( currency != null ) {
                    toret.add( currency );
                }
            } catch(Exception exc)
            {
                // Locale not found
            }
        }

        return toret;


        //return android.icu.util.Currency.getAvailableCurrencies();
    }

    @Deprecated
    public static Set<java.util.Currency> getJavaCurrencies()
    {
        Set<java.util.Currency> toret = new HashSet<java.util.Currency>();
        Locale[] locs = Locale.getAvailableLocales();

        for(Locale loc : locs) {
            try {
                java.util.Currency currency = java.util.Currency.getInstance( loc );

                if ( currency != null ) {
                    toret.add( currency );
                }
            } catch(Exception exc)
            {
                // Locale not found
            }
        }

        return toret;



        //return java.util.Currency.getAvailableCurrencies();
    }


    private static List<String> getCurrenciesEnums()
    {
        List<String> list = new ArrayList<>();
        for (CurrencyEnum cEn : CurrencyEnum.values())
        {
            CurrencyDetail detail = new CurrencyDetail();
            list.add(cEn.toString());
        }
        return list;
    }

    private static List<CurrencyDetail> createCurrencyDetails()
    {
        List<CurrencyDetail> detailList = new ArrayList<>();
        List<String> stringList = getCurrenciesEnums();
        for(String str : stringList)
        {
            detailList.add(new CurrencyDetail(str));
        }
        return detailList;
    }


    @Nullable
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static android.icu.util.Currency getCurrencyBySymbol(String simbol)
    {
        Set<android.icu.util.Currency> set = getCurrencies();
        for (android.icu.util.Currency curr:set)
        {
            if(curr.getSymbol().equals(simbol))
            {
                return curr;
            }
        }
        return null;
    }

    @Nullable
    @Deprecated
    public static java.util.Currency getJavaCurrencyBySymbol(String simbol)
    {
        Set<java.util.Currency> set = getJavaCurrencies();
        for (java.util.Currency curr:set)
        {
            if(curr.getSymbol().equals(simbol))
            {
                return curr;
            }
        }
        return null;
    }


    /*public static List<String> getCurrencyNames()
    {
        List<String> list = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            Set<android.icu.util.Currency> set = getCurrencies();
            for (android.icu.util.Currency curr : set)
            {
                list.add(curr.getDisplayName());
            }
        }
        else
        {
            Set<java.util.Currency> set = getJavaCurrencies();
            for (java.util.Currency curr : set)
            {
                list.add(curr.getDisplayName());
            }
        }
        return list;
    }*/

    public static List<String> getCurrencySymbols()
    {
        /*List<String> list = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            Set<android.icu.util.Currency> set = getCurrencies();
            for (android.icu.util.Currency curr : set)
            {
                list.add(curr.getSymbol());
            }
        }
        else
        {
            Set<java.util.Currency> set = getJavaCurrencies();
            for (java.util.Currency curr : set)
            {
                list.add(curr.getSymbol());
            }
        }
        return list;*/
        return getCurrenciesEnums();
    }

    public static List<CurrencyDetail> getCurrencyDetails()
    {
        return createCurrencyDetails();
    }
}
