package elapor.application.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class hasil_list implements Parcelable {

	ArrayList<hasil> listData;
	
	public hasil_list(ArrayList<hasil> listData) {
		this.listData = listData;
	}
	
	@SuppressWarnings("unchecked")
	public hasil_list(Parcel parcel) {
		this.listData = parcel.readArrayList(null);
	}
	
	public ArrayList<hasil> getListData() {
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
    public static Creator<hasil_list> CREATOR = new Creator<hasil_list>() {

        @Override
        public hasil_list createFromParcel(Parcel source) {
            return new hasil_list(source);
        }

        @Override
        public hasil_list[] newArray(int size) {
            return new hasil_list[size];
        }

    };

}


