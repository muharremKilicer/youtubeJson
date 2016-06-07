package com.example.java2.cizgifilmapp2;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;

public class Video extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    String gelenVideoId;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    TextView vbaslik,vaciklama,vkanal;
    String gTitle,gDesc,gChan;
    TextView watch,like,dislike;

    private static final int RECOVERY_DIALOG_REQUEST = 10;
    //Youtube üzerinde olan android key
    public static final String API_KEY = "AIzaSyA_oyItYOOd1EIUQWFoQetqz9txf3V0OBI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        //Bu bilgiler ayrıntı sayfasında zaten alınmıştı
        //Tekrardan json çalıştırmamak için static yapıldı alındı.
        gelenVideoId = Ayrinti.videoId;
        gTitle = Ayrinti.vTitle;
        gDesc  = Ayrinti.vDesc;
        gChan  = Ayrinti.vChan;

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubeVideoPlayer);
        vbaslik = (TextView) findViewById(R.id.txtVideoBaslik);
        vaciklama = (TextView) findViewById(R.id.txtVideoAciklama);
        vkanal = (TextView) findViewById(R.id.txtKanal);
        watch = (TextView) findViewById(R.id.txtWatch);
        like = (TextView) findViewById(R.id.txtLike);
        dislike = (TextView) findViewById(R.id.txtDislike);

        YouTubePlayerView youTubeView;

        try {
            if (!gelenVideoId.equals("")){
                //Video id düzgün geldiyse video bilgileri set edilsin.
                youTubeView = (YouTubePlayerView) findViewById(R.id.youtubeVideoPlayer);
                youTubeView.initialize(API_KEY, this);
                vbaslik.setText(gTitle);
                vkanal.setText("Kanal: "+gChan);
                vaciklama.setText(gDesc);
            }else{
                Toast.makeText(Video.this, "Video id boş geldi", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(Video.this, "Hata: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            
        }

        //Video'ya ait olan istatistik bilgiler. Kaç izlenme like dislike
        String url = "https://www.googleapis.com/youtube/v3/videos?part=statistics&id="+gelenVideoId+"&key=AIzaSyDx46tNGNZXT8D4U-jRiCRuzdlIrl8motA";
        new jsonOku(url,this).execute();

    }

    public void aboneOl(View v){
        Uri uri = Uri.parse("https://www.youtube.com/user/TRTCOCUKKANALI"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
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
                JSONArray items = obj.getJSONArray("items");
                JSONObject sta = items.getJSONObject(0);
                JSONObject istatic = sta.getJSONObject("statistics");
                String viewCount = istatic.getString("viewCount");
                String likeCount = istatic.getString("likeCount");
                String dislikeCount = istatic.getString("dislikeCount");

                //Videoya ait olan izlenme sayısı like ve dislike bilgileri set edildi.
                watch.setText(viewCount);
                like.setText(likeCount);
                dislike.setText(dislikeCount);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Veri çekme hatası: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format("YouTube Error (%1$s)",
                    errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(gelenVideoId);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtubeVideoPlayer);
    }
}
