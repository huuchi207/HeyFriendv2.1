package common;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chi.heyfriendv21.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class CommentViewHolder extends RecyclerView.ViewHolder
      {
    public TextView tvContent;
    public TextView tvDateSending;
    public CircleImageView ivMessenger;
    public TextView tvSender;

    public CommentViewHolder(View v) {
        super(v);
        tvContent = (TextView) itemView.findViewById(R.id.tvContent_ItemRecyclerViewComment);
        tvSender = (TextView) itemView.findViewById(R.id.tvName_ItemRecyclerViewComment);
        tvDateSending = (TextView) itemView.findViewById(R.id.tvTime_ItemRecyclerViewComment);
        ivMessenger = (CircleImageView) itemView.findViewById(R.id.civAvatar_ItemRecyclerViewComment);

    }

}