package elapor.application.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class informasi_list implements Parcelable {

	ArrayList<informasi> listData;
	
	public informasi_list(ArrayList<informasi> listData) {
		this.listData = listData;
	}
	
	@SuppressWarnings("unchecked")
	public informasi_list(Parcel parcel) {
		this.listData = parcel.readArrayList(null);
	}
	
	public ArrayList<informasi> getListData() {
		return this.listData;
	}
	
	@Override
    public int describeContents() {
        return 0;
    }

    // Required method to write to Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(listData);
    }

    // Method to recreate a Question from a Parcel
    public static Creator<informasi_list> CREATOR = new Creator<informasi_list>() {

        @Override
        public informasi_list createFromParcel(Parcel source) {
            return new informasi_list(source);
        }

        @Override
        public informasi_list[] newArray(int size) {
            return new informasi_list[size];
        }

    };

}


