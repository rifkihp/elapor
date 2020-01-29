package elapor.application.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class kelas_list implements Parcelable {

	ArrayList<kelas> listData;

	public kelas_list(ArrayList<kelas> listData) {
		this.listData = listData;
	}

	@SuppressWarnings("unchecked")
	public kelas_list(Parcel parcel) {
		this.listData = parcel.readArrayList(null);
	}
	
	public ArrayList<kelas> getListData() {
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
    public static Creator<kelas_list> CREATOR = new Creator<kelas_list>() {

        @Override
        public kelas_list createFromParcel(Parcel source) {
            return new kelas_list(source);
        }

        @Override
        public kelas_list[] newArray(int size) {
            return new kelas_list[size];
        }

    };

}


