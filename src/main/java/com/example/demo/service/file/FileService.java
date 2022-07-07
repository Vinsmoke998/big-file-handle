package com.example.demo.service.file;

import com.example.demo.model.entity.role.Role;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface FileService {

    ByteArrayInputStream convertRoleToCSV();

    List<Role> convertCsvToRoles(MultipartFile file);

    void uploadFile(MultipartFile file);

}
