package adapter;


import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chi.heyfriendv21.R;

import java.util.List;

import common.CommonMethod;
import de.hdodenhof.circleimageview.CircleImageView;
import object.Post;


public class TimelineRecyclerViewAdapter extends
        RecyclerView.Adapter<TimelineRecyclerViewAdapter.ViewHolder> {
    private List<Post> list;
    private final int LOCKED = 1;
    private final int UNLOCKED = 0;
    private final int PREMIUM = 2;
    private final int COMPLETED = 3;
    private static Context context;
    private ViewHolder viewHolder;
    private Fragment parentFragment;

    public TimelineRecyclerViewAdapter(List<Post> list, Context context) {

        this.list = list;
        this.context = context;
        this.parentFragment = parentFragment;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvContent, tvTime;
        public CircleImageView civAvatar;
        public ImageView ivImage;

        public ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tvName_ItemRecyclerViewTimeline);
            tvContent = (TextView) view.findViewById(R.id.tvContent_ItemRecyclerViewTimeline);
            tvTime = (TextView) view.findViewById(R.id.tvTime_ItemRecyclerViewTimeline);
            civAvatar = (CircleImageView) view.findViewById(R.id.civAvatar_ItemRecyclerViewTimeline);
            ivImage = (ImageView) view.findViewById(R.id.ivImage_ItemRecyclerViewTimeline);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void onBindViewHolder(final ViewHolder viewHolder,
                                 final int position) {
        viewHolder.setIsRecyclable(false);
        Post post = list.get(position);
        viewHolder.tvName.setText(post.getName());
        viewHolder.tvContent.setText(post.getContent());
        viewHolder.tvTime.setText(CommonMethod.diffTime(context, post.getTime()));
        Glide.with(context).load(post.getUrlAvatar()).into(viewHolder.civAvatar);
        if (post.getUrlImage()==null){
            viewHolder.ivImage.setVisibility(View.GONE);
        }
        else Glide.with(context).load(post.getUrlImage()).into(viewHolder.ivImage);
    }

    @Override
    public TimelineRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        //Inflate the layout, initialize the View Holder
        View itemLayoutView;
        itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recyclerview_timeline, parent, false);

        viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }


}