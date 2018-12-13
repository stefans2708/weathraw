package com.example.stefan.weathraw.viewmodel;

import com.example.stefan.weathraw.model.BottomMenuItem;

public class BottomMenuItemViewModel extends BaseViewModel {

    public static int ACTION_SETTINGS = 1;
    public static int ACTION_ADD_CITY = 2;
    public static int ACTION_ABOUT = 3;

    private String itemName;

    public BottomMenuItemViewModel(BottomMenuItem item) {
        this.itemName = item.getItemTitle();
    }

    public String getItemName() {
        return itemName;
    }

}
