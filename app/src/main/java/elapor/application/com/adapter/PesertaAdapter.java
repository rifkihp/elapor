package elapor.application.com.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alexzh.circleimageview.CircleImageView;

import java.util.ArrayList;

import customfonts.MyTextView;
import elapor.application.com.elapor.MainActivity;
import elapor.application.com.elapor.R;
import elapor.application.com.libs.CommonUtilities;
import elapor.application.com.model.peserta;

public class PesertaAdapter extends BaseAdapter {

	ArrayList<peserta> listDataPeserta = new ArrayList<>();
	Context context;

	public PesertaAdapter(Context context, ArrayList<peserta> listDataPeserta) {
		this.context = context;
		this.listDataPeserta = listDataPeserta;
	}

	public void UpdatePesertaAdapter(ArrayList<peserta> listDataPeserta) {
		this.listDataPeserta = listDataPeserta;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return listDataPeserta.size();
	}

	@Override
	public Object getItem(int position) {
		return listDataPeserta.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		public CircleImageView image;
		public MyTextView nama;
		public MyTextView lokasi_kerja;
		public MyTextView laki_laki;
		public MyTextView perempuan;

		public int position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {

		final ViewHolder view;
		LayoutInflater inflator =  LayoutInflater.from(parent.getContext());
		if(convertView==null) {
			view = new ViewHolder();
			convertView = inflator.inflate(R.layout.peserta_item_list, null);

			view.image         = (CircleImageView) convertView.findViewById(R.id.image);
			view.image.setLayerType(View.LAYER_TYPE_HARDWARE, null);
			view.nama          = (MyTextView) convertView.findViewById(R.id.nama);
			view.lokasi_kerja = (MyTextView) convertView.findViewById(R.id.lokasi_kerja);
			view.laki_laki     = (MyTextView) convertView.findViewById(R.id.laki_laki);
			view.perempuan     = (MyTextView) convertView.findViewById(R.id.perempuan);

			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}

		final peserta info = listDataPeserta.get(position);
		view.position = listDataPeserta.indexOf(info);

		String server = CommonUtilities.SERVER_URL;
		String url = server+"/uploads/member/"+info.getPhoto();
		MainActivity.imageLoader.displayImage(url, view.image, MainActivity.imageOptionsUser);

		view.nama.setText(info.getNama());
		view.lokasi_kerja.setText(info.getLokasi_kerja());
		view.laki_laki.setVisibility(info.getJenis_kelamin().equalsIgnoreCase("Laki-laki")?View.VISIBLE:View.GONE);
		view.perempuan.setVisibility(info.getJenis_kelamin().equalsIgnoreCase("Perempuan")?View.VISIBLE:View.GONE);

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View convertView) {

			}
		});

		return convertView;
	}
}
