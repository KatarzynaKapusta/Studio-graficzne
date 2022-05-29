package com.example.studiograficzne;

public enum ItemStatus {
    NOTOWNED(4),
    OWNED(1),
    PREVIEW(2);

    public final int value;

    ItemStatus(int value) {
        this.value = value;
    }
}


