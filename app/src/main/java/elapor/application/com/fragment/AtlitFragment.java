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
import elapor.application.com.elapor.R;
import elapor.application.com.libs.EndlessScrollListener;

public class AtlitFragment extends Fragment {

	public static ImageView back, tambah;
	public static ListView listview;
	public static ProgressBar loading;
	public static LinearLayout retry;
	public static Button btnReload;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_atlit, container, false);

		tambah = (ImageView) rootView.findViewById(R.id.tambah);
		back = (ImageView) rootView.findViewById(R.id.back);
		listview = (ListView) rootView.findViewById(R.id.listview);
		loading = (ProgressBar) rootView.findViewById(R.id.pgbarLoading);
		retry = (LinearLayout) rootView.findViewById(R.id.loadMask);
		btnReload = (Button) rootView.findViewById(R.id.btnReload);
		btnReload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//((MainActivity) getActivity()).LoadDataAtlit();
			}
		});

		//listview.setAdapter(MainActivity.atlitAdapter);

		listview.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public boolean onLoadMore(int page, int totalItemsCount) {

				//((MainActivity) getActivity()).LoadDataAtlit();

				return true;
			}
		});

		tambah.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//((MainActivity) getActivity()).EditAtlit(null);
			}
		});
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).displayView(0);
			}
		});

		//((MainActivity) getActivity()).LoadDataAtlit();
		return rootView;
	}
}
