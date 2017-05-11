package com.example.android.shopinggururegister.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.android.shopinggururegister.Dos.CategoryListData;
import com.example.android.shopinggururegister.Dos.WishListData;

import java.util.ArrayList;

/**
 * Created by Android on 12-12-2016.
 */
public class ProductsListDb extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "product.db";
    public static final int DATABASE_VERSION = 1;
    public static final String WISHLIST_TABLE_NAME = "wishlist_table";
    public static final String CART_TABLE_NAME = "cart_table";


    public static final String COULMN_ID = "id";
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_PRODUCT_IMAGE = "product_image";
    public static final String COLUMN_PRODUCT_NAME = "product_name";
    public static final String COLUMN_PRODUCT_ACTUAL_PRICE = "product_actual_price";
    public static final String COLUMN_PRODUCT_SELLING_PRICE = "product_selling_price";


    public static final String COLUMN_IS_PRODUCT_LIKED = "product_like";

    boolean isLiked;
    public int i = isLiked ? 1 : 0;

    public static final String[] PROJECTIONS_FOR_WISHLIST = {COULMN_ID, COLUMN_PRODUCT_ID, COLUMN_PRODUCT_NAME, COLUMN_PRODUCT_ACTUAL_PRICE, COLUMN_PRODUCT_SELLING_PRICE, COLUMN_IS_PRODUCT_LIKED, COLUMN_PRODUCT_IMAGE};
    public static final String[] COLUMN_DATA_TYPE_FOR_WISHLIST = {"INTEGER PRIMARY KEY AUTOINCREMENT", "TEXT", "TEXT", "TEXT", "TEXT", "INTEGER DEFAULT 0", "BLOB"};


    public static final String[] PROJECTIONS_FOR_CART = {COULMN_ID, COLUMN_PRODUCT_ID, COLUMN_PRODUCT_NAME, COLUMN_PRODUCT_SELLING_PRICE, COLUMN_PRODUCT_IMAGE};
    public static final String[] COLUMN_DATA_TYPE_FOR_CART = {"INTEGER PRIMARY KEY AUTOINCREMENT", "TEXT", "TEXT", "TEXT", "BLOB"};


    private SQLiteDatabase sqLiteDatabaseWritableDB;
    private SQLiteDatabase sqLiteDatabaseReadableDB;

    Context context;

    public ProductsListDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        sqLiteDatabaseWritableDB = this.getWritableDatabase();
        sqLiteDatabaseReadableDB = this.getReadableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        table for wishlist
        String createWishListTableQuery = "create table " + WISHLIST_TABLE_NAME + " ( ";
        for (int i = 0; i < PROJECTIONS_FOR_WISHLIST.length; i++) {
            if (i == 0) {
                createWishListTableQuery += PROJECTIONS_FOR_WISHLIST[i] + " " + COLUMN_DATA_TYPE_FOR_WISHLIST[i];
            } else {
                createWishListTableQuery += ", " + PROJECTIONS_FOR_WISHLIST[i] + " " + COLUMN_DATA_TYPE_FOR_WISHLIST[i];
            }
        }
        createWishListTableQuery += ");";
        Log.d(">>>> ProductDB query ", createWishListTableQuery);
        sqLiteDatabase.execSQL(createWishListTableQuery);

//        table for cart item

        String createCartTableQuery = "create table " + CART_TABLE_NAME + " ( ";
        for (int i = 0; i < PROJECTIONS_FOR_CART.length; i++) {
            if (i == 0) {
                createCartTableQuery += PROJECTIONS_FOR_CART[i] + " " + COLUMN_DATA_TYPE_FOR_CART[i];
            } else {
                createCartTableQuery += ", " + PROJECTIONS_FOR_CART[i] + " " + COLUMN_DATA_TYPE_FOR_CART[i];
            }
        }
        createCartTableQuery += ");";
        Log.d(">>>> ProductDB query ", createCartTableQuery);
        sqLiteDatabase.execSQL(createCartTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String upgradeWishlistQuery = "DROP TEBLE IF EXISTS " + WISHLIST_TABLE_NAME;
        sqLiteDatabase.execSQL(upgradeWishlistQuery);
        String upgradeCartQuery = "DROP TEBLE IF EXISTS " + CART_TABLE_NAME;
        sqLiteDatabase.execSQL(upgradeCartQuery);
        onCreate(sqLiteDatabase);
    }

    public int getCartCount() {
        String countQuery = "SELECT  * FROM " + CART_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


    public void insertProductIntoDb(String product_id, String product_name, String product_selling_price, String product_actual_price, int isLiked, byte[] product_image) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_PRODUCT_ID, product_id);
        contentValues.put(COLUMN_PRODUCT_NAME, product_name);
        contentValues.put(COLUMN_PRODUCT_SELLING_PRICE, product_selling_price);
        contentValues.put(COLUMN_PRODUCT_ACTUAL_PRICE, product_actual_price);
        contentValues.put(COLUMN_IS_PRODUCT_LIKED, isLiked);
        contentValues.put(COLUMN_PRODUCT_IMAGE, product_image);
        sqLiteDatabaseWritableDB.insert(WISHLIST_TABLE_NAME, null, contentValues);
        Toast.makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT).show();

    }


    public void insertProductIntoCart(String product_id, String product_name, String product_selling_price, byte[] product_image) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_PRODUCT_ID, product_id);
        contentValues.put(COLUMN_PRODUCT_NAME, product_name);
        contentValues.put(COLUMN_PRODUCT_SELLING_PRICE, product_selling_price);
        contentValues.put(COLUMN_PRODUCT_IMAGE, product_image);
        sqLiteDatabaseWritableDB.insert(CART_TABLE_NAME, null, contentValues);
        Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();

    }


    public void deleteParticularProductsFromDatabase(String product_id) {
        String deleteProductQuery = "DELETE FROM " + WISHLIST_TABLE_NAME + " WHERE " + COLUMN_PRODUCT_ID + " = '" + product_id + "'";
        sqLiteDatabaseWritableDB.execSQL(deleteProductQuery);
    }

    public void deleteParticularProductsFromCartDatabase(String product_id) {
        String deleteProductQuery = "DELETE FROM " + CART_TABLE_NAME + " WHERE " + COLUMN_PRODUCT_ID + " = '" + product_id + "'";
        sqLiteDatabaseWritableDB.execSQL(deleteProductQuery);
    }

    public void deleteAllProductsFromDatabase() {
        int numOfRecordDeleted = sqLiteDatabaseWritableDB.delete(WISHLIST_TABLE_NAME, null, null);
        System.out.println(">>NUm of record deleted>>" + numOfRecordDeleted);
    }

    public ArrayList<WishListData> getProductsFromDatabase() {

        ArrayList<WishListData> categoryListDataArrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + WISHLIST_TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                WishListData categoryListData = new WishListData();
                categoryListData.setProduct_id(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ID)));
                categoryListData.setProduct_name(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME)));
                categoryListData.setProduct_image(cursor.getBlob(cursor.getColumnIndex(COLUMN_PRODUCT_IMAGE)));
                categoryListData.setProduct_selling_price(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_SELLING_PRICE)));
                categoryListData.setProduct_actual_price(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ACTUAL_PRICE)));
                categoryListDataArrayList.add(categoryListData);
            } while (cursor.moveToNext());
        }
        return categoryListDataArrayList;
    }

    public ArrayList<WishListData> getProductsFromCartDatabase() {

        ArrayList<WishListData> categoryListDataArrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + CART_TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                WishListData categoryListData = new WishListData();
                categoryListData.setProduct_id(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ID)));
                categoryListData.setProduct_name(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME)));
                categoryListData.setProduct_image(cursor.getBlob(cursor.getColumnIndex(COLUMN_PRODUCT_IMAGE)));
                categoryListData.setProduct_selling_price(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_SELLING_PRICE)));
//                categoryListData.setProduct_actual_price(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ACTUAL_PRICE)));
                categoryListDataArrayList.add(categoryListData);
            } while (cursor.moveToNext());
        }
        return categoryListDataArrayList;
    }

}
