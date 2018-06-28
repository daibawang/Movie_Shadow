package com.sgcy.shadow.MovieAdapter;



import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sgcy.shadow.MovieBean.StageImg;
import com.sgcy.shadow.MovieActivity.MovieDetailsActiyity;
import com.sgcy.shadow.MovieView.ASmartimgView;
import com.sgcy.shadow.R;

import java.util.List;

/**
 * Created by dai on 2018/6/26.
 */

public class MoviePhotoListAdapter extends RecyclerView.Adapter<MoviePhotoListAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater mInfalter;
    private List<StageImg> imgsList;

    public MoviePhotoListAdapter(Context context, List<StageImg> imgsList) {
        mInfalter=LayoutInflater.from(context);
        this.context = context;
        this.imgsList = imgsList;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInfalter.inflate(R.layout.item_movie_photo,parent,false));

    }

    @Override
    public void onBindViewHolder(MoviePhotoListAdapter.ViewHolder holder, int position) {
        StageImg stageImg = this.imgsList.get(position);
        holder.photo.setId_(position);
        holder.photo.loadImageFromNet(stageImg.getImgUrl());
    }

    @Override
    public int getItemCount() {
        return this.imgsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ASmartimgView photo;
        public ViewHolder(View itemView) {
            super(itemView);
            photo=itemView.findViewById(R.id.iv_movie_photo);

        }
    }
    //rv_movie_video_photo
}
