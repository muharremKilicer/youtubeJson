package com.example.java2.cizgifilmapp2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UrunlerAdapter extends BaseAdapter {

    private LayoutInflater inf;
    private List<Urunler> urls;
    private Activity ac;

    public UrunlerAdapter(Activity activity, List<Urunler> urls) {
        inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.urls = urls;
        ac = activity;
    }

    @Override
    public int getCount() {//ürün sayısı kadar dön
        return urls.size();
    }

    @Override
    public Urunler getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView;
        rowView = inf.inflate(R.layout.row, null);
        TextView vBaslik = (TextView) rowView.findViewById(R.id.txtBaslik);
        ImageView vResim = (ImageView) rowView.findViewById(R.id.resim);

        Urunler ul = urls.get(position);
        vBaslik.setText(ul.getProductName());
        Picasso.with(ac).load(ul.getImgUrl()).into(vResim);

        return rowView;
    }

}
