package xyz.nlaz.albus;

/**
 * Created by Nick on 12/6/2016.
 */

public class Moments {

    private String Title;
    private String Description;
    private int ReviewCount;

    public Moments(String title, String description, int reviewCount){
        this.Title = title;
        this.Description = description;
        this.ReviewCount = reviewCount;
    }
    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public int getReviewCount(){
        return ReviewCount;
    }
    public Moments() {
    }
}
