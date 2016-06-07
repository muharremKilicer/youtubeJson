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

public class BolumlerAdapter extends BaseAdapter {

    private LayoutInflater inf;
    private List<Bolumler> bolls;
    private Activity ac;

    public BolumlerAdapter(Activity activity, List<Bolumler> bolls) {
        inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.bolls = bolls;
        ac = activity;
    }

    @Override
    public int getCount() {
        return bolls.size();
    }

    @Override
    public Bolumler getItem(int position) {
        return bolls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView;
        rowView = inf.inflate(R.layout.row2, null);
        TextView vBaslik = (TextView) rowView.findViewById(R.id.txtBolumBaslik);
        TextView vAciklama = (TextView) rowView.findViewById(R.id.txtKanal);
        ImageView vResim = (ImageView) rowView.findViewById(R.id.videoImg);

        Bolumler ul = bolls.get(position);
        vBaslik.setText(ul.getTitle());
        vAciklama.setText(ul.getChannelTitle());
        Picasso.with(ac).load(ul.getImgUrl()).into(vResim);

        return rowView;
    }
}
