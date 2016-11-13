package models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by nlazaris on 10/30/16.
 */

public class Collection implements Parcelable{

   private String name;
   private ArrayList<Moment> moments;

    public Collection() {
        //Default constructor required by Firebase
        moments = new ArrayList<Moment>();
    }

   Collection(String name) {
        this.name = name;
        this.moments = new ArrayList<Moment>();
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public ArrayList<Moment> getMoments() {
      return moments;
   }

   public void setMoments(ArrayList<Moment> moments) {
      this.moments = moments;
   }

   public void addMoment(Moment moment) {
      this.moments.add(moment);
   }

   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(name);
      dest.writeList(moments);
   }

   private Collection(Parcel in) {
      name = in.readString();
      moments = in.readArrayList(Moment.class.getClassLoader());
   }

   public static final Creator<Collection> CREATOR = new Creator<Collection>() {
       @Override
       public Collection createFromParcel(Parcel in) {
           return new Collection(in);
       }

       @Override
       public Collection[] newArray(int size) {
           return new Collection[size];
       }
   };
}
