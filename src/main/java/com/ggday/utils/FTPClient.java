package com.ggday.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FTPClient {

    private final org.apache.commons.net.ftp.FTPClient ftpClient;
    private static final String PUBLIC_FOLDER = "~/ggday/public_html/public/files";

    public FTPClient(String hostname) {
        this.ftpClient = new org.apache.commons.net.ftp.FTPClient();
        this.ftpClient.setControlKeepAliveTimeout(300);
        try {
            ftpClient.connect(hostname);
        } catch (IOException e) {
            System.out.print(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean login(String username, String password) {
        try {
          return ftpClient.login(username, password);
        } catch (IOException e) {
            System.out.print(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean changeDirectoryTo(String path) {
        try {
            return ftpClient.changeWorkingDirectory(path);
        } catch (IOException e) {
            System.out.print(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String getCurrentDirectoryPath() {
        try {
            return ftpClient.printWorkingDirectory();
        } catch (IOException e) {
            System.out.print(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean openPublicFilesDirectory() {
      return changeDirectoryTo(PUBLIC_FOLDER);
    }

    public boolean createTodayImageDirectory() {
        LocalDateTime now = LocalDateTime.now();
        Stream.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth()).map(String::valueOf)
                .forEach(p->{
                    boolean marker = mkdirAndCd(p);
                    if (!marker) {
                        throw new RuntimeException("Previous folder wasn't created");
                    }
                });
        return true;
    }

    public boolean mkdirAndCd(String folderName) {
        if (!StringUtils.isBlank(folderName)) {
            try {
                boolean dirExists = ftpClient.changeWorkingDirectory(folderName);
                if (!dirExists) {
                    boolean isCreated = ftpClient.makeDirectory(folderName);
                    if (isCreated) {
                        if (!changeDirectoryTo(folderName)) {
                            System.out.println("Can't open dir: " + folderName);
                            System.out.println("Current path: " + getCurrentDirectoryPath());
                            System.out.println("Available directories: " + Stream.of(ftpClient.listDirectories())
                                    .map(FTPFile::getName).collect(Collectors.joining("/")));
                            throw new RuntimeException("Can't execute mkdirAndCd");
                        }
                        return true;
                    }
                } else {
                    return true;
                }
            } catch (IOException e) {
                System.out.print(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
