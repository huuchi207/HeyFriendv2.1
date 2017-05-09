package dialog;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chi.heyfriendv21.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import common.CommonMethod;
import common.Constant;
import de.hdodenhof.circleimageview.CircleImageView;
import object.GroupChatData;
import object.Post;

import static android.app.Activity.RESULT_OK;


public class NewStatusDialogFragment extends DialogFragment {
    ImageButton ibAddImage;
    CircleImageView ivClearImage;
    EditText etContent;
    Button btCancel, btOK;
    ImageView ivImage;
    LinearLayout llAddImage;
    View view;
    Bitmap imageBitmap;
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    public NewStatusDialogFragment(GroupChatData groupChatData) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set style of dialog fragment
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SendInvitationDialog);
    }

    public NewStatusDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        //get view
        view = inflater.inflate(R.layout.dialog_post_status, null);
        //init component
        ibAddImage = (ImageButton) view.findViewById(R.id.ibAddImage);
        btOK = (Button) view.findViewById(R.id.btOK);
        btCancel = (Button) view.findViewById(R.id.btCancel);
        etContent= (EditText) view.findViewById(R.id.etContent);
        ivClearImage =(CircleImageView) view.findViewById(R.id.ivClearImage);
        ivImage = (ImageView) view.findViewById(R.id.iv_dialog_post_status);
        llAddImage = (LinearLayout) view.findViewById(R.id.llAddImage);
        //handle event
        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(Constant.buttonClick);
            if (imageBitmap==null){
                pushNew("");
            }
            else postImageToFirebase(imageBitmap);
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(Constant.buttonClick);
                dismiss();
            }
        });
        ibAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                onLaunchCamera();

            }
        });
        ivClearImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivImage.setImageResource(android.R.color.transparent);
                llAddImage.setVisibility(View.VISIBLE);
                ivClearImage.setVisibility(View.GONE);
                imageBitmap = null;
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap= (Bitmap) extras.get("data");
            //show image
            ivImage.setImageBitmap(imageBitmap);
            ivClearImage.setVisibility(View.VISIBLE);
        }
    }
    private void postImageToFirebase(Bitmap bitmap) {
        if (bitmap!= null) {
            String fileName = ""+bitmap.hashCode();
            StorageReference filepathRef =
                    FirebaseStorage.getInstance().getReference("images"+ "/"+ fileName);

            //compress image
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

            byte[] data = byteArrayOutputStream.toByteArray();

            UploadTask uploadTask = filepathRef.putBytes(data);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests")
                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    pushNew(downloadUrl.toString());

                }
            });

        }
    }
    private void pushNew(String photoUrl){
        String content = etContent.getText().toString().trim();
        if (content.equals("") ){
            Toast.makeText(view.getContext(), getString(R.string.txt_write_something),
                    Toast.LENGTH_SHORT).show();
            return;
        }else{
            if (photoUrl.equals(""))
                photoUrl = null;
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            String avatar = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
            long currentTime = CommonMethod.getCurrentTime();

            firebaseDatabase.getReference(Constant.CHILD_TIMELINE+"/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()
            ).push().setValue(new Post(avatar, currentTime, name,  content, photoUrl));

        }
        dismiss();
    }
    void onLaunchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(view.getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
