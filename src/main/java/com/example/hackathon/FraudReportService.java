package com.example.hackathon;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FraudReportService {

    private static final Logger LOGGER = Logger.getLogger(FraudReportService.class.getName());

    // In-memory storage for demo purposes
    // In a real application, this would be replaced with a database
    private static final Map<String, FraudReport> reportDatabase = new HashMap<>();

    public boolean submitFraudReport(FraudReport report) {
        try {
            // Save evidence files to storage
            if (report.getEvidenceFiles() != null && !report.getEvidenceFiles().isEmpty()) {
                List<File> savedFiles = saveEvidenceFiles(report.getId(), report.getEvidenceFiles());
                report.setEvidenceFiles(savedFiles);
            }

            // In a real application, this would save to a database
            reportDatabase.put(report.getId(), report);

            // Log the submission
            LOGGER.info("Fraud report submitted: " + report.getId());

            // Trigger notification to fraud team (simulated)
            notifyFraudTeam(report);

            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error submitting fraud report", e);
            return false;
        }
    }

    public List<FraudReport> getAllReports() {
        return new ArrayList<>(reportDatabase.values());
    }

    public FraudReport getReportById(String id) {
        return reportDatabase.get(id);
    }

    public boolean updateReportStatus(String id, String status) {
        if (reportDatabase.containsKey(id)) {
            FraudReport report = reportDatabase.get(id);
            report.setStatus(status);
            reportDatabase.put(id, report);
            return true;
        }
        return false;
    }

    private List<File> saveEvidenceFiles(String reportId, List<File> files) {
        List<File> savedFiles = new ArrayList<>();

        try {
            // Create directory for evidence files
            Path evidenceDir = Paths.get("evidence", reportId);
            Files.createDirectories(evidenceDir);

            // Copy files to evidence directory
            for (File file : files) {
                Path destination = evidenceDir.resolve(file.getName());
                Files.copy(file.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
                savedFiles.add(destination.toFile());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving evidence files", e);
        }

        return savedFiles;
    }

    private void notifyFraudTeam(FraudReport report) {
        // In a real application, this would send an email or other notification
        // to the fraud team
        LOGGER.info("Notification sent to fraud team for report: " + report.getId());
    }
}