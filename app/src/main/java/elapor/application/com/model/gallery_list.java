package elapor.application.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class gallery_list implements Parcelable {

	ArrayList<gallery> listData;
	
	public gallery_list(ArrayList<gallery> listData) {
		this.listData = listData;
	}
	
	@SuppressWarnings("unchecked")
	public gallery_list(Parcel parcel) {
		this.listData = parcel.readArrayList(null);
	}
	
	public ArrayList<gallery> getListData() {
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
    public static Creator<gallery_list> CREATOR = new Creator<gallery_list>() {

        @Override
        public gallery_list createFromParcel(Parcel source) {
            return new gallery_list(source);
        }

        @Override
        public gallery_list[] newArray(int size) {
            return new gallery_list[size];
        }

    };

}


