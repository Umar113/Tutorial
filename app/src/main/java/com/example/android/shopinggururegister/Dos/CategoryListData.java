package com.example.android.shopinggururegister.Dos;

/**
 * Created by anjal on 26/11/2016.
 */
public class CategoryListData {

    public String product_id;
    public String product_name;
    public String product_image;
    public String product_desc;
    public String product_actual_price;
    public String product_selling_price;
    public Boolean likeStatus = false;
    private int liked = 0;


    public CategoryListData() {
    }

    public CategoryListData(String product_id, String product_name, String product_image, String product_desc, String product_actual_price, String product_selling_price, int liked) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_desc = product_desc;
        this.product_actual_price = product_actual_price;
        this.product_selling_price = product_selling_price;
        this.liked = liked;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getProduct_actual_price() {
        return product_actual_price;
    }

    public void setProduct_actual_price(String product_actual_price) {
        this.product_actual_price = product_actual_price;
    }

    public String getProduct_selling_price() {
        return product_selling_price;
    }

    public void setProduct_selling_price(String product_selling_price) {
        this.product_selling_price = product_selling_price;
    }

    public Boolean getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(Boolean likeStatus) {
        this.likeStatus = likeStatus;
    }


    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }
}
