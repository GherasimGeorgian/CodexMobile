package service;

import android.os.Parcel;
import android.os.Parcelable;

public class ClientParcelable implements Parcelable{
    private int mData;

    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<ClientParcelable> CREATOR = new Parcelable.Creator<ClientParcelable>() {
        public ClientParcelable createFromParcel(Parcel in) {
            return new ClientParcelable(in);
        }

        public ClientParcelable[] newArray(int size) {
            return new ClientParcelable[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private ClientParcelable(Parcel in) {
        mData = in.readInt();
    }
}