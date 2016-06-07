package com.example.java2.cizgifilmapp2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Ayrinti extends AppCompatActivity {

    ListView ls;
    final List<Bolumler> listeArray = new ArrayList<>();
    String gelenPlaylistId;
    String apiKey;
    String url;
    Button ileri,geri;
    String nextToken,prevToken;
    static String videoId,vTitle,vDesc,vChan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayrinti);

        this.setTitle("TÜM BÖLÜMLER");

        ls = (ListView) findViewById(R.id.bolumList);
        gelenPlaylistId=MainActivity.playlistId;
        ileri= (Button) findViewById(R.id.btnIleri);
        geri= (Button) findViewById(R.id.btnGeri);

        //Json'dan gelen verileri almak için youtube tarafından verilen browser key
        apiKey="AIzaSyDx46tNGNZXT8D4U-jRiCRuzdlIrl8motA";

        getir();
        yenile();

    }

    class jsonOku extends AsyncTask<Void,Void,Void> {

        private ProgressDialog pr;
        String url = "";
        String data = "";

        public jsonOku(String url, Activity ac) {
            this.url = url;
            pr =  new ProgressDialog(ac);
            pr.setMessage("Yükleniyor, Lütfen bekleyiniz...");
            pr.show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute(); //Bekleme durumu
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                data = Jsoup.connect(url).ignoreContentType(true).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                pr.dismiss();
            }
            return null;
        }

        int pos;
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);// biten nokta
            try {
                JSONObject obj = new JSONObject(data);
                JSONObject pageInfo = obj.getJSONObject("pageInfo");
                int num = pageInfo.getInt("totalResults");

                final JSONArray ar=obj.getJSONArray("items");
                for (int i = 0; i < ar.length(); i++) {
                    JSONObject gel=ar.getJSONObject(i);
                    JSONObject on=gel.getJSONObject("snippet");
                    int le=on.length();
                    //Eger uzunluk 8'den büyükse video gizli degildir.
                    if(le>8){
                        //Gizli olamayan bu videonun degerleri alınıyor
                        String title=on.getString("title");
                        JSONObject thumb = on.getJSONObject("thumbnails");
                        JSONObject high = thumb.getJSONObject("high");
                        JSONObject reso = on.getJSONObject("resourceId");
                        String des=on.getString("description");
                        String imgUrl=high.getString("url");
                        String channel=on.getString("channelTitle");
                        String videoId=reso.getString("videoId");

                        //Alındı, set edildi. Arraylist'e eklendi.
                        Bolumler ur = new Bolumler(title, des,imgUrl,channel,videoId);
                        listeArray.add(ur);

                    }

                    //Her sayfada 20 tane video gösterilecegi için position olan sayıyı alıyoruz
                    pos=on.getInt("position")+1;

                }
                if(pos<num){
                    //Tüm sayfalar 20 den fazlaya bir sonraki sayfa vardır
                    nextToken = obj.getString("nextPageToken");
                }
                if(pos>20){
                    //Position 20 den büyükse daima geri sayfası vardır.
                    prevToken = obj.getString("prevPageToken");
                }

                yenile();
                
                ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            //Tıklanan'a ait olan bilgileri daha önce set edilmiş sınıftan get edilerek alındı
                            Bolumler ur =listeArray.get(position);
                            videoId=ur.getVideoId();
                            vTitle=ur.getTitle();
                            vDesc=ur.getDescription();
                            vChan=ur.getChannelTitle();
                            //Bir sonraki sayfaya geçildi.
                            Intent i = new Intent(Ayrinti.this, Video.class);
                            startActivity(i);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "ID yakalama hatası: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Veri çekme hatası: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getir(){
        if (!gelenPlaylistId.equals("")){
            //Youtube üzerinden gelen id boş degilse
            url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=20&pageToken=20&playlistId="+gelenPlaylistId+"&key="+apiKey;
            new jsonOku(url,this).execute();
        }else{
            Toast.makeText(Ayrinti.this, "Gelen bilgi hatası", Toast.LENGTH_SHORT).show();
        }
    }

    public void yenile(){
        //Array adapter yenile
        BolumlerAdapter adp = new BolumlerAdapter(Ayrinti.this, listeArray);
        ls.setAdapter(adp);
    }

    public void sayfaIleri(View v){
        if (nextToken!=null){
            //Bir sonraki sayfa varsa bu buton çalışsın yoksa zaten son sayfadır
            //Listeye temizleyelim ki üst üstte binmesin.
            listeArray.clear();
            url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=20&pageToken="+nextToken+"&playlistId="+gelenPlaylistId+"&key="+apiKey;
            new jsonOku(url,this).execute();
        }else{
            Toast.makeText(Ayrinti.this, "Son sayfa", Toast.LENGTH_SHORT).show();
        }

    }

    public void sayfaGeri(View v){
        if (prevToken!=null){
            //Bir önceki sayfa varsa bu buton çalışsın yoksa zaten son sayfadır
            //Listeye temizleyelim ki üst üstte binmesin.
            listeArray.clear();
            url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=20&pageToken="+prevToken+"&playlistId="+gelenPlaylistId+"&key="+apiKey;
            new jsonOku(url,this).execute();
        }else{
            Toast.makeText(Ayrinti.this, "Son sayfa", Toast.LENGTH_SHORT).show();
        }
    }

}
