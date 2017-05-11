package com.example.android.shopinggururegister.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.shopinggururegister.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Android on 20-08-2016.
 */
public class MessageAdapter extends BaseAdapter {
    Context messageContext;
    ArrayList<Message> messageArrayList;

    public MessageAdapter(Context context, ArrayList<Message> messages) {
        messageArrayList = messages;
        messageContext = context;

    }

    @Override
    public int getCount() {
        return messageArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private static class MessageViewHolder {
        public ImageView thumbnailImageView;
        public TextView senderView;
        public TextView bodyView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MessageViewHolder holder;

        // if there is not already a view created for an item in the Message list.

        if (convertView == null) {
            LayoutInflater messageInflater = (LayoutInflater) messageContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            // create a view out of our `message.xml` file
            convertView = messageInflater.inflate(R.layout.message_view_layout, null);

            // create a MessageViewHolder
            holder = new MessageViewHolder();

            // set the holder's properties to elements in `message.xml`
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.img_thumbnail);
            holder.senderView = (TextView) convertView.findViewById(R.id.message_sender);
            holder.bodyView = (TextView) convertView.findViewById(R.id.message_body);

            // assign the holder to the view we will return
            convertView.setTag(holder);
        } else {

            // otherwise fetch an already-created view holder
            holder = (MessageViewHolder) convertView.getTag();
        }

        // get the message from its position in the ArrayList
        Message message = (Message) getItem(position);

        // set the elements' contents
//        holder.bodyView.setText(message.text);
//        holder.senderView.setText(message.name);

        // fetch the user's Twitter avatar from their username
        // and place it into the thumbnailImageView.
        Picasso.with(messageContext).
                load("https://facebook.com/" + message + "/profile_image?size=original").
                placeholder(R.mipmap.ic_launcher).
                into(holder.thumbnailImageView);

        return convertView;

    }

    public void add(Message message) {
        messageArrayList.add(message);
        notifyDataSetChanged();
    }
}
