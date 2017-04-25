package dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.chi.heyfriendv21.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;

import common.CommonMethod;
import object.GroupChatData;
import object.Tag;
import view.ContactCompletionView;


public class ListParticipantsDialogFragment extends DialogFragment {
    Button btSave, btCancel;
    //    TextView tvTitle;

    DatabaseReference databaseReference;
    FirebaseUser currentFirebaseUser;

    ArrayList<Tag> tags;
    ArrayList<Tag> selectedTags;
    ContactCompletionView completionView;
    ArrayAdapter<Tag> adapter;
    GroupChatData groupChatData;
    public ListParticipantsDialogFragment(GroupChatData groupChatData) {
        currentFirebaseUser= CommonMethod.getCurrentFirebaseUser();
        this.groupChatData = groupChatData;
//        tags= new ArrayList<>();
//        databaseReference= FirebaseDatabase.getInstance().getReference();
        selectedTags = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set style of dialog fragment
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SendInvitationDialog);
    }

    public ListParticipantsDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        //get view
        View view = inflater.inflate(R.layout.dialog_list_participants, null);
        //init component
//        btCancel = (Button) view.findViewById(R.id.btCancel);
        btSave = (Button) view.findViewById(R.id.btOK);
        completionView = (ContactCompletionView) view.findViewById(R.id.searchView);
//        completionView.s
        //auto complete view
//        completionView.setTokenListener(new TokenCompleteTextView.TokenListener<Tag>() {
//            @Override
//            public void onTokenAdded(Tag token) {
//                adapter.remove(token);
//                adapter.notifyDataSetChanged();
//                selectedTags.add(token);
//
//            }
//
//            @Override
//            public void onTokenRemoved(Tag token) {
//                adapter.add(token);
//                adapter.notifyDataSetChanged();
//                selectedTags.remove(token);
//            }
//        });
        for (GroupChatData.Participant participant : groupChatData.getParticipants()) {
            if (participant.isStatus()){
                Tag tag = new Tag(participant.getUid(), participant.getName(), participant.getPhotoUrl());
                completionView.addObject(tag);
                selectedTags.add(tag);
                //remove duplicate tag
//                for (int i=0; i< adapter.getCount(); i++){
//                    if (adapter.getItem(i).getUid().equals(tag.getUid())){
//                        adapter.remove(adapter.getItem(i));
//                        adapter.notifyDataSetChanged();
//                    }
                }
            }
//        }
        completionView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.None);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, selectedTags);
//        getTaglist();
        completionView.setAdapter(adapter);


        //handle event
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethod.showAnimation(v, getActivity());
                dismiss();
                }

        });
//        btCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.startAnimation(Constant.buttonClick);
//                dismiss();
//            }
//        });
        return view;
    }

//    public void getTaglist(){
//        databaseReference.child(Constant.CHILD_FRIENDLISTS).
//                child(currentFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                tags.clear();
//                for (DataSnapshot ds: dataSnapshot.getChildren()) {
//                    String uid = ds.getKey();
//                    String name = "";
//                    String photoUrl = "";
//                    if (ds.hasChild(Constant.KEY_NAME)) {
//                        name = ds.child(Constant.KEY_NAME).getValue().toString();
//                    }
//                    if (ds.hasChild(Constant.KEY_PHOTOURL)) {
//                        photoUrl = ds.child(Constant.KEY_PHOTOURL).getValue().toString();
//                    }
//                    if (name.equals("") || photoUrl.equals("")) {
//
//                    } else {
//                        Tag tag = new Tag(uid, name, photoUrl);
//                        adapter.add(tag);
//                        adapter.notifyDataSetChanged();
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }
}
