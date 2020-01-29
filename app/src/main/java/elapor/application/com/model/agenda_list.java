package elapor.application.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class agenda_list implements Parcelable {

	ArrayList<agenda> listData;
	
	public agenda_list(ArrayList<agenda> listData) {
		this.listData = listData;
	}
	
	@SuppressWarnings("unchecked")
	public agenda_list(Parcel parcel) {
		this.listData = parcel.readArrayList(null);
	}
	
	public ArrayList<agenda> getListData() {
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
    public static Creator<agenda_list> CREATOR = new Creator<agenda_list>() {

        @Override
        public agenda_list createFromParcel(Parcel source) {
            return new agenda_list(source);
        }

        @Override
        public agenda_list[] newArray(int size) {
            return new agenda_list[size];
        }

    };

}


