package org.mattrr78.sparsefetchdemo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Convert 5000 records CSV generated from http://eforexcel.com/wp/downloads-16-sample-csv-files-data-sets-for-testing/
 * Changes:
 *      Convert date to ISO date
 *      Convert time to ISO time
 *      Regenerate IDS because they duplicate
 */
public class EmployeeFileConverter {
    private static final DateTimeFormatter FILE_DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");
    private static final DateTimeFormatter FILE_TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm:ss a");

    private int nextId = 1;

    public static void main(String[] args) throws Exception {
        if (args.length == 0)  {
            System.err.println("Full path to file argument required.");
            System.exit(1);
        }

        new EmployeeFileConverter().convertFile(args[0]);
    }

    public void convertFile(String inputFilename) throws IOException {
        File inputFile = new File(inputFilename);
        List<String> lines = Files.readAllLines(inputFile.toPath());

        File outputFile = new File(inputFile.getParentFile(), "converted_" + inputFile.getName());
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            for (String line : lines) {
                String convertedLine = convert(line);
                writer.println(convertedLine);
            }
        }
    }

    private String convert(String line) {
        String[] fields = line.split(",");
        fields[0] = String.valueOf(nextId++); // IDS are duplicated in the file.
        fields[10] = convertDate(fields[10]);
        fields[11] = convertTime(fields[11]);
        fields[14] = convertDate(fields[14]);
        return String.join(",", fields);
    }

    private String convertDate(String field) {
        LocalDate date = LocalDate.parse(field, FILE_DATE_FORMATTER);
        String convertedDate = date.format(DateTimeFormatter.ISO_DATE);
        return convertedDate;
    }

    private String convertTime(String field) {
        LocalTime time = LocalTime.parse(field, FILE_TIME_FORMATTER);
        String convertedTime = time.format(DateTimeFormatter.ISO_TIME);
        return convertedTime;
    }
}