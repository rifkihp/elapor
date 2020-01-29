package elapor.application.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class mendali_list implements Parcelable {

	ArrayList<mendali> listData;
	
	public mendali_list(ArrayList<mendali> listData) {
		this.listData = listData;
	}
	
	@SuppressWarnings("unchecked")
	public mendali_list(Parcel parcel) {
		this.listData = parcel.readArrayList(null);
	}
	
	public ArrayList<mendali> getListData() {
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
    public static Creator<mendali_list> CREATOR = new Creator<mendali_list>() {

        @Override
        public mendali_list createFromParcel(Parcel source) {
            return new mendali_list(source);
        }

        @Override
        public mendali_list[] newArray(int size) {
            return new mendali_list[size];
        }

    };

}


