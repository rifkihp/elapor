package elapor.application.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class divisi_list implements Parcelable {

	ArrayList<divisi> listData;

	public divisi_list(ArrayList<divisi> listData) {
		this.listData = listData;
	}

	@SuppressWarnings("unchecked")
	public divisi_list(Parcel parcel) {
		this.listData = parcel.readArrayList(null);
	}
	
	public ArrayList<divisi> getListData() {
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
    public static Creator<divisi_list> CREATOR = new Creator<divisi_list>() {

        @Override
        public divisi_list createFromParcel(Parcel source) {
            return new divisi_list(source);
        }

        @Override
        public divisi_list[] newArray(int size) {
            return new divisi_list[size];
        }

    };

}


