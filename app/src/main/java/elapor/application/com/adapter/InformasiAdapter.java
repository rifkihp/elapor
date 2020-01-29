package elapor.application.com.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import elapor.application.com.libs.CommonUtilities;
import elapor.application.com.elapor.MainActivity;
import elapor.application.com.libs.ResizableImageView;
import elapor.application.com.model.informasi;

import java.util.ArrayList;

import customfonts.MyTextView;
import elapor.application.com.elapor.R;

public class InformasiAdapter extends BaseAdapter {

	ArrayList<informasi> listDataInformasi = new ArrayList<>();
	Context context;

	public InformasiAdapter(Context context, ArrayList<informasi> listDataInformasi) {
		this.context = context;
		this.listDataInformasi = listDataInformasi;
	}

	public void UpdateInformasiAdapter(ArrayList<informasi> listDataInformasi) {
		this.listDataInformasi = listDataInformasi;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return listDataInformasi.size();
	}

	@Override
	public Object getItem(int position) {
		return listDataInformasi.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		public ResizableImageView image;
		ImageView btn_play;
		public MyTextView laporan;
		public MyTextView kategori;
		public MyTextView tanggal;
		public MyTextView pic;
		public MyTextView area;
		public MyTextView no_temuan;

		public MyTextView menunggu;
		public MyTextView proses;
		public MyTextView selesai;

		public int position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {

		final ViewHolder view;
		LayoutInflater inflator =  LayoutInflater.from(parent.getContext());
		if(convertView==null) {
			view = new ViewHolder();
			convertView = inflator.inflate(R.layout.informasi_item_list, null);

			view.image      = convertView.findViewById(R.id.image);
			view.btn_play   = convertView.findViewById(R.id.btn_play);
			view.laporan    = convertView.findViewById(R.id.laporan);
			view.kategori   = convertView.findViewById(R.id.kategori);
			view.tanggal    = convertView.findViewById(R.id.tanggal);
			view.pic        = convertView.findViewById(R.id.pic);
			view.area       = convertView.findViewById(R.id.area);
			view.no_temuan  = convertView.findViewById(R.id.no_temuan);
			view.menunggu   = convertView.findViewById(R.id.menunggu);
			view.proses     = convertView.findViewById(R.id.proses);
			view.selesai    = convertView.findViewById(R.id.selesai);

			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}

		final informasi info = listDataInformasi.get(position);
		view.position = listDataInformasi.indexOf(info);

		String server = CommonUtilities.SERVER_URL;
		String url = server+"/adminweb/reports/image_laporan_android.php?id="+info.getId();
		MainActivity.imageLoader.displayImage(url, view.image, MainActivity.imageOptions);

		view.btn_play.setVisibility(info.getTipe().equalsIgnoreCase("V")?View.VISIBLE:View.GONE);

		view.laporan.setText(info.getKonten());
		view.kategori.setText(info.getJudul());
		view.tanggal.setText(info.getTanggal());
		view.pic.setText("PIC: "+info.getPic());
		view.area.setText("Area: "+info.getHeader());
		view.no_temuan.setText("No. Temuan: "+info.getNo_temuan());

		view.menunggu.setVisibility(info.getStatus().equalsIgnoreCase("M") || info.getStatus().equalsIgnoreCase("0")?View.VISIBLE:View.GONE);
		view.proses.setVisibility(info.getStatus().equalsIgnoreCase("P")?View.VISIBLE:View.GONE);
		view.selesai.setVisibility(info.getStatus().equalsIgnoreCase("D")?View.VISIBLE:View.GONE);


		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View convertView) {
				((MainActivity) parent.getContext()).openDetailInformasi(info);
			}
		});

		return convertView;
	}
}
