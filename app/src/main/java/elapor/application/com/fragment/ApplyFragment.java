package elapor.application.com.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import customfonts.MyEditText;
import customfonts.MyTextView;
import elapor.application.com.elapor.MainActivity;
import elapor.application.com.elapor.R;
import elapor.application.com.libs.EndlessScrollListener;
import elapor.application.com.libs.ResizableImageView;

public class ApplyFragment extends Fragment {

    float downX = 0, downY = 0, upX, upY;

    public static MyTextView total_competitor;

    public static ImageView back;

    public static ResizableImageView image;
    public static MyTextView title;
    public static MyTextView kyorugi;
    public static MyTextView poomsae;
    public static MyTextView tanggal;
    public static MyTextView lokasi;
    public static MyTextView atlit;
    public static MyTextView pendaftaran;
    public static MyTextView label_tutup;
    

    public static LinearLayout linear_addCompetitor;
    public static MyEditText edit_atlit;
    public static ImageView pickup_atlit;

    public static MyEditText edit_divisi;
    public static ImageView pickup_divisi;

    public static MyEditText edit_kelas;
    public static ImageView pickup_kelas;
    
    public static MyTextView btn_tambah;

    public static MyTextView label_no_add;

    public static ListView listView;
    public static ProgressBar loading;
    public static LinearLayout retry;
    public static Button btnReload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_apply_competitor, container, false);

        back = (ImageView) rootView.findViewById(R.id.back);

        listView = (ListView) rootView.findViewById(R.id.lisview);
        loading = (ProgressBar) rootView.findViewById(R.id.pgbarLoading);
        retry = (LinearLayout) rootView.findViewById(R.id.loadMask);
        btnReload = (Button) rootView.findViewById(R.id.btnReload);

        LayoutInflater myinflater = getActivity().getLayoutInflater();
        ViewGroup myHeader = (ViewGroup)myinflater.inflate(R.layout.header_fragment_apply_competitor, listView, false);
        listView.addHeaderView(myHeader, null, false);

        image = (ResizableImageView) myHeader.findViewById(R.id.image);
        title = (MyTextView) myHeader.findViewById(R.id.title);
        kyorugi = (MyTextView) myHeader.findViewById(R.id.grup_kyorugi);
        poomsae = (MyTextView) myHeader.findViewById(R.id.grup_poomsae);
        tanggal = (MyTextView) myHeader.findViewById(R.id.tanggal);
        lokasi = (MyTextView) myHeader.findViewById(R.id.lokasi);
        atlit = (MyTextView) myHeader.findViewById(R.id.atlit);
        pendaftaran = (MyTextView) myHeader.findViewById(R.id.pendaftaran);
        label_tutup = (MyTextView) myHeader.findViewById(R.id.label_tutup);

        total_competitor = (MyTextView) myHeader.findViewById(R.id.total_competitor);

        linear_addCompetitor = (LinearLayout)  myHeader.findViewById(R.id.linear_add);
        edit_atlit    = (MyEditText) myHeader.findViewById(R.id.edit_atlit);
        pickup_atlit  = (ImageView) myHeader.findViewById(R.id.pickup_atlit);
        edit_divisi   = (MyEditText) myHeader.findViewById(R.id.edit_divisi);
        pickup_divisi = (ImageView) myHeader.findViewById(R.id.pickup_divisi);
        edit_kelas    = (MyEditText) myHeader.findViewById(R.id.edit_kelas);
        pickup_kelas  = (ImageView) myHeader.findViewById(R.id.pickup_kelas);
        btn_tambah   = (MyTextView) myHeader.findViewById(R.id.tambah);
        
        total_competitor.setVisibility(View.GONE);
        label_no_add = (MyTextView)  rootView.findViewById(R.id.label_no_add);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).displayView(0);
            }
        });

        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity) getActivity()).applyNewCompetitor();
            }
        });
        pickup_atlit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity) getActivity()).LoadDialogListView("apply_atlit");
            }
        });
        edit_atlit.setOnTouchListener(new View.OnTouchListener() {

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
                            //((MainActivity) getActivity()).LoadDialogListView("apply_atlit");
                        }

                        break;
                }

                return false;
            }
        });

        pickup_divisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity) getActivity()).LoadDialogListView("apply_divisi");
            }
        });
        edit_divisi.setOnTouchListener(new View.OnTouchListener() {

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
                            //((MainActivity) getActivity()).LoadDialogListView("apply_divisi");
                        }

                        break;
                }

                return false;
            }
        });

        pickup_kelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity) getActivity()).LoadDialogListView("apply_kelas");
            }
        });
        edit_kelas.setOnTouchListener(new View.OnTouchListener() {

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

                            //((MainActivity) getActivity()).LoadDialogListView("apply_kelas");
                        }

                        break;
                }

                return false;
            }
        });

        btnReload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //((MainActivity) getActivity()).LoadDataApply();
            }
        });

        listView.setOnScrollListener(new EndlessScrollListener() {

            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {

                //((MainActivity) getActivity()).LoadDataApply();

                return true;
            }
        });

        //listView.setAdapter(MainActivity.applyAdapter);
        loading.setVisibility(View.INVISIBLE);

        //((MainActivity) getActivity()).LoadDetailEvent_ApplyCompetitorFragment();
        //((MainActivity) getActivity()).LoadDataSelectAtlit();
		//((MainActivity) getActivity()).LoadDataApply();

        return rootView;
    }
}
