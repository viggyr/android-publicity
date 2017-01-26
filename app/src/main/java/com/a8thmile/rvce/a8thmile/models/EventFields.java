package com.a8thmile.rvce.a8thmile.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vignesh on 23/1/17.
 */

public class EventFields implements Parcelable{

    private String id;
    private String name;
    private String date;
    private int type;
    private int price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public EventFields(String id, String name, String date, int type, int price) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.type = type;
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public EventFields(Parcel in) {
        id = in.readString();
        name = in.readString();
        date=in.readString();
        type=in.readInt();
        price=in.readInt();
    }

    public static final Parcelable.Creator<EventFields> CREATOR = new Parcelable.Creator<EventFields>() {
        public EventFields createFromParcel(Parcel in) {
            return new EventFields(in);
        }

        public EventFields[] newArray(int size) {
            return new EventFields[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(date);
        parcel.writeInt(type);
        parcel.writeInt(price);
    }
}
