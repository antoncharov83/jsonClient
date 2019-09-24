package com.antoncharov;

public class DeviceData {
    private boolean available;
    private String created_date;
    private int id;
    private boolean is_active;
    private String modified_by;
    private String modified_date;
    private String name;
    private int par_unid;
    private int unid;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPar_unid() {
        return par_unid;
    }

    public void setPar_unid(int par_unid) {
        this.par_unid = par_unid;
    }

    public int getUnid() {
        return unid;
    }

    public void setUnid(int unid) {
        this.unid = unid;
    }

    @Override
    public String toString() {
        return "DeviceData{" +
                "available=" + available +
                ", created_date='" + created_date + '\'' +
                ", id=" + id +
                ", is_active=" + is_active +
                ", modified_by='" + modified_by + '\'' +
                ", modified_date='" + modified_date + '\'' +
                ", name='" + name + '\'' +
                ", par_unid=" + par_unid +
                ", unid=" + unid +
                '}';
    }
}
