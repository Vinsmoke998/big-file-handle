package com.example.demo.repository.file;

import com.example.demo.model.entity.file.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<UploadFile, String> {
}
