package com.rakuten.fullstackrecruitmenttest.database;

import  com.rakuten.fullstackrecruitmenttest.storage.StorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class DatabaseLoader implements CommandLineRunner {
    private final EmployeeRepository repository;
    private Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

    @Autowired
    public DatabaseLoader(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) throws Exception {}

    public void writeCsv(String prefix) throws FileNotFoundException {
        StorageProperties properties = new StorageProperties();
        Path rootLocation = Paths.get(properties.getLocation());
        PrintWriter writer = new PrintWriter(new File(rootLocation + "/" + prefix + "_error_records.csv"));
        List<Employee> errorList = this.repository.findAllWrongRecords();
        for(Employee emp: errorList) {
           writer.write(emp.toString());
        }
        writer.close();
    }

    public boolean validate(String[] fields) throws Exception {
        List<Pattern> list = Arrays.asList(
                Pattern.compile("[a-zA-Z]+"),
                Pattern.compile("[\\w-_\\*]+"),
                Pattern.compile("Developer|Senior Developer|Manager|Team Lead|VP|CEO"),
                Pattern.compile("\\d+"),
                Pattern.compile("\\d{4}-\\d{2}-\\d{2}")
        );
        if (fields.length != 5) {
            throw new Exception("Length Not Match");
        } else {
            int index = 0;
            Iterator<Pattern> iter = list.iterator();
            for(Pattern pattern: list) {
                if(!iter.next().matcher(fields[index++]).matches()) {
                    logger.info("Create Wrong Record for Employee: " + fields[0]);
                    if(this.repository.findByName(fields[0]) == null) {
                        this.repository.save(new Employee(fields[0], fields[1], fields[2], fields[3], fields[4], false));
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public void loadFile(MultipartFile file) throws Exception {
        StorageProperties properties = new StorageProperties();
        Path rootLocation = Paths.get(properties.getLocation());
        logger.info("reading file:" + rootLocation + "/" + file.getOriginalFilename());
        BufferedReader br = new BufferedReader(new FileReader(rootLocation + "/" + file.getOriginalFilename()));
        br.readLine();
        String line;
        while((line = br.readLine()) != null) {
            logger.info("line: " + line);
            String[] fields = line.split(",");
            if(validate(fields)) {
                String employeeName = this.repository.findByName(fields[0]);
                if(!fields[0].equals(employeeName)) {
                    logger.info("Create New Record for Employee: " + fields[0]);
                    this.repository.save(new Employee(fields[0], fields[1], fields[2], fields[3], fields[4], true));
                }
            }
        }
        writeCsv(file.getOriginalFilename());
    }

    public void updateRecord(String updateColumn, String name, String updateValue) {
        if (updateColumn.equals("department")) this.repository.updateDepartment(updateValue, name);
        if (updateColumn.equals("designation")) this.repository.updateDesignation(updateValue, name);
        if (updateColumn.equals("salary")) this.repository.updateSalary(updateValue, name);
        if (updateColumn.equals("joinDate")) this.repository.updateJoinDate(updateValue, name);
    }
}
