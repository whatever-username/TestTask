package app.GUI;

import app.AppContext;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Innokentiy on 28.06.2019.
 */
public class CustomTabbedPane extends JTabbedPane {
    HashMap<String, Component> tabs;
        public CustomTabbedPane(){
            tabs = new HashMap<>();
            setPreferredSize(new Dimension(800,400));
        }
        public void addNewTab(String tabTitle, Component content){
            if (tabs.get(tabTitle)==null){

                tabs.put(tabTitle, content);
                addTab(tabTitle, content);
            }
            setSelectedComponent(tabs.get(tabTitle));
        }
        public void closeTab(String fileName){
            remove(tabs.get(fileName));
            tabs.remove(fileName);

        }



}
