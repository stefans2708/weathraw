package com.stefan.weathraw.model;

public class BottomMenuItem {

    private int itemType;
    private String itemTitle;

    public BottomMenuItem(int itemType, String itemTitle) {
        this.itemType = itemType;
        this.itemTitle = itemTitle;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
}
