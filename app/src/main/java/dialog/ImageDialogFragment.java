package dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chi.heyfriendv21.R;


public class ImageDialogFragment extends DialogFragment {
    private ImageView imageView;
    private String photoUrl;
    private Dialog dialog;
    public ImageDialogFragment(String photoUrl) {
        this.photoUrl= photoUrl;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public ImageDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        //get view
        View view = inflater.inflate(R.layout.dialog_image, null);
        //init component
        imageView = (ImageView) view.findViewById(R.id.ivPhoto_ImageDialog);
        Glide.with(view.getContext()).load(photoUrl).into(imageView);


        return view;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.DialogFloatingTheme);

        dialog.show();
        return dialog;
    }
}
