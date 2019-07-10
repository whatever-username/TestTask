package app;

import java.io.File;

/**
 * Created by Innokentiy on 27.06.2019.
 */
public class FileDecorator {
    String fileName;
    File filePath;
    public FileDecorator(String fileName, File filePath){
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public File getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return fileName;
    }
}
