package models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nlazaris on 10/24/16.
 */

public class Moment implements Parcelable {
    private String key;
    private Integer id;
    private String title;
    private String description;
    private Integer reviewCount = 0;

    public Moment(){
        // Default constructor for Firebase
    }

    public Moment(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Moment(String key, String title, String description) {
        this.key = key;
        this.title = title;
        this.description = description;
    }

    public Moment(Integer id, String title, String description, Integer count) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.reviewCount = count;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public void incrementReviewCount() {
        this.reviewCount++;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(reviewCount);
    }

    private Moment(Parcel in) {
        key = in.readString();
        title = in.readString();
        description = in.readString();
        reviewCount = in.readInt();
    }

    public static final Creator<Moment> CREATOR = new Creator<Moment>() {
        @Override
        public Moment createFromParcel(Parcel in) {
            return new Moment(in);
        }

        @Override
        public Moment[] newArray(int size) {
            return new Moment[size];
        }
    };
}
