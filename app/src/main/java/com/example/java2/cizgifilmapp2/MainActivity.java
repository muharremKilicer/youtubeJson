package com.example.java2.cizgifilmapp2;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView liste;
    final List<Urunler> ls = new ArrayList<>();
    //Ürünlerin id lerini tutmak için
    ArrayList<String> idler = new ArrayList<>();
    //Tıklanan videonun youtube üzerinde kayıtlı olduğu playlist video url adresi
    static String playlistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("KAHRAMANLAR");
        liste = (ListView) findViewById(R.id.karakterList);

        calis();
    }

    public void calis(){
        if(isNetworkAvailable()){
            //İnternet varsa
            String url ="http://jsonbulut.com/json/product.php?ref=7d638cf8daf402e8925b9651bbb68d09&start=0";
            new jsonOku(url, this).execute();

            UrunlerAdapter adp = new UrunlerAdapter(this, ls);
            liste.setAdapter(adp);
        }else{
            //İnternet yoksa
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Baglantı hatası");
            builder.setMessage("İnternet baglantısı bulanmadı!");
            //İnternet olmadıgında bu mesajın sürekli gösterilmesi için tekrar class'ı tetkiliyoruz
            builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    calis();
                }
            });
            builder.show();
        }
    }

    class jsonOku extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pr;

        String url = "";
        String data = "";

        public jsonOku(String url, Activity ac) {
            this.url = url;
            pr = new ProgressDialog(ac);
            pr.setMessage("Yükleniyor, Lütfen Bekleyiniz...");
            pr.show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute(); // bekleme durumu
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid); // biten nokta. Grafiksel işlemler burada yapılır.
            try {
                JSONObject obj = new JSONObject(data);
                JSONArray arr = obj.getJSONArray("Products");
                JSONObject yaz = arr.getJSONObject(0);
                final JSONArray ar = yaz.getJSONArray("bilgiler");
                String thumb = null;
                for (int i = 0; i < ar.length(); i++) {
                    JSONObject bil = ar.getJSONObject(i);
                    String baslik = bil.getString("productName");
                    String imgDurum = bil.getString("image");

                    if (!imgDurum.equals("false")){
                        JSONArray resimAr = bil.getJSONArray("images");
                        thumb = resimAr.getJSONObject(0).getString("normal");
                    }else{
                        thumb="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQl7ufVlj1nFi95OJLhWlzedMYuBLRl8swh_HdHQQYkm6-fu8eRKw";
                    }
                    //Json parçalanıp alınan degerler urunler class'ına set edildi.
                    Urunler ur = new Urunler(baslik, thumb);
                    ls.add(ur);
                    //Aynı ürünleri id leri alındı
                    idler.add(bil.getString("productId"));

                }

                UrunlerAdapter adp = new UrunlerAdapter(MainActivity.this, ls);
                liste.setAdapter(adp);

                liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            //Tıklananın playlist id'si alındı
                            JSONObject cat= ar.getJSONObject(position);
                            playlistId=cat.getString("brief");
                            //Diger sayfaya geçildi
                            Intent i = new Intent(MainActivity.this, Ayrinti.class);
                            startActivity(i);
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "ID yakalama hatası: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            //burada toast gibi buton gibi grafiksel yapıları çalıştıramayız.
            try {
                data = Jsoup.connect(url).timeout(30000).ignoreContentType(true).execute().body();
                //Log.d("Gelen Data : ", data);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                pr.dismiss(); // yükleniyor yazısını durdurur.
            }
            return null;
        }
    }

    private boolean isNetworkAvailable() {
        //İnternet kontrol sınıfımız
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
