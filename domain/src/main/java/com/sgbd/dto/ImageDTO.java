package com.sgbd.dto;

public class ImageDTO {

    private String imageName;
    private String imageURI;
    private String iconPathname;

    public ImageDTO() {
    }

    public ImageDTO(String iconPathname, String imageName, String imageURI) {
        this.imageName = imageName;
        this.imageURI = imageURI;
        this.iconPathname = iconPathname;
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

    public String getIconPathname() {
        return iconPathname;
    }

    public void setIconPathname(String iconPathname) {
        this.iconPathname = iconPathname;
    }
}
