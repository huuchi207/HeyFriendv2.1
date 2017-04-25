package dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chi.heyfriendv21.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import common.Constant;
import object.GroupChatData;


public class EditConversationNameDialogFragment extends DialogFragment {
    Button btSave, btCancel;
    //    TextView tvTitle;
    EditText etEditName;
    DatabaseReference databaseReference;

    GroupChatData groupChatData;

    public static interface OnCompleteListener {
        public abstract void onComplete(String name);
    }

    private OnCompleteListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }
    public EditConversationNameDialogFragment(GroupChatData groupChatData) {
        this.groupChatData = groupChatData;
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set style of dialog fragment
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SendInvitationDialog);
    }

    public EditConversationNameDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        //get view
        View view = inflater.inflate(R.layout.dialog_edit_conversation_name, null);
        //init component
        btCancel = (Button) view.findViewById(R.id.btCancel);
        btSave = (Button) view.findViewById(R.id.btOK);
        etEditName = (EditText) view.findViewById(R.id.etEditName);
        if (groupChatData.getName()!= null)
            etEditName.setText(groupChatData.getName());



        //handle event
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(Constant.buttonClick);
                final String newName= etEditName.getText().toString().trim();
                if (newName.equals("")){
                    Toast.makeText(getActivity(), R.string.announce_empty_name, Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child(Constant.CHILD_GROUPCHAT).child(groupChatData.getUid())
                            .child(Constant.KEY_NAME).setValue(newName, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError!= null){
                                Toast.makeText(getActivity(), R.string.announce_error, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getActivity(), R.string.announce_successfully, Toast.LENGTH_SHORT).show();
                                EditConversationNameDialogFragment.this.mListener.onComplete(newName);
                                dismiss();
                            }
                        }
                    });
                }
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(Constant.buttonClick);
                dismiss();
            }
        });
        return view;
    }

}
