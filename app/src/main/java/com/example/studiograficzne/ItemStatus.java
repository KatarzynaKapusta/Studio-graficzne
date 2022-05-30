package com.example.studiograficzne;

public enum ItemStatus {
<<<<<<< HEAD
    NOTOWNED(0),
    OWNED(1),
    HIDDENOWNED(2),
    PREVIEW(3),
    HIDDENFORPREVIEW(4);
=======
    NOTOWNED(4),
    OWNED(1),
    PREVIEW(2);
>>>>>>> bfcbbad656dd6bfb82ec36cecbeec6b582d1e70c

    public final int value;

    ItemStatus(int value) {
        this.value = value;
    }
}


