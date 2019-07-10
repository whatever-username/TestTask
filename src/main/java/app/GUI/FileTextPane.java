package app.GUI;

import app.App;
import app.AppContext;
import app.EntrancePosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * Created by Innokentiy on 06.07.2019.
 */
public class FileTextPane extends JPanel {
    JEditorPane editorPane;
    AppContext context;

    public FileTextPane(AppContext context, File file) {

        this.context = context;
        editorPane = new JEditorPane();
        editorPane.setMargin(new Insets(0,0,0,0));
        this.setDoubleBuffered(true);
        setLayout(new BorderLayout());
        printWithOffset(file,0);
        int lineAmount = 0;
        try {
            lineAmount = (int) Files.lines(file.toPath()).parallel().count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long longestLine = 0;
        try {
            longestLine = Files.lines(file.toPath()).parallel().map((s) -> s.length()).max(Comparator.comparing(Long::valueOf)).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JScrollBar hbar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 10, 0, (int) longestLine);
        JScrollBar vbar = new JScrollBar(JScrollBar.VERTICAL, 0, 10, 0, lineAmount);


        hbar.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {

                editorPane.setMargin(new Insets(0, -e.getValue() * 4 + 10, 0, 0));
                editorPane.repaint();

            }
        });
        vbar.addAdjustmentListener(new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {

                long offset = e.getValue();
                printWithOffset(file,offset);

            }
        });
        JPanel editorPanel = new JPanel();
        editorPanel.setLayout(new BorderLayout());
        editorPanel.add(hbar, BorderLayout.SOUTH);
        editorPanel.add(vbar, BorderLayout.EAST);
        editorPanel.add(editorPane, BorderLayout.CENTER);
        add(editorPanel,BorderLayout.CENTER);
        JPanel navigationPanel = new JPanel();
        JLabel entranceLabel = new JLabel("Вхождение ");
        JTextField entranceNumber = new JTextField(3);
        JLabel entranceLabel2 = new JLabel(" из "+context.getSearchEngine().getResults().get(file).size());
        JButton prev = new JButton("Предыдущее");
        JButton next = new JButton("Следующее");
        JButton close = new JButton("Закрыть файл");

        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.getMainFrame().closeTab(file.getName());
            }
        });
        entranceNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int demandedEntrance = Integer.parseInt(entranceNumber.getText())-1;

                    long line = context.getSearchEngine().getResults().get(file).get(demandedEntrance).getLine();

                    vbar.setValue((int)line-1);
                }catch (Exception ex){
                    //popup
                    String message = "Введите число от 1 до "+ context.getSearchEngine().getResults().get(file).size();
                    JOptionPane.showMessageDialog(context.getMainFrame(), message);
                    entranceNumber.setText("");
                }
            }
        });
        prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long min = 1;
                long max = context.getSearchEngine().getResults().get(file).size();
                long currentValue;
                try{
                    currentValue = Integer.parseInt(entranceNumber.getText());
                }catch (NumberFormatException ex){
                    currentValue =1;
                }
                if (currentValue==min){
                    currentValue=max;
                }else {
                    currentValue--;
                }
                entranceNumber.setText(currentValue+"");
                vbar.setValue((int) context.getSearchEngine().getResults().get(file).get((int)currentValue-1).getLine()-1);
            }
        });
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long min = 1;
                long max = context.getSearchEngine().getResults().get(file).size();
                long currentValue;
                try{
                    currentValue = Integer.parseInt(entranceNumber.getText());
                }catch (NumberFormatException ex){
                    currentValue = max;
                }
                if (currentValue==max){
                    currentValue=min;
                }else {
                    currentValue++;
                }

                entranceNumber.setText(currentValue+"");
                vbar.setValue((int) context.getSearchEngine().getResults().get(file).get((int)currentValue-1).getLine()-1);
            }
        });

        navigationPanel.add(entranceLabel);
        navigationPanel.add(entranceNumber);
        navigationPanel.add(entranceLabel2);
        navigationPanel.add(prev);
        navigationPanel.add(next);
        navigationPanel.add(close);
        add(navigationPanel,BorderLayout.SOUTH);
        editorPanel.setPreferredSize(new Dimension(800,400));
        navigationPanel.setPreferredSize(new Dimension(800,50));
        setPreferredSize(new Dimension(800,450));
        setVisible(true);
    }
    private void printWithOffset(File file, long offset){
        StringBuilder builder = new StringBuilder();
        try {
            List<EntrancePosition> entrances = context.getSearchEngine().getResults().get(file);
            HashMap<Long, List<Long>> entrancesMap = new HashMap<>();
            for (int i = 0; i < entrances.size(); i++) {
                if (entrancesMap.get(entrances.get(i).getLine())==null){
                    entrancesMap.put(entrances.get(i).getLine(), new ArrayList<>());
                }
                entrancesMap.get(entrances.get(i).getLine()).add(entrances.get(i).getPosition());
            }
            Stream<String> offsetLines = Files.lines(file.toPath()).parallel().skip(offset);
            AtomicLong count = new AtomicLong(offset+1);
            int querySize = AppContext.context.getSearchEngine().getQuery().length();
            offsetLines.sequential().limit(50).forEach(s ->{
                StringBuilder line = new StringBuilder(s);
                    if (entrancesMap.containsKey(count.longValue())){
                       List<Long> curLineEntrances = entrancesMap.get(count.longValue());
                       for (int i = 0; i < curLineEntrances.size(); i++) {
                            line.insert(curLineEntrances.get(curLineEntrances.size()-i-1).intValue()+querySize,"</span>");
                            line.insert(curLineEntrances.get(curLineEntrances.size()-i-1).intValue(),"<span style=\"background-color:red\">");
                        }
                    }
                    builder.append(line).append("<br>");
                    count.incrementAndGet();
            });
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        editorPane.setContentType("text/html");
        editorPane.setText(builder.toString());
    }

}