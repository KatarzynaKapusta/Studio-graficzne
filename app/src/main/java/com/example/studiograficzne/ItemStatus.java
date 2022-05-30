package com.example.studiograficzne;

public enum ItemStatus {
    NOTOWNED(0),
    OWNED(1),
    HIDDENOWNED(2),
    PREVIEW(3),
    HIDDENFORPREVIEW(4);

    public final int value;

    ItemStatus(int value) {
        this.value = value;
    }
}


