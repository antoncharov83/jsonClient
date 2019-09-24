package com.antoncharov;

public class Results {
    private int heart_rate;
    private int bp_syst;
    private int bp_diast;

    public Results(){};

    public Results(int heart_rate, int bp_syst, int bp_diast) {
        this.heart_rate = heart_rate;
        this.bp_syst = bp_syst;
        this.bp_diast = bp_diast;
    }

    public int getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(int heart_rate) {
        this.heart_rate = heart_rate;
    }

    public int getBp_syst() {
        return bp_syst;
    }

    public void setBp_syst(int bp_syst) {
        this.bp_syst = bp_syst;
    }

    public int getBp_diast() {
        return bp_diast;
    }

    public void setBp_diast(int bp_diast) {
        this.bp_diast = bp_diast;
    }

    @Override
    public String toString() {
        return "Results{" +
                "heart_rate=" + heart_rate +
                ", bp_syst=" + bp_syst +
                ", bp_diast=" + bp_diast +
                '}';
    }
}
