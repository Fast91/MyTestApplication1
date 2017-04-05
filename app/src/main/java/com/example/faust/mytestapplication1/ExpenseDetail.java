package com.example.faust.mytestapplication1;

import android.media.Image;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by faust on 05/04/2017.
 */

public class ExpenseDetail
{
    private UUID mId;
    private String mTitle;
    private Image mImage;
    private String mGroup;
    private String mDate;
    private String mAmount;
    private String mCurrency;
    private String mCategory;
    private String mDescription;

    public ExpenseDetail()
    {
        mId = UUID.randomUUID();
    }

    public ExpenseDetail(String title, String group)
    {
        this();

        setTitle(title);
        setGroup(group);
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Image getImage() {
        return mImage;
    }

    public void setImage(Image image) {
        mImage = image;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        mGroup = group;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
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

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
