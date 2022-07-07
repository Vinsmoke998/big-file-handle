package com.example.demo.service.file.impl;

import com.example.demo.manager.FileManager;
import com.example.demo.model.entity.file.UploadFile;
import com.example.demo.model.entity.role.Role;
import com.example.demo.model.request.FileQueueRequest;
import com.example.demo.model.type.UploadFileStatus;
import com.example.demo.repository.file.FileRepository;
import com.example.demo.repository.role.RoleRepository;
import com.example.demo.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final RoleRepository roleRepository;

    private final FileManager fileManager;

    private final FileRepository fileRepository;

    @Value("${upload.file.path}")
    private String rootFilePath;

    @Override
    public ByteArrayInputStream convertRoleToCSV() {
        List<Role> roles = roleRepository.findAll();
        if (CollectionUtils.isNotEmpty(roles)) {
            return fileManager.convertRoleToCSV(roles);
        }
        return null;
    }

//    @Async
    @Override
    @Transactional(rollbackOn = Exception.class)
    public List<Role> convertCsvToRoles(MultipartFile file) {
        try {
            List<Role> roles = fileManager.convertCsvToRoles(file.getInputStream());
            roleRepository.saveAll(roles);
        } catch (IOException | ParseException | InterruptedException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Transactional
    @Override
    public void uploadFile(MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        String filePath = rootFilePath + uuid + "\\";
        fileManager.checkUploadPath(filePath);
        UploadFile uploadFile = new UploadFile();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        uploadFile.setFileName(fileName);
        uploadFile.setStatus(UploadFileStatus.IN_PROGRESS);
        uploadFile.setType(file.getContentType());
        uploadFile.setFilePath(filePath);
        fileRepository.save(uploadFile);

        try {
            fileManager.sendFileMessage(new FileQueueRequest(uploadFile.getId(), fileName, filePath, file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
