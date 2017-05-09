package adapter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import com.chi.heyfriendv21.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import common.CommonMethod;
import common.Constant;
import common.MessageViewHolder;
import dialog.ImageDialogFragment;
import dialog.UserInfoDialogFragment;
import object.Message;


public class PrivateMessagesForRecyclerViewAdapter extends FirebaseRecyclerAdapter<Message, MessageViewHolder> {

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

    public PrivateMessagesForRecyclerViewAdapter(Class<Message> modelClass, int modelLayout,
                                                 Class<MessageViewHolder> viewHolderClass,
                                                 DatabaseReference ref, Context context, String clientUid,
                                                 String myUid, String clientPhotoURL
                                                    ,Activity activity) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.ref = ref;
        this.context = context;
        this.clientPhotoURL = clientPhotoURL;
        this.clientUid = clientUid;
        this.myUid = myUid;
        this.activity = activity;


    }

    @Override
    protected void populateViewHolder(MessageViewHolder viewHolder, final Message message, final int position) {
        //dont reuse view
        viewHolder.setIsRecyclable(false);
        //get senderUid to present data to recyclerview
        String senderUid = message.getSenderUid();
        // update status of lastmessage
        if (position == getItemCount() - 1) {
//            Log.e(position+ "---"+ getItemCount(), "test message");
            ref.getRoot().child(Constant.CHILD_FRIENDLISTS).child(myUid).child(clientUid).child(Constant.CHILD_LASTMESSAGE).child(Constant.KEY_STATUS).setValue(true, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.e("completion error", databaseError.getDetails());
//                            Toast.makeText(OneToOneConversationActivity.this, R.string.announce_cant_send_msg, Toast.LENGTH_SHORT).show();
                    } else {
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(clientUid.hashCode());
                    }
                }
            });
        }

        //show msg
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10;
        lp.rightMargin = 10;

        if (position > 0) {
            long diffTime = (message.getTime() - getItem(position - 1).getTime());
            if (diffTime < 600000) {
                viewHolder.tvDateSending.setVisibility(View.GONE);
            } else
                viewHolder.tvDateSending.setText(CommonMethod.convertLongToTime(message.getTime()));
//                    Log.e(message.getTime()+ "--"+ CommonMethod.convertLongToTime(message.getTime())+ "--" +CommonMethod.convertLongToTime(getItem(position-1).getTime())+"//"+ getItem(position-1).getTime(), diffTime+"");
        }
        else viewHolder.tvDateSending.setText(CommonMethod.convertLongToTime(message.getTime()));
        if (message.getMessage().equals("Image")) {
            viewHolder.tvMessage.setVisibility(View.GONE);
        } else viewHolder.tvMessage.setText(message.getMessage());
        if (!message.getPhotoUrl().equals("")) {
            Glide.with(context).load(message.getPhotoUrl()).into(viewHolder.ivPhoto);
        } else viewHolder.ivPhoto.setVisibility(View.GONE);

        if (senderUid.equals(clientUid)) {
            lp.gravity = Gravity.LEFT;
            viewHolder.linearLayout.setLayoutParams(lp);
            if (position > 0 && getItem(position - 1).getSenderUid().equals(clientUid)) {
                viewHolder.ivMessenger.setImageResource(android.R.color.transparent);
                long diffTime = (message.getTime() - getItem(position - 1).getTime());
                if (diffTime < 600000) {
//                    viewHolder.tvMessage.setBackgroundResource(R.drawable.receive_second);
                    viewHolder.llContentMsg.setBackgroundResource(R.drawable.receive_second);
                } else {
                    viewHolder.llContentMsg.setBackgroundResource(R.drawable.receive_first);
                }
//                else viewHolder.tvMessage.setBackgroundResource(R.drawable.receive_first);
            } else {
                viewHolder.tvMessage.setBackgroundResource(R.drawable.receive_first);
                Glide.with(context).load(clientPhotoURL).into(viewHolder.ivMessenger);
            }

            viewHolder.tvMessage.setTextColor(Color.WHITE);
            viewHolder.tvMessage.setLayoutParams(lp);
        } else {
            lp.gravity = Gravity.RIGHT;
            viewHolder.linearLayout.setLayoutParams(lp);
//                    viewHolder.linearLayout.setGravity(Gravity.RIGHT);
            viewHolder.ivMessenger.setImageResource(android.R.color.transparent);
            if (position > 0 && getItem(position - 1).getSenderUid().equals(myUid)) {
                long diffTime = (message.getTime() - getItem(position - 1).getTime());
                if (diffTime < 600000) {
//                    viewHolder.tvMessage.setBackgroundResource(R.drawable.sending_second);
                    viewHolder.llContentMsg.setBackgroundResource(R.drawable.sending_second);
                } else{
//                    viewHolder.tvMessage.setBackgroundResource(R.drawable.sending_first);
                    viewHolder.llContentMsg.setBackgroundResource(R.drawable.sending_first);
                }


            } else viewHolder.tvMessage.setBackgroundResource(R.drawable.sending_first);
// viewHolder.tvMessage.setBackgroundResource(R.drawable.bubble_out);
            viewHolder.tvMessage.setLayoutParams(lp);

        }
        //for context menu
        viewHolder.tvMessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(position);
                return false;
            }

        });
        viewHolder.llAllComponent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(position);
                return false;
            }

        });
        viewHolder.ivPhoto.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                setPosition(position);
                return false;
            }
        });
        viewHolder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageDialogFragment dialog = new ImageDialogFragment(message.getPhotoUrl());
                dialog.show(activity.getFragmentManager(), "");
            }
        });
    }

}
