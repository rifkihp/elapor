
package elapor.application.com.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import customfonts.MyTextView;
import elapor.application.com.elapor.ProsesRegistrasiActivity;
import elapor.application.com.elapor.R;

public class RegistrasiSuccessFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_registrasi_success, container, false);

		MyTextView selesai = (MyTextView) rootView.findViewById(R.id.selesai);

		selesai.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				((ProsesRegistrasiActivity) getActivity()).app_close();
			}
		});

		return rootView;
	}
	
}
