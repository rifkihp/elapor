package elapor.application.com.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class banner_list implements Parcelable {

	ArrayList<banner> listData;
	
	public banner_list(ArrayList<banner> listData) {
		this.listData = listData;
	}
	
	@SuppressWarnings("unchecked")
	public banner_list(Parcel parcel) {
		this.listData = parcel.readArrayList(null);
	}
	
	public ArrayList<banner> getListData() {
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
    public static Creator<banner_list> CREATOR = new Creator<banner_list>() {

        @Override
        public banner_list createFromParcel(Parcel source) {
            return new banner_list(source);
        }

        @Override
        public banner_list[] newArray(int size) {
            return new banner_list[size];
        }

    };

}


