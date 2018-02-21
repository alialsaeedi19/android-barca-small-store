package hu.ait.barcaListHW.data;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BarcaItem extends RealmObject implements Parcelable {

    private String itemName;
    private String itemCategory;
    private double itemPrice;
    private String itemDescription;


    private boolean purchased;

    @PrimaryKey
    private String itemID;


    public BarcaItem() {
    }

    public BarcaItem(String itemName, boolean done) {
        this.itemName = itemName;
        this.purchased = done;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public String getItemID() {
        return itemID;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(itemID);
        out.writeString(itemName);
        out.writeString(itemCategory);
        out.writeString(itemDescription);
        out.writeDouble(itemPrice);
    }
    public static final Parcelable.Creator<BarcaItem> CREATOR = new Parcelable.Creator<BarcaItem>() {
        public BarcaItem createFromParcel(Parcel in) {
            return new BarcaItem(in);
        }

        public BarcaItem[] newArray(int size) {
            return new BarcaItem[size];
        }
    };
    private BarcaItem(Parcel in) {
        itemPrice = in.readDouble();
        itemID = in.readString();
        itemName = in.readString();
        itemCategory = in.readString();
        itemDescription = in.readString();

    }
}
