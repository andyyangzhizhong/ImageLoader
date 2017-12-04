package cn.andy.study.imageloader.facebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import cn.andy.study.imageloader.R;

/**
 * Created by yangzhizhong
 */

public class ImageAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    private List<String> mList;
    private LayoutInflater mInflater;

    private Context context;

    private ImageLoader mImageLoader;
    private int mStart, mEnd;
    public static String[] URLS;
    private boolean mFirstIn;

    public ImageAdapter(Context context, List<String> list, GridView gridView) {
        this.mList = list;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        mImageLoader = new ImageLoader(gridView);

        URLS = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            URLS[i] = list.get(i);
        }
        mFirstIn = true;
        gridView.setOnScrollListener(this);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_layout, parent, false);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String url = (String) getItem(position);
        holder.ivIcon.setTag(url);
        holder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "点击的条目是:" + (position + 1), Toast.LENGTH_SHORT).show();
            }
        });

        // x.image().bind(holder.ivIcon, url);
        mImageLoader.showImageByAsyncTask(url, holder.ivIcon);
        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            mImageLoader.loadImages(mStart, mEnd);
        } else {
            mImageLoader.cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;
        if (mFirstIn && visibleItemCount > 0) {
            mImageLoader.loadImages(mStart, mEnd);
            mFirstIn = false;
        } else if (visibleItemCount <= 0) {
            return;
        }
    }

    static class ViewHolder {
        ImageView ivIcon;
    }
}
