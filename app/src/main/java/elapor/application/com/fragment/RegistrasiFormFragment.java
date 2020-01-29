package elapor.application.com.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alexzh.circleimageview.CircleImageView;

import customfonts.MyEditText;
import customfonts.MyTextView;
import elapor.application.com.elapor.ProsesRegistrasiActivity;
import elapor.application.com.elapor.R;

public class RegistrasiFormFragment extends Fragment {

    public static MyEditText edit_nama;
    public static MyEditText edit_bagian;

    /*public static MyEditText edit_pihak;
    public static MyEditText edit_alamat;
    public static MyEditText edit_nohp;
    public static MyEditText edit_comdiv;*/

    public static MyEditText edit_userid;
    public static MyEditText edit_password;
    public static MyEditText edit_konfirmasi;

    public static CircleImageView image_profile;
    public static  MyTextView upload;
    public static  MyTextView save;

    float downX = 0, downY = 0, upX, upY;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_registrasi_form, container, false);

        edit_nama = rootView.findViewById(R.id.edit_nama);
        edit_bagian = rootView.findViewById(R.id.edit_bagian);

        /*edit_pihak = rootView.findViewById(R.id.edit_pihak);
        edit_alamat = rootView.findViewById(R.id.edit_alamat);
        edit_nohp = rootView.findViewById(R.id.edit_nohp);
        edit_comdiv = rootView.findViewById(R.id.edit_comdiv);*/

        edit_userid = rootView.findViewById(R.id.edit_userid);
        edit_password = rootView.findViewById(R.id.edit_password);
        edit_konfirmasi = rootView.findViewById(R.id.edit_konfirmasi);

        image_profile =  rootView.findViewById(R.id.image_user_profile);
        upload = rootView.findViewById(R.id.upload);
        save = rootView.findViewById(R.id.save);

        /*edit_pihak.setOnTouchListener(new View.OnTouchListener() {

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
                            ((ProsesRegistrasiActivity) getActivity()).selectPihak();
                            // TODO Auto-generated method stub

                        }

                        break;
                }

                return false;
            }
        });*/

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ProsesRegistrasiActivity) getActivity()).selectImage();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((ProsesRegistrasiActivity) getActivity()).submitRegistrasi();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
