package com.itsolution.electronichealthrecord;



public class model_for_other_reports {


    String name,profile_img;


    public model_for_other_reports() {

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public model_for_other_reports(String name,String profile_img) {
        this.name = name;
        this.profile_img=profile_img;
    }





}
