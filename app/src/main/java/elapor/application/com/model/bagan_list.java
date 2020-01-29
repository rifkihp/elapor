package elapor.application.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class bagan_list implements Parcelable {

	ArrayList<bagan> listData;
	
	public bagan_list(ArrayList<bagan> listData) {
		this.listData = listData;
	}
	
	@SuppressWarnings("unchecked")
	public bagan_list(Parcel parcel) {
		this.listData = parcel.readArrayList(null);
	}
	
	public ArrayList<bagan> getListData() {
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
    public static Creator<bagan_list> CREATOR = new Creator<bagan_list>() {

        @Override
        public bagan_list createFromParcel(Parcel source) {
            return new bagan_list(source);
        }

        @Override
        public bagan_list[] newArray(int size) {
            return new bagan_list[size];
        }

    };

}


