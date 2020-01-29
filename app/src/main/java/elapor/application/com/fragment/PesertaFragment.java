package elapor.application.com.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import elapor.application.com.elapor.MainActivity;

import elapor.application.com.libs.EndlessScrollListener;
import elapor.application.com.libs.ResizableImageView;

import customfonts.MyTextView;
import elapor.application.com.elapor.R;

public class PesertaFragment extends Fragment {

    public static ImageView back;

    public static ListView listView;
    public static ProgressBar loading;
    public static LinearLayout retry;
    public static Button btnReload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_peserta, container, false);

        back = (ImageView) rootView.findViewById(R.id.back);

        listView = (ListView) rootView.findViewById(R.id.lisview);
        loading = (ProgressBar) rootView.findViewById(R.id.pgbarLoading);
        retry = (LinearLayout) rootView.findViewById(R.id.loadMask);
        btnReload = (Button) rootView.findViewById(R.id.btnReload);

        btnReload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //((MainActivity) getActivity()).LoadDataPeserta();
            }
        });

        listView.setOnScrollListener(new EndlessScrollListener() {

            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {

                //((MainActivity) getActivity()).LoadDataPeserta();

                return true;
            }
        });

        //listView.setAdapter(MainActivity.pesertaAdapter);
        loading.setVisibility(View.INVISIBLE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).displayView(0);
            }
        });

        //((MainActivity) getActivity()).LoadDataPeserta();

        return rootView;
    }
}
