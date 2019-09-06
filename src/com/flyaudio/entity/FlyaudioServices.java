package com.flyaudio.entity;

/**
 * Created by yaoyuqing
 * on 17-6-13.
 */

public class FlyaudioServices {

    private String package_name;
    private String class_name;
    private String aciton_name;
    private boolean acc_start;

    public FlyaudioServices() {
    }

    public FlyaudioServices(String package_name, String class_name, String aciton_name, boolean acc_start) {
        this.package_name = package_name;
        this.class_name = class_name;
        this.aciton_name = aciton_name;
        this.acc_start = acc_start;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getAciton_name() {
        return aciton_name;
    }

    public void setAciton_name(String aciton_name) {
        this.aciton_name = aciton_name;
    }

    public boolean isAcc_start() {
        return acc_start;
    }

    public void setAcc_start(boolean acc_start) {
        this.acc_start = acc_start;
    }

    @Override
    public String toString() {
        return "FlyaudioServices{" +
                "package_name='" + package_name + '\'' +
                ", class_name='" + class_name + '\'' +
                ", aciton_name='" + aciton_name + '\'' +
                ", acc_start=" + acc_start +
                '}';
    }
}
