package elapor.application.com.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import customfonts.MyTextView;
import elapor.application.com.elapor.MainActivity;

import elapor.application.com.libs.EndlessScrollListener;

import elapor.application.com.elapor.R;

public class InformasiFragment extends Fragment {

	public static SwipeRefreshLayout swipeRefreshLayout;
	public static MyTextView buatlaporan;
	public static ListView listview;
	public static ProgressBar loading;
	public static LinearLayout retry;
	public static Button btnReload;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_informasi, container, false);

		swipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
		buatlaporan = rootView.findViewById(R.id.buatlaporan);
		listview = rootView.findViewById(R.id.listview);
		loading = rootView.findViewById(R.id.pgbarLoading);
		retry = rootView.findViewById(R.id.loadMask);
		btnReload = rootView.findViewById(R.id.btnReload);
		btnReload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				((MainActivity) getActivity()).LoadDataInformasi();
			}
		});

		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				swipeRefreshLayout.setRefreshing(false);
				((MainActivity) getActivity()).ReloadDataInformasi();
			}
		});

		listview.setAdapter(MainActivity.informasiAdapter);

		listview.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public boolean onLoadMore(int page, int totalItemsCount) {

				((MainActivity) getActivity()).LoadDataInformasi();

				return true;
			}
		});

		buatlaporan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).buatLaporan();
			}
		});

		((MainActivity) getActivity()).LoadDataInformasi();
		return rootView;
	}
}
