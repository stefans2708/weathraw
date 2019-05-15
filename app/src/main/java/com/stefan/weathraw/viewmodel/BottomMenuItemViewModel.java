package com.stefan.weathraw.viewmodel;

import com.stefan.weathraw.model.BottomMenuItem;

public class BottomMenuItemViewModel extends BaseViewModel {

    private String itemName;

    public BottomMenuItemViewModel(BottomMenuItem item) {
        this.itemName = item.getItemTitle();
    }

    public String getItemName() {
        return itemName;
    }

}
