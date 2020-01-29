package elapor.application.com.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class event_list implements Parcelable {

	ArrayList<event> listData;
	
	public event_list(ArrayList<event> listData) {
		this.listData = listData;
	}
	
	@SuppressWarnings("unchecked")
	public event_list(Parcel parcel) {
		this.listData = parcel.readArrayList(null);
	}
	
	public ArrayList<event> getListData() {
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
    public static Creator<event_list> CREATOR = new Creator<event_list>() {

        @Override
        public event_list createFromParcel(Parcel source) {
            return new event_list(source);
        }

        @Override
        public event_list[] newArray(int size) {
            return new event_list[size];
        }

    };

}


