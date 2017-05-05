package com.sgbd.dto;

public class ImageDTO {

    private String imageName;
    private String imageURI;

    public ImageDTO() {
    }

    public ImageDTO(String imageName, String imageURI) {
        this.imageName = imageName;
        this.imageURI = imageURI;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }
}
