package com.example.demo.model.type.converter;

import com.example.demo.model.type.UploadFileStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UploadFileStatusConverter implements AttributeConverter<UploadFileStatus, String> {

    @Override
    public String convertToDatabaseColumn(UploadFileStatus uploadFileStatus) {
        if (uploadFileStatus == null) return null;

        return uploadFileStatus.getValue();
    }

    @Override
    public UploadFileStatus convertToEntityAttribute(String value) {
        return UploadFileStatus.findByValue(value);
    }
}