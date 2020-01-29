package elapor.application.com.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.alexzh.circleimageview.CircleImageView;

import customfonts.MyEditText;
import customfonts.MyTextView;
import elapor.application.com.elapor.MainActivity;
import elapor.application.com.elapor.R;

public class ProfileFragment extends Fragment {

    public static MyEditText edit_nama;
    public static MyEditText edit_bagian;
    public static MyEditText edit_userid;
    public static MyEditText edit_pihak;

    public static MyEditText edit_alamat;
    public static MyEditText edit_nohp;
    public static MyEditText edit_comdev;

    public static CheckBox checkbox_ganti_password;
    public static LinearLayout panel_ganti_password;
    public static MyEditText edit_old_password;
    public static MyEditText edit_password;
    public static MyEditText edit_konfirmasi;

    public static CircleImageView image_profile;
    public static  MyTextView upload;
    public static  MyTextView save;

    float downX = 0, downY = 0, upX, upY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        edit_nama   = rootView.findViewById(R.id.edit_nama);
        edit_bagian = rootView.findViewById(R.id.edit_bagian);
        edit_pihak  = rootView.findViewById(R.id.edit_pihak);
        edit_alamat = rootView.findViewById(R.id.edit_alamat);
        edit_nohp   = rootView.findViewById(R.id.edit_phone);
        edit_comdev = rootView.findViewById(R.id.edit_comdiv);

        edit_userid = rootView.findViewById(R.id.edit_userid);
        checkbox_ganti_password = rootView.findViewById(R.id.checkbox_ganti_password);
        panel_ganti_password = rootView.findViewById(R.id.panel_ganti_password);
        edit_old_password = rootView.findViewById(R.id.edit_old_password);
        edit_password = rootView.findViewById(R.id.edit_password);
        edit_konfirmasi = rootView.findViewById(R.id.edit_konfirmasi);

        image_profile = (CircleImageView) rootView.findViewById(R.id.image_user_profile);
        upload = (MyTextView) rootView.findViewById(R.id.upload);
        save = (MyTextView) rootView.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).simpanDataProfile();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).selectImage();
            }
        });

        edit_pihak.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        downY = event.getY();

                        break;

                    case MotionEvent.ACTION_UP:
                        upX = event.getX();
                        upY = event.getY();
                        float deltaX = downX - upX;
                        float deltaY = downY - upY;

                        if(Math.abs(deltaX)<50 && Math.abs(deltaY)<50) {
                            ((MainActivity) getActivity()).selectPihak();
                            // TODO Auto-generated method stub

                        }

                        break;
                }

                return false;
            }
        });
        
        checkbox_ganti_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkbox_ganti_password.isChecked()) {
                    panel_ganti_password.setVisibility(View.VISIBLE);
                } else {
                    panel_ganti_password.setVisibility(View.GONE);
                }
            }
        });

		((MainActivity) getActivity()).LoadDataProfile();
		return rootView;
    }
}
