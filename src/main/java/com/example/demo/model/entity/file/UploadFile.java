package com.example.demo.model.entity.file;

import com.example.demo.model.type.UploadFileStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "file")
public class UploadFile {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "file_name")
    private String fileName;

    private String type;

    @Column(name = "file_path")
    private String filePath;

    private UploadFileStatus status;

    @CreationTimestamp
    @DateTimeFormat(pattern = "dd/mm/yyyy")
    @Column(name = "created_date")
    private Date createdDate;

    @UpdateTimestamp
    @DateTimeFormat(pattern = "dd/mm/yyyy")
    @Column(name = "modified_date")
    private Date modifiedDate;
}
