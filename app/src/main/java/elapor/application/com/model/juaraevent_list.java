package elapor.application.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class juaraevent_list implements Parcelable {

	ArrayList<juaraevent> listData;
	
	public juaraevent_list(ArrayList<juaraevent> listData) {
		this.listData = listData;
	}
	
	@SuppressWarnings("unchecked")
	public juaraevent_list(Parcel parcel) {
		this.listData = parcel.readArrayList(null);
	}
	
	public ArrayList<juaraevent> getListData() {
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
    public static Creator<juaraevent_list> CREATOR = new Creator<juaraevent_list>() {

        @Override
        public juaraevent_list createFromParcel(Parcel source) {
            return new juaraevent_list(source);
        }

        @Override
        public juaraevent_list[] newArray(int size) {
            return new juaraevent_list[size];
        }

    };

}


