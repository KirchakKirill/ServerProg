package com.example.ServerProgLab34.Services;

import com.example.ServerProgLab34.Data.ContactData;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class CsvExportService {
    @Value("${app.csv.file-path}")
    private String csvFilePath;

    public void writeToCsv(ContactData contactData) throws Exception {
        Path path = Paths.get(csvFilePath);
        boolean fileExists = Files.exists(path);
        String[] HEADERS = {"Name", "Email","Subject","Msg"};
        try (Writer writer = Files.newBufferedWriter(
                path,
                fileExists ? StandardOpenOption.APPEND : StandardOpenOption.CREATE,
                StandardOpenOption.WRITE)) {

            if(!fileExists){
                CSVWriter csvWriter = new CSVWriter(writer);
                csvWriter.writeNext(HEADERS);
            }

            CSVWriter csvWriter = new CSVWriter(writer,
                    ',',  // разделитель
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);


            String[] data = new String[]{
                    contactData.getName(),
                    contactData.getEmail(),
                    contactData.getSubject(),
                    contactData.getMsg()
            };

            csvWriter.writeNext(data);
            csvWriter.flush();
        }
    }
}