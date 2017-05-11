package com.example.android.shopinggururegister.Dos;

/**
 * Created by Android on 11-08-2016.
 */
public class ProductDo {


    private String product;
    private String category;
    private String price;
    private String orignal_price;
    private String selling_price;
    private String image;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product)
    {
        this.product = product;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getPrice()
    {
        return String.valueOf(price);
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getOrignal_price()
    {
        return String.valueOf(orignal_price);
    }

    public void setOrignal_price(String orignal_price)
    {
        this.orignal_price = orignal_price;
    }

    public String getSelling_price()
    {
        return String.valueOf(selling_price);
    }

    public void setSelling_price(String selling_price)
    {
        this.selling_price = selling_price;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public ProductDo(){

    }



}
