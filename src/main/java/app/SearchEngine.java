package app;

import app.GUI.GUIService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by Innokentiy on 27.06.2019.
 */
public class SearchEngine {
    private ThreadPoolExecutor executor;
    private AppContext context;
    private HashMap<File, List<EntrancePosition>> results;
    private String query;
    private GUIService guiService;

    public SearchEngine(AppContext context){
        this.context = context;
        results = new HashMap<>();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
    }
    public void searchForSubstring(String directory, String substring, String fileFormat) throws IOException, InterruptedException {
        query = substring;
        results.clear();
        List<File> files = getFilesFromFolder(directory,  fileFormat);
        if (files.size()==0){
            JOptionPane.showMessageDialog(context.getMainFrame(),"Вхождений не найдено");
            return;
        }
        guiService = new GUIService(context, files.size());
        new Thread(guiService).start();
//        GUIService guiService= new GUIService(context,files.size());
        for (int i = 0; i < files.size(); i++) {
            executor.submit(new SearchThread(files.get(i),substring, this));
        }
    }
    private List<File> getFilesFromFolder(String folderName, String fileFormat) throws IOException {
        File directory = new File(folderName);
        List<File> files = Files.walk(Paths.get(directory.getAbsolutePath()))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
        if (!fileFormat.equals("")){
            List<File> toRemove = new ArrayList<>();
            for (int i = 0; i < files.size(); i++) {
                int dotPos = files.get(i).getName().lastIndexOf('.');
                boolean isDirectory = files.get(i).isDirectory();
                boolean isFormatSame = files.get(i).getName().substring(dotPos+1).equals(fileFormat);
                boolean isContainDot = dotPos!=0;
                if ((!isDirectory && !isFormatSame) || !isContainDot){
                    toRemove.add(files.get(i));
                }
            }
            files.removeAll(toRemove);
        }
        return files;
    }

    public HashMap<File, List<EntrancePosition>> getResults() {
        return results;
    }
    public synchronized void addResults(File file, List<EntrancePosition> entrances){
        results.put(file, entrances);
        guiService.decrLatch();

    }
    public String getQuery(){
        return this.query;
    }
}
