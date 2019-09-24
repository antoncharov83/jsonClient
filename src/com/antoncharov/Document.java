package com.antoncharov;

import java.util.List;

public class Document {
    private String name;
    private String date;
    private String description;
    private String conclusion;
    private String file;
    private String file_md5;
    private List<Device> devices;
    private List<Results> results;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFile_md5() {
        return file_md5;
    }

    public void setFile_md5(String file_md5) {
        this.file_md5 = file_md5;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        final String endl = System.getProperty("line.separator");

        String result = "Document{" + endl +
                "name='" + name + '\'' + endl +
                ", date='" + date + '\'' + endl +
                ", description='" + description + '\'' + endl +
                ", conclusion='" + conclusion + '\'' + endl +
                ", file='" + file + '\'' + endl +
                ", file_md5='" + file_md5 + '\'' + endl;
        if(devices != null)
            for(Device device : devices)
                result += device + endl;

        if(results != null)
            for(Results results : this.results)
                result += results + endl;

        return result;
    }
}
