package elapor.application.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class cabor_list implements Parcelable {

	ArrayList<cabor> listData;

	public cabor_list(ArrayList<cabor> listData) {
		this.listData = listData;
	}

	@SuppressWarnings("unchecked")
	public cabor_list(Parcel parcel) {
		this.listData = parcel.readArrayList(null);
	}
	
	public ArrayList<cabor> getListData() {
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
    public static Creator<cabor_list> CREATOR = new Creator<cabor_list>() {

        @Override
        public cabor_list createFromParcel(Parcel source) {
            return new cabor_list(source);
        }

        @Override
        public cabor_list[] newArray(int size) {
            return new cabor_list[size];
        }

    };

}


