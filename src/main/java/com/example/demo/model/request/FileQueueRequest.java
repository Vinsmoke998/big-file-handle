package com.example.demo.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileQueueRequest implements Serializable {

    private String fileUUID;

    private String fileName;

    private String filePath;

    private byte[] bytes;
}
