package com.google.ar.sceneform.samples.hellosceneform;

public class itemInformation {

    private int _id;
    private String _itemName;
    private String _itemLocation;
    private String _itemLabel;
    private String _itemCoordinates;

    private int _buttonCount;


    public itemInformation(){

    }


    public itemInformation(String itemName, String itemLocation, String itemLabel, int buttonCount, String itemCoordinates) {
        this._itemName = itemName;
        this._itemLocation = itemLocation;
        this._itemLabel = itemLabel;
        this._buttonCount = buttonCount;
        this._itemCoordinates = itemCoordinates;
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



    public int get_buttonCount() {
        return _buttonCount;
    }

    public void set_buttonCount(int _buttonCount) {
        this._buttonCount = _buttonCount;
    }



    public String get_itemCoordinates() {
        return _itemCoordinates;
    }

    public void set_itemCoordinates(String _itemCoordinates) {
        this._itemCoordinates = _itemCoordinates;
    }
}


