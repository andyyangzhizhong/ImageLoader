package cn.andy.study.imageloader.facebook;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.andy.study.imageloader.CircleImageView;
import cn.andy.study.imageloader.R;
import cn.andy.study.imageloader.imageloader.view.AsyncImageView;

/**
 * Created by yangzhizhong
 */

public class ImageAdapterFB extends BaseAdapter {

    private Context context;
    private List<String> uriList = new ArrayList<>();

    private boolean isPicasso = true;
    private boolean isFb = true;
    private boolean isMySelf = true;
    private boolean isGlide = true;

    public void setPicasso(boolean picasso) {
        isPicasso = picasso;
    }

    public void setFb(boolean fb) {
        isFb = fb;
    }

    public void setMySelf(boolean mySelf) {
        isMySelf = mySelf;
    }

    public void setGlide(boolean glide) {
        isGlide = glide;
    }

    public ImageAdapterFB(Context context, List<String> uriList) {
        super();
        this.context = context;
        this.uriList = uriList;
    }

    @Override
    public int getCount() {
        return uriList.size();
    }

    @Override
    public Object getItem(int position) {
        return uriList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.fb_image_view_item, parent, false);
            viewHolder.imageView = convertView.findViewById(R.id.item_fb);
            viewHolder.asyncImageView = convertView.findViewById(R.id.item_my);
            viewHolder.picasso = convertView.findViewById(R.id.picasso);
            viewHolder.glide = convertView.findViewById(R.id.glide);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Uri uri = Uri.parse(uriList.get(position));
        switch ((position + 1) % 4) {
            case 1:
                viewHolder.imageView.setVisibility(View.GONE);
                viewHolder.picasso.setVisibility(isPicasso ? View.VISIBLE : View.GONE);
                viewHolder.asyncImageView.setVisibility(View.GONE);
                viewHolder.glide.setVisibility(View.GONE);
                if (isPicasso) {
                    Picasso.with(context).load(uri).into(viewHolder.picasso);
                    Picasso.with(context).load(uri).error(R.drawable.common).into(viewHolder.picasso);
                }
                break;
            case 2:
                viewHolder.imageView.setVisibility(isFb ? View.VISIBLE : View.GONE);
                viewHolder.picasso.setVisibility(View.GONE);
                viewHolder.asyncImageView.setVisibility(View.GONE);
                viewHolder.glide.setVisibility(View.GONE);
                if (isFb) {
                    viewHolder.imageView.setImageURI(uri);
                }
                break;
            case 3:
                viewHolder.imageView.setVisibility(View.GONE);
                viewHolder.picasso.setVisibility(View.GONE);
                viewHolder.glide.setVisibility(View.GONE);
                viewHolder.asyncImageView.setVisibility(isMySelf ? View.VISIBLE : View.GONE);
                if (isMySelf) {
                    viewHolder.asyncImageView.loadUrlImage(uri.toString());
                }
                break;
            case 0:
                viewHolder.imageView.setVisibility(View.GONE);
                viewHolder.picasso.setVisibility(View.GONE);
                viewHolder.glide.setVisibility(isGlide ? View.VISIBLE : View.INVISIBLE);
                viewHolder.asyncImageView.setVisibility(View.GONE);
                if (isGlide){
                    Glide.with(context).load(uri).into(viewHolder.glide);
                    Glide.with(context).load(uri).error(R.drawable.common).into(viewHolder.glide);
                }
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        SimpleDraweeView imageView;
        CircleImageView glide;
        CircleImageView picasso;
        AsyncImageView asyncImageView;
    }
}
