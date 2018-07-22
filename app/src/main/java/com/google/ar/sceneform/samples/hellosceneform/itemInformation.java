package com.google.ar.sceneform.samples.hellosceneform;

public class itemInformation {

    private int _id;
    private String _itemName;
    private String _itemLocation;
    private String _itemLabel;
    private String _areaLocation;
    private float _pointX;
    private float _pointY;
    private float _pointZ;


    private int _buttonCount;


    public itemInformation(){

    }



    public itemInformation(String itemName, String itemLocation, String itemLabel, String areaLocation, int buttonCount, float pointX, float pointY, float pointZ) {
        this._itemName = itemName;
        this._itemLocation = itemLocation;
        this._itemLabel = itemLabel;
        this._areaLocation = areaLocation;
        this._buttonCount = buttonCount;
        this._pointX = pointX;
        this._pointY = pointY;
        this._pointZ = pointZ;
    }

    public String get_areaLocation() {
        return _areaLocation;
    }

    public void set_areaLocation(String _areaLocation) {
        this._areaLocation = _areaLocation;
    }

    public float get_pointX() {
        return _pointX;
    }

    public void set_pointX(float _pointX) {
        this._pointX = _pointX;
    }

    public float get_pointY() {
        return _pointY;
    }

    public void set_pointY(float _pointY) {
        this._pointY = _pointY;
    }

    public float get_pointZ() {
        return _pointZ;
    }

    public void set_pointZ(float _pointZ) {
        this._pointZ = _pointZ;
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



}


