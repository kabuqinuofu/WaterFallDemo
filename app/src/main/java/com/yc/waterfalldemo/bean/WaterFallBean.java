package com.yc.waterfalldemo.bean;

/**
 * @author by CNKIFU on 2019-04-23.
 */
public class WaterFallBean {

    private String width;
    private String height;
    private String img;
    private String link;
    private float scale;

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public String toString() {
        return "WaterFallBean{" +
                "width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", img='" + img + '\'' +
                ", link='" + link + '\'' +
                ", scale=" + scale +
                '}';
    }
}
