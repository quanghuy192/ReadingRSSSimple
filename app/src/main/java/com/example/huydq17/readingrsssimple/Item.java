package com.example.huydq17.readingrsssimple;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by HuyDQ17 on 10/5/2015.
 */
public class Item {

    private Bitmap imageView;
    private String title;
    private String description;
    private String category;
    private String link;
    private ImageView detail;
    private String detailSrc;

    public String getDetailSrc() {
        String detailSrc = description.split("\" />")[0];
        String linkSrc = detailSrc.split("src=\"")[1];
        return linkSrc;
    }

    public void setDetailSrc(String detailSrc) {
        this.detailSrc = detailSrc;
    }

    public Bitmap getImageView() {
        return imageView;
    }

    public void setImageView(Bitmap imageView) {
        this.imageView = imageView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        String detail = description.split("/>")[1];
        return detail;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ImageView getDetail() {
        return detail;
    }

    public void setDetail(ImageView detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Item{" +
                "imageView=" + imageView +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", link='" + link + '\'' +
                ", detail=" + detail +
                ", detailSrc='" + detailSrc + '\'' +
                '}';
    }
}
