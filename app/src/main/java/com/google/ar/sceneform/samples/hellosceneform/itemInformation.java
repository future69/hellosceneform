package com.google.ar.sceneform.samples.hellosceneform;

public class itemInformation {

    private int _id;
    private String _itemName;
    private String _itemLocation;
    private String _itemLabel;



    public itemInformation(){

    }

    public itemInformation(String itemName, String itemLocation, String itemLabel) {
        this._itemName = itemName;
        this._itemLocation = itemLocation;
        this._itemLabel = itemLabel;
    }

    public String get_itemName() {
        return _itemName;
    }

    public void set_itemName(String _itemName) {
        this._itemName = _itemName;
    }



    public String get_itemLocation() {
        return _itemLocation;
    }

    public void set_itemLocation(String _itemLocation) {
        this._itemLocation = _itemLocation;
    }



    public String get_itemLabel() {
        return _itemLabel;
    }

    public void set_itemLabel(String _itemLabel) {
        this._itemLabel = _itemLabel;
    }
}
