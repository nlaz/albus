package xyz.nlaz.albus;

/**
 * Created by nlazaris on 10/24/16.
 */

public class Moment {
    private String title;
    private String description;

    public Moment(String title, String description) {
        this.title = title;
        this.description = description;
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
}
