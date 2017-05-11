package com.example.android.shopinggururegister.Dos;

/**
 * Created by Android on 24-01-2017.
 */
public class RecentOrdersDataObject {
    private String order_total;
    private String order_date;
    private String order_status;
    private String order_id;


    public RecentOrdersDataObject(String order_total, String order_date, String order_id, String order_status) {
        this.order_total = order_total;
        this.order_date = order_date;
        this.order_id = order_id;
        this.order_status = order_status;
    }


    public String getOrder_total() {
        return order_total;
    }

    public void setOrder_total(String order_total) {
        this.order_total = order_total;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
