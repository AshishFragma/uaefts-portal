package com.uaefts.portal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class FileProcessingService {

    // Inject directory paths from application.properties
    @Value("${file.incoming.directory}")
    private String incomingDirectory;

    @Value("${file.processing.folder}")
    private String processingFolder;

    @Value("${file.processed.folder}")
    private String processedFolder;

    @Value("${file.exception.folder}")
    private String exceptionFolder;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // Adjustable based on system resources

    // Run every 10 seconds
   // @Scheduled(fixedRate = 1000000)
    
    public void monitorDirectory() {
    	System.out.println("checking files");
        File directory = new File(incomingDirectory);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".FTR") || name.endsWith(".SHA"));

        if (files != null && files.length > 0) {
            processFiles(files);
        }
    }

    private void processFiles(File[] files) {
        // Match files by name, assuming .FTR and .sha have the same base name
        for (File file : files) {
            if (file.getName().endsWith(".FTR")) {
                String baseName = file.getName().replace(".FTR", "");
                File shaFile = new File(file.getParent(), baseName + ".FTR.SHA");

                if (shaFile.exists()) {
                    executorService.submit(() -> processFilePair(file, shaFile));
                }
            }
        }
    }

    private void processFilePair(File ftrFile, File shaFile) {
        // Move both files to the processing folder
        try {
        	System.out.println("inside processFilePair- ftrFile"+processingFolder+" ftr -"+ftrFile+"  sha file "+shaFile);
            moveFileToFolder(ftrFile, processingFolder);
            moveFileToFolder(shaFile, processingFolder);

            // Process the files
            boolean isSuccess = compareFiles(ftrFile, shaFile);

            if (isSuccess) {
                // Move to the processed folder after successful processing
                moveFileToFolder(new File(processingFolder, ftrFile.getName()), processedFolder);
                moveFileToFolder(new File(processingFolder, shaFile.getName()), processedFolder);
                System.out.println("Files successfully processed: " + ftrFile.getName());
            } else {
                // Move to the exception folder if there is a mismatch
                moveFileToFolder(new File(processingFolder, ftrFile.getName()), exceptionFolder);
                moveFileToFolder(new File(processingFolder, shaFile.getName()), exceptionFolder);
                System.err.println("File mismatch: " + ftrFile.getName());
            }
        } catch (IOException e) {
            // In case of an error, move files to the exception folder
            try {
                moveFileToFolder(new File(processingFolder, ftrFile.getName()), exceptionFolder);
                moveFileToFolder(new File(processingFolder, shaFile.getName()), exceptionFolder);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private boolean compareFiles(File ftrFile, File shaFile) throws IOException {
    	
    	System.out.println("inside compareFiles");
        // Read .ftr file content and convert to Base64
        byte[] ftrContent = Files.readAllBytes(new File(processingFolder, ftrFile.getName()).toPath());
        String ftrBase64 = Base64.getEncoder().encodeToString(ftrContent);

        // Read .sha file content
        String shaContent = new String(Files.readAllBytes(new File(processingFolder, shaFile.getName()).toPath())).trim();

        System.out.println("ftr base 64 string-"+ ftrBase64);
        System.out.println("sha content        "+ shaContent);
        // Compare .ftr Base64 content with .sha file content
        return ftrBase64.equals(shaContent);
    }

    private void moveFileToFolder(File file, String destinationFolder) throws IOException {
    	System.out.println("moveFileToFolder");
        File destinationDir = new File(destinationFolder);
        if (!destinationDir.exists()) {
            destinationDir.mkdirs(); // Ensure the destination folder exists
        }

        Path destinationPath = new File(destinationFolder, file.getName()).toPath();
        Files.move(file.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }
}

