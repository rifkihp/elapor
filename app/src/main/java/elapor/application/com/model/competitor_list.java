package elapor.application.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class competitor_list implements Parcelable {

	ArrayList<competitor> listData;
	
	public competitor_list(ArrayList<competitor> listData) {
		this.listData = listData;
	}
	
	@SuppressWarnings("unchecked")
	public competitor_list(Parcel parcel) {
		this.listData = parcel.readArrayList(null);
	}
	
	public ArrayList<competitor> getListData() {
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
    public static Creator<competitor_list> CREATOR = new Creator<competitor_list>() {

        @Override
        public competitor_list createFromParcel(Parcel source) {
            return new competitor_list(source);
        }

        @Override
        public competitor_list[] newArray(int size) {
            return new competitor_list[size];
        }

    };

}


