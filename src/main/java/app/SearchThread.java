package app;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Innokentiy on 23.06.2019.
 */
public class SearchThread implements Runnable{

    private File file;
    private String substring;
    private SearchEngine searchEngine;
    public SearchThread(File file, String substring, SearchEngine searchEngine){
        this.file = file;
        this.substring = substring;
        this.searchEngine = searchEngine;
    }
    public List<EntrancePosition> search(File file, String substring) throws IOException {
        InputStream in = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        List<EntrancePosition> result = new ArrayList<>();
        try {
            String line;

            String[] substringInLines = substring.split("\n");
                Pattern pattern = Pattern.compile(substring);
                int lineNumber = 1;
                while ((line = reader.readLine()) != null) {
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        result.add(new EntrancePosition(lineNumber,matcher.start()));
                    }
                    lineNumber++;

                }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void run() {
        try {
            List<EntrancePosition> result = search(file, substring);
            if (result.size()!=0) {
                searchEngine.addResults(file, result);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
