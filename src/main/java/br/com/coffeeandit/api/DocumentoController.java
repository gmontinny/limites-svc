package br.com.coffeeandit.api;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/documentos")
@Slf4j
public class DocumentoController {

    @Value("${document.bucket-name}")
    private String bucketName;

    AmazonS3 amazonS3;

    public DocumentoController(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @GetMapping("/{filename}")
    public ResponseEntity download(@PathVariable String filename) throws IOException {
        S3Object data = amazonS3.getObject(bucketName, filename);
        S3ObjectInputStream content = data.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(content);
        ByteArrayResource resource = new ByteArrayResource(bytes);
        content.close();
        return ResponseEntity.ok()
                .contentLength(bytes.length)
                .header("Content-Type", "application/octet-stream")
                .header("Content-Disposition", "attachment; filename=\"" +filename+ "\"")
                .body(resource);
    }

    @PostMapping
    public ResponseEntity uploadDocument(MultipartFile file) throws IOException, URISyntaxException {

        String fileName = UUID.randomUUID() + file.getName();
        File tempFile = new File(System.getProperty("java.io.tmpdir")+ File.separatorChar + fileName);
        file.transferTo(tempFile);

        amazonS3.putObject(bucketName, fileName, tempFile);

        tempFile.deleteOnExit();

        return ResponseEntity.created(
                amazonS3.generatePresignedUrl(bucketName, fileName, convertToDateByInstant(LocalDate.now().plusDays(2))).toURI()).build();
    }

    private Date convertToDateByInstant(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());

    }

}

