package com.example.karim.muzzafapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.karim.muzzafapp.R;

import java.util.List;

public class GVAdapter extends BaseAdapter {
    Context _ctx;
    List<String> images;

    public GVAdapter(Context _ctx, List<String> images) {
        this._ctx = _ctx;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=LayoutInflater.from(_ctx).inflate(R.layout.image_item,parent,false);
        ImageView imageView=view.findViewById(R.id.image);
        Glide.with(_ctx).load(images.get(position)).placeholder(R.drawable.defualt).into(imageView);
        return view;
    }
}
