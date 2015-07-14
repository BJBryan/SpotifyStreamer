package com.fuerza.moosh9.spotifystreamer;

/**
 * Created by Moosh9 on 7/13/2015.
 * Code copied from http://theopentutorials.com/tutorials/android/listview/android-custom-listview-with-image-and-text-using-arrayadapter/
 */
public class RowItem {
//just a container for the information to go in a custom ListView
    private int imageId;
    private String title;
    private String desc;

    public RowItem(int imageId, String title, String desc) {
        this.imageId = imageId;
        this.title = title;
        this.desc = desc;
    }

    public RowItem(String desc){
        this.desc= desc;
    }

    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return title + "\n" + desc;
    }

}
