package com.example.hp.directorymanagement.management.model;

/**
 * Created by hp on 10/18/2017.
 */

public class Person {
    private String mId;
    private String mName;
    private String mNumber;
    private boolean mIsLongClick=false;
    private boolean mIsChecked=false;

    public Person(String id, String name, String number) {
        mId=id;
        mName = name;
        mNumber = number;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmNumber() {
        return mNumber;
    }

    public void setmNumber(String mNumber) {
        this.mNumber = mNumber;
    }

    public boolean ismIsLongClick() {
        return mIsLongClick;
    }

    public void setmIsLongClick(boolean mIsLongClick) {
        this.mIsLongClick = mIsLongClick;
    }

    public boolean ismIsChecked() {
        return mIsChecked;
    }

    public void setmIsChecked(boolean mIsChecked) {
        this.mIsChecked = mIsChecked;
    }
}
