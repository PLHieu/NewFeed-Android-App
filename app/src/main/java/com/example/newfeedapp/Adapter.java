package com.example.newfeedapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.newfeedapp.model.Article;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Article> _articles;
    private Context _context;
    private OnItemClickListener _onItemClickListener;

    public Adapter(List<Article> articles, Context context) {
        _articles = articles;
        _context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(view, _onItemClickListener);
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        final MyViewHolder holder = holders;
        Article model = _articles.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(_context)
                .load(model.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder._progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                       holder._progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder._imageView);
        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDescription());
        holder.source.setText(model.getSource().getName());
        holder.time.setText(" \u2022 " + Utils.DateToTimeFormat(model.getPublishedAt()));
        holder.published_ad.setText(Utils.DateFormat(model.getPublishedAt()));
        holder.author.setText(model.getAuthor());
    }

    @Override
    public int getItemCount() {
        return _articles.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this._onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView title, desc, author, published_ad, source, time;
        ImageView _imageView;
        ProgressBar _progressBar;
        OnItemClickListener _onItemClickListener;

        public MyViewHolder(View itemview, OnItemClickListener onItemClickListener){
            super(itemview);

            itemview.setOnClickListener(this);
            title = itemview.findViewById(R.id.title);
            desc = itemview.findViewById(R.id.desc);
            author = itemview.findViewById(R.id.author);
            published_ad = itemview.findViewById(R.id.publishedAt);
            source = itemview.findViewById(R.id.source);
            time = itemview.findViewById(R.id.time);
            _imageView = itemview.findViewById(R.id.img);
            _progressBar = itemview.findViewById(R.id.prograss_load_photo);
            _onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            _onItemClickListener.onItemClick(v,getAdapterPosition() );
        }
    }
}
