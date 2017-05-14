package adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import common.CommentViewHolder;
import common.CommonMethod;
import object.Comment;


public class CommentRecyclerViewAdapter extends
        FirebaseRecyclerAdapter<Comment, CommentViewHolder> {

    private Context context;
    private int position;
    public String clientUid, myUid, clientPhotoURL;
    private DatabaseReference ref;
    private Activity activity;
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CommentRecyclerViewAdapter(Class<Comment> modelClass, int modelLayout,
                                      Class<CommentViewHolder> viewHolderClass,
                                      DatabaseReference ref, Context context
                                                    , Activity activity) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.ref = ref;
        this.context = context;
        this.activity = activity;

    }

    @Override
    protected void populateViewHolder(CommentViewHolder viewHolder, final Comment comment, final int position) {
        //dont reuse view
        viewHolder.setIsRecyclable(false);
        Log.e("CommentRVAdapter", comment.getContent());
        //show msg
        Glide.with(context).load(clientPhotoURL).into(viewHolder.ivMessenger);
        viewHolder.tvContent.setText(comment.getContent());
        viewHolder.tvSender.setText(comment.getName());
        viewHolder.tvDateSending.setText(CommonMethod.convertLongToTime(comment.getTime()));

    }

}
