package com.sgcy.shadow.MovieActivity;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lcodecore.extextview.ExpandTextView;
import com.sgcy.shadow.MovieAdapter.MovieCommentAdapter;
import com.sgcy.shadow.MovieBean.Actors;
import com.sgcy.shadow.MovieBean.Comments;
import com.sgcy.shadow.MovieBean.Director;
import com.sgcy.shadow.MovieBean.MovieCollection;
import com.sgcy.shadow.MovieBean.Other;
import com.sgcy.shadow.MovieBean.StageImg;
import com.sgcy.shadow.MovieBean.Video;
import com.sgcy.shadow.MovieAdapter.MoviePhotoListAdapter;
import com.sgcy.shadow.MovieAdapter.MovieStarListAdapter;
import com.sgcy.shadow.MovieView.ASmartimgView;
import com.sgcy.shadow.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class MovieDetailsActiyity extends AppCompatActivity {

    private SQLiteDatabase db;
    public int movie_id;
    private List<Actors> actorsDataList;
    //private List<Director> directorDataList;
    private Director directorData;
    private Other otherData;
    private Video videoData;
    private List<StageImg> stageImgsList;
    private String m;//电影ID.string
    //电影信息
    private TextView movie_name_c;
    private TextView movie_d;
    private TextView movie_name_en;
    private TextView movie_type;
    private TextView movie_place_time;
    private TextView movie_shangyintime;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private MovieStarListAdapter movieAdapter;
    private ExpandTextView expandTextView;
    private ASmartimgView aSmartimgView;
    private RecyclerView recyclerView_photo;
    private MoviePhotoListAdapter photoListAdapter;
    private LinearLayoutManager linearLayoutManager2;
    private LinearLayoutManager linearLayoutManager3;
    private String date;
    //收藏想看
    private ImageView star;
    private ImageView love;
    private TextView star_text;
    private TextView love_text;
    private int flag1=0;
    private MovieCollection delMovie;
    //视频
    private FrameLayout framevideo;
    //变色
    private LinearLayout changelayout;
    private LinearLayout changelayout2;
    private ContentValues values = new ContentValues();
    private ContentValues values2 = new ContentValues();

    private MovieCommentAdapter movieCommentAdapter;
    private List<Comments> commentsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__detail);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }
        movie_name_c = (TextView) findViewById(R.id.tv_movie_name);
        movie_d = (TextView) findViewById(R.id.tv_movie_3D);
        movie_name_en = (TextView) findViewById(R.id.tv_movie_english_name);
        movie_type = (TextView) findViewById(R.id.tv_movie_type);
        movie_place_time = (TextView) findViewById(R.id.tv_src_place_time);
        movie_shangyintime = (TextView) findViewById(R.id.tv_movie_time);
        expandTextView = (ExpandTextView) findViewById(R.id.rv_movie_story);
        aSmartimgView = (ASmartimgView) findViewById(R.id.iv_movie_img);
        framevideo = (FrameLayout) findViewById(R.id.fl_movie_img);
        changelayout= (LinearLayout) findViewById(R.id.movie_love_layout);
        changelayout2= (LinearLayout) findViewById(R.id.movie_collection_layout);
        star = (ImageView) findViewById(R.id.movie_collection_star);
        love = (ImageView) findViewById(R.id.movie_love_heart);
        star_text= (TextView) findViewById(R.id.movie_collection_text);
        love_text= (TextView) findViewById(R.id.movie_love_text);
        Intent intent = getIntent();
        movie_id=intent.getIntExtra("movieid",0);

        Log.i("movieid", "onCreate:这是" + movie_id);
         m = movie_id + "";
        int movie = LitePal.where("movieId==?", m).count(Other.class);
        int com=LitePal.where("movieid==?",m).count(Comments.class);

        Log.i("movie", "onCreate: " + movie);
        if (movie == 0||com==0) {
            loadactors("https://ticket-api-m.mtime.cn/movie/detail.api?locationId=290&movieId=" + movie_id);
            loadComments("https://api-m.mtime.cn/Showtime/HotMovieComments.api?pageIndex=1&movieId="+movie_id);
            Log.i("获取", "onCreate: " + "获取");

//            Log.i("zsszzzzzzzzzzzz", "onCreate: " + commentList);
        } else {
            actorsDataList = LitePal.where("moviedid==?", m).find(Actors.class);
            directorData = LitePal.where("moviedid==?", m).find(Director.class).get(0);
            stageImgsList = LitePal.where("moviedid==?", m).find(StageImg.class);
            otherData = LitePal.where("movieid==?", m).find(Other.class).get(0);
            videoData = LitePal.where("moviedid==?", m).find(Video.class).get(0);
            commentsList=LitePal.where("movieid=?",m).find(Comments.class);
            Log.i("直接去", "onCreate: " + "直接去");
            BindData(otherData,actorsDataList,stageImgsList,videoData);
            BindData2(commentsList);
        }

    }

    private void loadComments(String path) {
        OkHttpUtils.get().url(path).build().execute(new MyCallback5());
    }


    private void BindData(final Other otherData, List<Actors> actorsDataList, List<StageImg> stageImgsList, final Video videoData) {
        //绑定数据
        movie_name_c.setText(otherData.getName());
        movie_name_en.setText(otherData.getNameEn());
        String type = "";
        for (int i = 0; i < otherData.getType().size(); i++) {
            type = type + otherData.getType().get(i) + " ";
        }
        movie_type.setText(type);
        movie_place_time.setText(otherData.getReleaseArea() + " / " + otherData.getMins());
        date = otherData.getReleaseDate().substring(0, 4) + "-" + otherData.getReleaseDate().substring(4, 6) + "-" + otherData.getReleaseDate().substring(6, 8);
        movie_shangyintime.setText(date);
        String screenTypeS = "2D";
        if (otherData.isIs3D()) {
            screenTypeS = "3D";
        } else if (otherData.isIMAX()) {
            screenTypeS = "IMAX";
        } else if (otherData.isIMAX3D()) {
            screenTypeS = "IMAX3D";
        }
        movie_d.setText(screenTypeS);
        expandTextView.setText(otherData.getStory());
        aSmartimgView.loadImageFromNet(otherData.getImg());
        //显示
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView = (RecyclerView) findViewById(R.id.rv_movie_star);
        movieAdapter = new MovieStarListAdapter(this, actorsDataList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(movieAdapter);
        linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_photo = (RecyclerView) findViewById(R.id.rv_movie_video_photo);
        photoListAdapter = new MoviePhotoListAdapter(this, stageImgsList);
        recyclerView_photo.setLayoutManager(linearLayoutManager2);
        recyclerView_photo.setAdapter(photoListAdapter);
        //视频播放
        framevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieDetailsActiyity.this, MovieVideoActivity.class);
                intent.putExtra("video_url", videoData.getHightUrl());
                intent.putExtra("video_title", videoData.getTitle());
                intent.putExtra("video_img", videoData.getImg());
                intent.putExtra("movie_name", otherData.getName());
                intent.putExtra("movie_time", date);
                intent.putExtra("moviedId",movie_id);
                intent.putExtra("videoId",videoData.getVideoId());
                MovieDetailsActiyity.this.startActivity(intent);
            }
        });
        delMovie = LitePal.where("movieid==?", m).find(MovieCollection.class).get(0);
        final boolean collection = delMovie.isCollection();
        if(collection==true){
            star.setImageDrawable(getResources().getDrawable(R.drawable.star2));
            star_text.setText("已收藏");
        }
        Log.i("collectionddddddddd", "onCreate: "+collection);
        //点击变色,数据库改变
        changelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag1++;
                if(flag1==1){
                    love.setImageDrawable(getResources().getDrawable(R.drawable.love2));
                    love_text.setText("已想看");
                }else {
                    love.setImageDrawable(getResources().getDrawable(R.drawable.love1));
                    love_text.setText("想看");
                    flag1=0;
                }

            }
        });

        changelayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                values.put("collection", 1);
                values2.put("collection", 0);
                if(collection==true){
                    star.setImageDrawable(getResources().getDrawable(R.drawable.star1));
                    star_text.setText("收藏");
                    LitePal.updateAll(MovieCollection.class,values2,"movieid = ?",m);
                }else{
                    star.setImageDrawable(getResources().getDrawable(R.drawable.star2));
                    star_text.setText("已收藏");
                    LitePal.updateAll(MovieCollection.class,values,"movieid = ?",m);
                }
            }
        });
    }

    private void loadactors(String path) {
        OkHttpUtils.get().url(path).build().execute(new MyCallback1());
    }

    private class MyCallback1 extends Callback<Map<String,Object>> {
        @Override
        public Map<String,Object> parseNetworkResponse(Response response, int id) throws Exception {
            String content = response.body().string();
            JSONObject jsonObject = new JSONObject(content);
            JSONObject jsondata = (JSONObject) jsonObject.get("data");
            JSONObject jsonbasic = (JSONObject) jsondata.get("basic");
            JSONArray jsonactors = (JSONArray) jsonbasic.get("actors");
            Gson gson = new Gson();
            List<Actors> actors = new ArrayList<>();
            for (int i = 0; i < jsonactors.length(); i++) {
                JSONObject actorJson = (JSONObject) jsonactors.get(i);
                //movieJson就是json的电影对象 ---> Movie
                Actors act = gson.fromJson(actorJson.toString(), Actors.class);
                act.setMoviedid(movie_id);
                actors.add(act);
            }
            //存数据库
            LitePal.saveAll(actors);
            //解析img
            JSONObject jsonStageimg = (JSONObject) jsonbasic.get("stageImg");
            JSONArray jsonimg = (JSONArray) jsonStageimg.get("list");
            List<StageImg> stageImgs = new ArrayList<>();
            for (int i = 0; i < jsonimg.length(); i++) {
                JSONObject imgJson = (JSONObject) jsonimg.get(i);
                StageImg stageImg = gson.fromJson(imgJson.toString(), StageImg.class);
                stageImg.setMoviedid(movie_id);
                stageImgs.add(stageImg);
            }
            LitePal.saveAll(stageImgs);
            //解析director
            JSONObject jsondirector = (JSONObject) jsonbasic.get("director");
            Director director = new Director();
            director = gson.fromJson(jsondirector.toString(), Director.class);
            director.setMoviedid(movie_id);
            director.save();

            //解析other
            Other other = new Other();
            other = gson.fromJson(jsonbasic.toString(), Other.class);
            other.save();
//            //解析Video
            JSONObject jsonvideo = (JSONObject) jsonbasic.get("video");
            Video video = new Video();
            video = gson.fromJson(jsonvideo.toString(), Video.class);
            video.setMoviedid(movie_id);
            video.save();
            Map<String,Object> map = new HashMap<>();
            map.put("actorsDataList",actors);
            map.put("directorData",director);
            map.put("videoData",video);
            map.put("stageImgsList",stageImgs);
            map.put("otherData",other);
            return map;
        }

        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(Map<String, Object> map, int id) {
            BindData((Other) map.get("otherData"),(List<Actors>)map.get("actorsDataList"),(List<StageImg>)map.get("stageImgsList"),(Video) map.get("videoData"));
        }
    }



    private class MyCallback5 extends Callback <List<Comments>> {
        @Override
        public List<Comments> parseNetworkResponse(Response response, int id) throws Exception {
            String content = response.body().string();
            JSONObject jsonObject = new JSONObject(content);
            List<Comments> comments = new ArrayList<>();
            JSONObject jsondata = (JSONObject) jsonObject.get("data");
            JSONArray ctsTson = (JSONArray)jsondata.get("cts");

            Log.i("jaaaaaaaaaaaaa", "parseNetworkResponse: "+ctsTson);

            for(int i = 0;i<ctsTson.length();i++){

                JSONObject comJson = (JSONObject) ctsTson.getJSONObject(i);
                Comments comments1 = new Comments();
                comments1.setMovieid(movie_id);
                comments1.setCa(comJson.getString("ca"));
                comments1.setCaimg(comJson.getString("caimg"));
                comments1.setCal(comJson.getString("cal"));
                comments1.setCd(comJson.getInt("cd"));
                comments1.setCe(comJson.getString("ce"));
                comments1.setCr((int)comJson.getDouble("cr"));
                comments1.setHot(comJson.getBoolean("isHot"));
                comments1.save();
                comments.add(comments1);
            }
            return comments;
        }

        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(List<Comments> response, int id) {
            BindData2(response);
        }
    }

    private void BindData2(List<Comments> response) {
        linearLayoutManager3 = new LinearLayoutManager(this);
        linearLayoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) findViewById(R.id.rv_movie_video_comment);
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
         movieCommentAdapter = new MovieCommentAdapter(this,response);
         recyclerView.setLayoutManager(linearLayoutManager3);
         recyclerView.addItemDecoration(decoration);
         recyclerView.setAdapter(movieCommentAdapter);
    }


}
