package elapor.application.com.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import elapor.application.com.elapor.MainActivity;
import elapor.application.com.libs.EndlessScrollListener;
import elapor.application.com.elapor.R;

public class EventFragment extends Fragment {

	public static ListView listview;
	public static ProgressBar loading;
	public static LinearLayout retry;
	public static Button btnReload;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_event, container, false);

		listview = (ListView) rootView.findViewById(R.id.listview);
		loading = (ProgressBar) rootView.findViewById(R.id.pgbarLoading);
		retry = (LinearLayout) rootView.findViewById(R.id.loadMask);
		btnReload = (Button) rootView.findViewById(R.id.btnReload);
		btnReload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//((MainActivity) getActivity()).LoadDataEvent();
			}
		});

		//listview.setAdapter(MainActivity.elaporAdapter);

		listview.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public boolean onLoadMore(int page, int totalItemsCount) {

				//((MainActivity) getActivity()).LoadDataEvent();

				return true;
			}
		});

		//((MainActivity) getActivity()).LoadDataEvent();

		return rootView;
	}
}
