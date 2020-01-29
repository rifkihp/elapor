package elapor.application.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class apply_list implements Parcelable {

	ArrayList<apply> listData;
	
	public apply_list(ArrayList<apply> listData) {
		this.listData = listData;
	}
	
	@SuppressWarnings("unchecked")
	public apply_list(Parcel parcel) {
		this.listData = parcel.readArrayList(null);
	}
	
	public ArrayList<apply> getListData() {
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
    public static Creator<apply_list> CREATOR = new Creator<apply_list>() {

        @Override
        public apply_list createFromParcel(Parcel source) {
            return new apply_list(source);
        }

        @Override
        public apply_list[] newArray(int size) {
            return new apply_list[size];
        }

    };

}


