package com.sgbd.util;

public enum ContentType {
    JSON("application/json"),
    HTML("text/html");

    private String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}
