package com.sgcy.shadow.MovieAdapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sgcy.shadow.MovieBean.Comments;
import com.sgcy.shadow.MovieView.ASmartimgView;
import com.sgcy.shadow.R;

import java.util.List;

/**
 * Created by dai on 2018/6/29.
 */

public class MovieCommentAdapter extends RecyclerView.Adapter<MovieCommentAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater mInfalter;
    private List<Comments> commentList;

    public MovieCommentAdapter(Context context, List<Comments> commentList) {
        this.context = context;
        mInfalter=LayoutInflater.from(context);
        this.commentList = commentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInfalter.inflate(R.layout.item_movie_comment,parent,false));

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comments comment = this.commentList.get(position);
        holder.comment_detail.setText(comment.getCe());
        holder.comment_img.loadImageFromNet(comment.getCaimg());
        holder.comment_name.setText(comment.getCa());
        Log.i("zzzzcomment.getCr()zzzz", "onBindViewHolder: "+comment.getCr());
        holder.comment_score.setText(comment.getCr()+"分");
        int s = R.drawable.plstar1;
//        holder.comment_star.setImageDrawable(context.getResources().getDrawable(R.drawable.plstar1));
        holder.time.setText(comment.getCal());

    }

    @Override
    public int getItemCount() {
        return this.commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView comment_name;
        ASmartimgView comment_img;
        ImageView comment_star;
        TextView comment_score;
        TextView comment_detail;
        TextView time;
        TextView com_ishot;
        public ViewHolder(View itemView) {
            super(itemView);
            com_ishot=itemView.findViewById(R.id.com_ishot);
            comment_detail=itemView.findViewById(R.id.comment_detail);
            comment_name=itemView.findViewById(R.id.comment_name);
            comment_star=itemView.findViewById(R.id.comment_star);
            comment_img=itemView.findViewById(R.id.comment_img);
            comment_score=itemView.findViewById(R.id.comment_score);
            time=itemView.findViewById(R.id.comment_time_place);
        }
    }
}
