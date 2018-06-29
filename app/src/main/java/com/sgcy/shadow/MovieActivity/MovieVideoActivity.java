package com.sgcy.shadow.MovieActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.sgcy.shadow.MovieBean.Director;
import com.sgcy.shadow.MovieBean.Video;
import com.sgcy.shadow.MovieAdapter.MovieVideoAdapter;
import com.sgcy.shadow.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.Call;
import okhttp3.Response;

public class MovieVideoActivity extends AppCompatActivity {
    private String video_url;
    private String movie_name;
    private String movie_date;
    private String video_img;
    private String video_title;
    private JCVideoPlayerStandard jcVideoPlayerStandard;
    private int movieid;
    private int videoId;
    private Video videoData;
    private List<Video> videoList;
    private TextView vd_name;
    private TextView vd_time;
    private TextView vd_number;
    private ListView ls;
    private MovieVideoAdapter movieVideoAdapter;
    private String m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_video);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            actionBar.hide();
        }
        Intent intent = getIntent();
        video_url = intent.getStringExtra("video_url");
        movie_name=intent.getStringExtra("movie_name");
        movie_date=intent.getStringExtra("movie_time");
        video_img=intent.getStringExtra("video_img");
        video_title=intent.getStringExtra("video_title");
        movieid= intent.getIntExtra("moviedId",0);
        videoId=intent.getIntExtra("videoId",0);
        Log.i("movie_name", "onCreate: "+movie_name);
        Log.i("time", "onCreate: "+movie_date);
        vd_name= (TextView) findViewById(R.id.vd_movie_name);
        vd_time= (TextView) findViewById(R.id.movie_time);
        vd_number= (TextView) findViewById(R.id.vd_number);
        ls = (ListView) findViewById(R.id.vd_videolist);
        vd_name.setText(movie_name);
        vd_time.setText(movie_date+" 大陆上映");
        //删除异常
        LitePal.deleteAll(Video.class, "length = ? and moviedid = ?", "0","movieid");
         m = movieid+"";
        int isexist = LitePal.where("moviedid==?",m).count(Video.class);
        if(isexist==1){
            loadVideo("https://api-m.mtime.cn/Movie/Video.api?pageIndex=1&movieId="+movieid);
            Log.i("获取", "onCreate: " + "获取");
            Log.i("zsszzzzzzzzzzzz", "onCreate: " + videoData);
        }else{
            //从数据库中取
            videoList = LitePal.where("moviedid==?",m).find(Video.class);
            vd_number.setText("("+videoList.size()+")");
            loadvd(videoList);
            BindData(videoList);
            Log.i("直接取", "onCreate: " + "直接取");
        }
        //点击事件
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Video video = videoList.get(position);

                Toast.makeText(MovieVideoActivity.this,video.getTitle(),Toast.LENGTH_SHORT).show();
                movieVideoAdapter.setSelectedPosition(position);
                movieVideoAdapter.notifyDataSetInvalidated();//刷新适配器
                jcVideoPlayerStandard.setUp(videoList.get(position).getHightUrl(),
                        jcVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,videoList.get(position).getTitle());
                Glide.with(MovieVideoActivity.this).load(videoList.get(position).getImage()).into(jcVideoPlayerStandard.thumbImageView);
            }
        });
    }
    private void loadvd(List<Video> videoList){
        Video video = new Video();
        video=LitePal.where("moviedid==?", m).find(Video.class).get(0);
        jcVideoPlayerStandard= (JCVideoPlayerStandard) findViewById(R.id.video_view);
        jcVideoPlayerStandard.setUp(video.getHightUrl(),jcVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,video.getTitle());
        Glide.with(this).load(video.getImage()).into(jcVideoPlayerStandard.thumbImageView);
    }
    private void loadVideo(String path) {
        OkHttpUtils.get().url(path).build().execute(new MyCallback3());
    }
    class MyCallback3 extends Callback<List<Video>> {
        @Override
        public List<Video> parseNetworkResponse(Response response, int id) throws Exception {
            String content = response.body().string();
            JSONObject jsonObject = new JSONObject(content);
            List<Video> videos = new ArrayList<>();
            JSONArray videosJson = (JSONArray) jsonObject.get("videoList");
            Gson gson = new Gson();
            for(int i=0;i<videosJson.length();i++){
                JSONObject videoJson = (JSONObject) videosJson.get(i);
                Video video = gson.fromJson(videoJson.toString(),Video.class);
                video.setMoviedid(movieid);
                videos.add(video);
            }
            LitePal.saveAll(videos);
            return videos;
        }
        @Override
        public void onError(Call call, Exception e, int id) {

        }
        @Override
        public void onResponse(List<Video> response, int id) {
            loadvd(response);
            BindData(response);
        }
    }

    private void BindData(List<Video> videoData) {
         movieVideoAdapter = new MovieVideoAdapter(videoData,MovieVideoActivity.this);
        ls.setAdapter(movieVideoAdapter);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
