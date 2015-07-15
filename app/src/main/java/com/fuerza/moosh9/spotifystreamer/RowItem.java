package com.fuerza.moosh9.spotifystreamer;

/**
 * Created by Moosh9 on 7/13/2015.
 * Code copied from http://theopentutorials.com/tutorials/android/listview/android-custom-listview-with-image-and-text-using-arrayadapter/
 */
public class RowItem {
//just a container for the information to go in a custom ListView
    private String imageURL;
    private String desc2;
    private String desc;

    public RowItem(String desc, String desc2, String imageURL) {
        this.imageURL = imageURL;

        if (!desc2.equals("")){
            this.desc2 = desc2;
        }

        this.desc = desc;
    }

    public RowItem(String desc, String desc2) {
        this.desc2 = desc2;
        this.desc = desc;
    }

    public RowItem(String desc){
        this.desc= desc;
    }

    public String getImage() {


        return imageURL;


    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getDesc2() {
        return desc2;
    }
    public void setDesc2(String title) {
        this.desc2 = title;
    }
    @Override
    public String toString() {
        return desc2 + "\n" + desc;
    }

}
