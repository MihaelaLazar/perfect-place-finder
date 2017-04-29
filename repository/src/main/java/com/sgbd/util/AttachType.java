package com.sgbd.util;

/**
 * Created by Lazarm on 4/21/2017.
 */
public enum AttachType {
    PDF("pdf"),
    PNG("png"),
    JPEG("jpg");

    private String attachType;

    AttachType(String attachType) {
        this.attachType = attachType;
    }

    public String getAttachType() {
        return attachType;
    }
}
