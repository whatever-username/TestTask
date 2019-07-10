package app;

import app.GUI.Main;

/**
 * Created by Innokentiy on 27.06.2019.
 */
public class AppContext {
    public static AppContext context;
    private SearchEngine searchEngine;
    private Main mainFrame;
    public AppContext(){
        searchEngine = new SearchEngine(this);
        mainFrame = new Main(this);
        context = this;
    }

    public SearchEngine getSearchEngine() {
        return searchEngine;
    }

    public Main getMainFrame() {
        return mainFrame;
    }
}
