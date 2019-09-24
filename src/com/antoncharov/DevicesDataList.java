package com.antoncharov;

import java.util.List;

public class DevicesDataList {
    List<DeviceData> list;
    String endl = System.getProperty("line.separator");

    public DevicesDataList(List<DeviceData> list) {
        this.list = list;
    }

    public List<DeviceData> getList() {
        return list;
    }

    public void setList(List<DeviceData> list) {
        this.list = list;
    }

    public int getIdByName(String name){
        int id = -1;
        for(DeviceData data : list)
            if(data.getName().equalsIgnoreCase(name) && data.isAvailable() && data.isIs_active())
                id = data.getId();
        return id;
    }

    @Override
    public String toString() {
        String result = "";

        for(DeviceData data : list)
            result += data + endl;

        return "DevicesDataList{" +
                "list=" + endl + list +
                '}';
    }
}
