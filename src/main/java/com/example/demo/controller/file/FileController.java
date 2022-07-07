package com.example.demo.controller.file;

import com.example.demo.common.Endpoints;
import com.example.demo.manager.FileManager;
import com.example.demo.model.response.ResponseMessage;
import com.example.demo.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    private final FileManager fileManager;

    @Value("${file.export.name}")
    private String fileName;

    @GetMapping(Endpoints.URL_FILE_EXPORT)
    public ResponseEntity<Resource> exportFile() {
        InputStreamResource file = new InputStreamResource(fileService.convertRoleToCSV());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

    @PostMapping(Endpoints.URL_FILE_IMPORT)
    public ResponseEntity<ResponseMessage> importFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        if (fileManager.hasCSVFormat(file)) {
            try {
                fileService.convertCsvToRoles(file);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }
        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

    @PostMapping(Endpoints.URL_FILE_UPLOAD)
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            fileService.uploadFile(file);
            message = "Processing the file uploading: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    // remove comment #spring.servlet.multipart.enabled=false to enable this api
    @PostMapping(Endpoints.URL_FILE_UPLOAD + "/v2")
    public ResponseEntity<String> upload(HttpServletRequest request) {
        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (!isMultipart) {
                // Inform user about invalid request
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Not a multipart request.");
            }

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload();

            // Parse the request
            FileItemIterator iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (!item.isFormField()) {
                    String filename = item.getName();
                    // Process the input stream
                    OutputStream out = new FileOutputStream(filename);
                    IOUtils.copy(stream, out);
                    stream.close();
                    out.close();
                }
            }
        } catch (FileUploadException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("File upload error");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Internal server IO error");
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Success");
    }

}
