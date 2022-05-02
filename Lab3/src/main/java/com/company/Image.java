package com.company;

public class Image {
    private String id;
    private int width;
    private int height;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id='" + id + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", url='" + url + '\'' +
                '}';
    }
}
