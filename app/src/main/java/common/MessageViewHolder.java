package common;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.chi.heyfriendv21.R;

import de.hdodenhof.circleimageview.CircleImageView;



public class MessageViewHolder extends RecyclerView.ViewHolder
        implements View.OnCreateContextMenuListener {
    public TextView tvMessage;
    public TextView tvDateSending;
    public CircleImageView ivMessenger;
    public LinearLayout linearLayout, llAllComponent, llContentMsg;
    public ImageView ivPhoto;
    public int DELETE= 3;
    public int COPY= 2;

    public MessageViewHolder(View v) {
        super(v);
        tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
        tvDateSending = (TextView) itemView.findViewById(R.id.tvDateSending);
        ivMessenger = (CircleImageView) itemView.findViewById(R.id.ivMessenger);
        linearLayout= (LinearLayout) itemView.findViewById(R.id.llOneToOneChat);
        llAllComponent =(LinearLayout) itemView.findViewById(R.id.llAllComponent);
        ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
        llContentMsg = (LinearLayout) itemView.findViewById(R.id.llContentMsg);
        v.setOnCreateContextMenuListener(this);

    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            menu.add(0,1 , 0, "Edit");
        menu.add(0, COPY, 0, "Copy");
        menu.add(0, DELETE, 0, "Delete");
    }

}