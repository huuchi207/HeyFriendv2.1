package adapter;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.chi.heyfriendv21.R;

import java.util.List;

import object.News;

/**
 * Created by huuchi207 on 04/03/2017.
 */

public class TimelineRecyclerViewAdapter extends
        RecyclerView.Adapter<TimelineRecyclerViewAdapter.ViewHolder> {
    private List<News> list;
    private final int LOCKED = 1;
    private final int UNLOCKED = 0;
    private final int PREMIUM = 2;
    private final int COMPLETED = 3;
    private static Context context;
    private ViewHolder viewHolder;
    private Fragment parentFragment;

    public TimelineRecyclerViewAdapter(List<News> list, Context context) {

        this.list = list;
        this.context = context;
        this.parentFragment = parentFragment;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
       public ImageView tv;

        public ViewHolder(View view) {
            super(view);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void onBindViewHolder(final ViewHolder viewHolder,
                                 final int position) {

        viewHolder.setIsRecyclable(false);

    }

    @Override
    public TimelineRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        //Inflate the layout, initialize the View Holder
        View itemLayoutView;
        itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recyclerview_timeline, null);

        viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }


}