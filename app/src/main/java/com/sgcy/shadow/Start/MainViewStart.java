package com.sgcy.shadow.Start;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.google.gson.Gson;
import com.sgcy.shadow.MovieBean.Actors;
import com.sgcy.shadow.MovieBean.MovieCollection;
import com.sgcy.shadow.R;
import com.sgcy.shadow.app.MainActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lijiali on 2018/6/7.
 *
 * @启动页_28日修改，数据存入数据库——完成
 *
 */

public class MainViewStart extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 2000;
    private SQLiteDatabase db;
    private Button button;
    private List<Actors> actorslist;
    private List<MovieCollection> moviess = new ArrayList<>();
//    private List<MovieDetailBean> moviedetail = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedINstanceState){


        super.onCreate(savedINstanceState);
        setContentView(R.layout.activity_splash);
        db = LitePal.getDatabase();
        loadMovieDatasFromNet("https://api-m.mtime.cn/PageSubArea/HotPlayMovies.api?locationId=290");

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(MainViewStart.this,
                        MainActivity.class);
                MainViewStart.this.startActivity(mainIntent);
                MainViewStart.this.finish();

            }
        },SPLASH_DISPLAY_LENGHT);

    }

    private void loadMovieDatasFromNet(String path) {
        OkHttpUtils.get().url(path).build().execute(new MyCallback());
    }
    class MyCallback extends Callback<List<MovieCollection>> {

        @Override
        public List<MovieCollection> parseNetworkResponse(Response response, int id) throws Exception {
            String content = response.body().string();
            JSONObject jsonObject = new JSONObject(content);
            List<MovieCollection> movies = new ArrayList<>();
            JSONArray moviesJson = (JSONArray) jsonObject.get("movies");
            Gson gson = new Gson();
            for(int i = 0;i<moviesJson.length();i++){
                JSONObject movieJson = (JSONObject) moviesJson.get(i);
                //movieJson就是json的电影对象 ---> Movie
                MovieCollection movie  = gson.fromJson(movieJson.toString(),MovieCollection.class);
                movies.add(movie);
            }
            //存数据库
            LitePal.deleteAll(MovieCollection.class);
            LitePal.saveAll(movies);
            //moviess = DataSupport.where("rDay==15").find(MovieCollection.class);
            Log.i("movieCollections", "parseNetworkResponse: bbbb"+ movies);
            return movies;
        }

        @Override
        public void onError(Call call, Exception e, int id) {

        }
        @Override
        public void onResponse(List<MovieCollection> response, int id) {
            Log.i("movies", "onResponse: "+response);
        }
    }


}
