package com.example.demo.manager;

import com.example.demo.common.Constants;
import com.example.demo.model.entity.file.UploadFile;
import com.example.demo.model.entity.role.Role;
import com.example.demo.model.request.FileQueueRequest;
import com.example.demo.model.type.UploadFileStatus;
import com.example.demo.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.*;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileManager {

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final JmsTemplate jmsTemplate;

    private final FileRepository fileRepository;

    public boolean hasCSVFormat(MultipartFile file) {
        if (!Constants.CSV_TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public void checkUploadPath(String uploadFilePath) {
        File folder = new File(uploadFilePath);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public List<Role> convertCsvToRoles(InputStream is) throws ParseException, InterruptedException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            List<Role> roles = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                Role role = new Role();
                role.setCode(csvRecord.get("Code"));
                role.setRoleName(csvRecord.get("RoleName"));
                role.setCreatedDate(df.parse(csvRecord.get("CreatedDate")));
                role.setModifiedDate(df.parse(csvRecord.get("ModifiedDate")));
                roles.add(role);
            }
            return roles;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    public ByteArrayInputStream convertRoleToCSV(List<Role> roles) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (Role role : roles) {
                List<String> data = Arrays.asList(
                        String.valueOf(role.getId()),
                        role.getCode(),
                        role.getRoleName(),
                        role.getCreatedDate().toString(),
                        role.getModifiedDate().toString()
                );
                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }

    @Async
    @JmsListener(destination = "FileBytesQueue", containerFactory = "myFactory")
    public void receiveMessage(FileQueueRequest fileQueueRequest) throws Exception {
        try {
            byte[] bytes = fileQueueRequest.getBytes();
            Path path = Paths.get(fileQueueRequest.getFilePath() + fileQueueRequest.getFileName());
            Files.write(path, bytes);
            UploadFile uploadFile = fileRepository.findById(fileQueueRequest.getFileUUID()).get();
            if (uploadFile == null) {
                throw new Exception("file not found!");
            }
            uploadFile.setStatus(UploadFileStatus.UPLOADED);
            fileRepository.save(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendFileMessage(FileQueueRequest fileQueueRequest) {
        jmsTemplate.convertAndSend("FileBytesQueue", fileQueueRequest);
    }

}
