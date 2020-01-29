package elapor.application.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class teaser_list implements Parcelable {

	ArrayList<teaser> listData;
	
	public teaser_list(ArrayList<teaser> listData) {
		this.listData = listData;
	}
	
	@SuppressWarnings("unchecked")
	public teaser_list(Parcel parcel) {
		this.listData = parcel.readArrayList(null);
	}
	
	public ArrayList<teaser> getListData() {
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
    public static Creator<teaser_list> CREATOR = new Creator<teaser_list>() {

        @Override
        public teaser_list createFromParcel(Parcel source) {
            return new teaser_list(source);
        }

        @Override
        public teaser_list[] newArray(int size) {
            return new teaser_list[size];
        }

    };

}


