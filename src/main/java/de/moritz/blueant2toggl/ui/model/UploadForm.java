package de.moritz.blueant2toggl.ui.model;

import org.springframework.web.multipart.MultipartFile;

public class UploadForm {
    private String        userName;
    private String        password;
    private MultipartFile uploadFile;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MultipartFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(MultipartFile uploadFile) {
        this.uploadFile = uploadFile;
    }
}
