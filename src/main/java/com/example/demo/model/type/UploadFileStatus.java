package com.example.demo.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UploadFileStatus {

    IN_PROGRESS("in_process"),
    UPLOADED("uploaded");

    private final String value;

    public static UploadFileStatus findByValue(String value) {
        if (value == null) return null;

        for (UploadFileStatus type : UploadFileStatus.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }

        return null;
    }
}