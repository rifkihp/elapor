package elapor.application.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class peserta_list implements Parcelable {

	ArrayList<peserta> listData;

	public peserta_list(ArrayList<peserta> listData) {
		this.listData = listData;
	}

	@SuppressWarnings("unchecked")
	public peserta_list(Parcel parcel) {
		this.listData = parcel.readArrayList(null);
	}
	
	public ArrayList<peserta> getListData() {
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
    public static Creator<peserta_list> CREATOR = new Creator<peserta_list>() {

        @Override
        public peserta_list createFromParcel(Parcel source) {
            return new peserta_list(source);
        }

        @Override
        public peserta_list[] newArray(int size) {
            return new peserta_list[size];
        }

    };

}


