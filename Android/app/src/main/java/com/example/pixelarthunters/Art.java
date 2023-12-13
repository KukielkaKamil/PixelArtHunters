package com.example.pixelarthunters;
import com.google.gson.annotations.SerializedName;
public class Art {
    @SerializedName("id")
    protected int id;

    @SerializedName("size")
    protected int size;

    @SerializedName("image")
    protected String image;

    @SerializedName("poi_id")
    protected int poi_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPoi_id() {
        return poi_id;
    }

    public void setPoi_id(int poi_id) {
        this.poi_id = poi_id;
    }
}
