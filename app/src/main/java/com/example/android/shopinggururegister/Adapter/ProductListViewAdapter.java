package com.example.android.shopinggururegister.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.shopinggururegister.Dos.ProductDo;
import com.example.android.shopinggururegister.R;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by vikra_000 on 07-09-2016.
 */
public class ProductListViewAdapter extends ArrayAdapter<ProductDo> {
    ArrayList<ProductDo> ArrayListActors;
    int Resource;
    Context context;
    LayoutInflater vi;

    public ProductListViewAdapter(Context context, int resource, ArrayList<ProductDo> objects) {
        super(context, resource, objects);

        ArrayListActors = objects;
        Resource = resource;
        this.context = context;

        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){

            convertView = vi.inflate(Resource, null);
            holder = new ViewHolder();

            holder.imageView = (ImageView)convertView.findViewById(R.id.imageView);
            holder.textView1=(TextView)convertView.findViewById(R.id.textView1);
            holder.textView2=(TextView)convertView.findViewById(R.id.textView2);
            holder.textView3=(TextView)convertView.findViewById(R.id.textView3);
            holder.textView4=(TextView)convertView.findViewById(R.id.textView4);
            holder.textView5=(TextView)convertView.findViewById(R.id.textView5);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        new DownloadImageTask(holder.imageView).execute(ArrayListActors.get(position).getImage());
        holder.textView1.setText(ArrayListActors.get(position).getProduct());
        holder.textView2.setText(ArrayListActors.get(position).getCategory());
        holder.textView3.setText("Rs-"+ ArrayListActors.get(position).getPrice()+"/-");
        holder.textView4.setText("Original Price-"+ ArrayListActors.get(position).getOrignal_price());
        holder.textView5.setText("Selling Price-"+ ArrayListActors.get(position).getSelling_price());

        return convertView;

    }



    static class ViewHolder{
        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        public TextView textView4;
        public TextView textView5;


    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
        ImageView imageView;
        public DownloadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11= BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

}


