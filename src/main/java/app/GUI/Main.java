package app.GUI;

import app.AppContext;
import app.FileDecorator;
import app.Tree;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.awt.GridBagConstraints.FIRST_LINE_START;

/**
 * Created by Innokentiy on 25.06.2019.
 */
public class Main extends JFrame {
    private GridBagConstraints gbc;
    private JTree fileTree;
    private JPanel panel;
    private JScrollPane searchResults;
    private AppContext context;
    private CustomTabbedPane tabbedPane;
    private JLabel fileTypeLabel;
    public Main(AppContext context){
        super("File Searcher");
        this.context = context;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        gbc = new GridBagConstraints();



        gbc.gridx=0; gbc.gridy=0;
        gbc.anchor=FIRST_LINE_START;
        gbc.weightx=1;
        gbc.insets = new Insets(2,2,2,2);
        //Результаты поиска
        searchResults= new JScrollPane(null, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        searchResults.setPreferredSize(new Dimension(200,500));



        gbc.gridheight=3;
        add(searchResults,gbc); // 0 0
        gbc.gridheight=1;
        gbc.gridx++;

        JLabel directoryPointLabel = new JLabel("Файл: ");
        JTextField directoryPointText = new JTextField("",30);
        JButton fileNavOpenButton = new JButton("Выбрать");
        fileNavOpenButton.setMargin(new Insets(2,6,3,7));
        fileNavOpenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int r = fileChooser.showOpenDialog(null);
                if (r==0){
                    File file = fileChooser.getSelectedFile();
                    directoryPointText.setText(file.toString());
                }

            }
        });
        add(directoryPointLabel,gbc); // 0 1
        gbc.gridx++;
        add(directoryPointText,gbc); // 0 2
        gbc.gridx++;
        add(fileNavOpenButton,gbc); // 0 3

        fileTypeLabel = new JLabel("Расширение: .");
        JTextField fileTypeField = new JTextField( "log",5);
        gbc.gridx++;
        add(fileTypeLabel,gbc); // 0 5
        gbc.gridx++;
        add(fileTypeField,gbc); // 0 6

        JLabel searchQueryLabel = new JLabel("Поисковый запрос: ");
//        JTextField searchQueryField = new JTextField(30);
        JTextField searchQueryField = new JTextField("" ,30); //ЗАМЕНИТЬ

        gbc.gridy=1;
        gbc.gridx =1;
        add(searchQueryLabel,gbc); // 1 0
        gbc.gridx++; // 1 1
        add(searchQueryField,gbc);

        JButton searchButton = new JButton("Поиск");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pattern pattern = Pattern.compile("[a-zA-Z0-9]{0,}$");
                Matcher matcher = pattern.matcher(fileTypeField.getText());
                if (!matcher.find()) {
                    JOptionPane.showMessageDialog(context.getMainFrame(), "Введите формат файлов без точки или оставьте поле пустым");

                } else {
                    try {

                        context.getSearchEngine().searchForSubstring(directoryPointText.getText(), searchQueryField.getText(),fileTypeField.getText());

                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(context.getMainFrame(), "Ошибка чтения файла");
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } catch (InvalidPathException e1){
                        JOptionPane.showMessageDialog(context.getMainFrame(), "Проверьте правильность ввода пути к файлу");
                    }
                }
            }
        });
        gbc.gridx++; // 1 2
        gbc.gridwidth =2;
        add(searchButton, gbc);



        panel = new JPanel();
        panel.setPreferredSize(new Dimension(800,450));
        gbc.gridy=2; gbc.gridx =1; gbc.gridheight=1;gbc.gridwidth=5;
        add(panel,gbc); //2 1

        pack();
        setVisible(true);
        setResizable(false);
    }
    private DefaultMutableTreeNode formTreeNode(List<File> files){
        Tree fileTree = new Tree(files);
        Tree.Node topNode = fileTree.getTopNode();
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(new FileDecorator(topNode.value.getName(),topNode.value));
        addChildren(fileTree.getTopNode(), top);
        return top;
    }
    private void addChildren(Tree.Node node, DefaultMutableTreeNode treeNode){
        if (node.childrenList!=null) {
            for (int i = 0; i < node.childrenList.size(); i++) {
                Tree.Node curNode = node.childrenList.get(i);
                DefaultMutableTreeNode cur = new DefaultMutableTreeNode(new FileDecorator(curNode.value.getName(),curNode.value));
                treeNode.add(cur);
                addChildren(node.childrenList.get(i), cur);
            }
        }
    }
    public synchronized void refreshTree(){
        List<File> files = context.getSearchEngine().getResults().keySet().stream().collect(Collectors.toList());
        DefaultMutableTreeNode node = formTreeNode(files);
        fileTree = new JTree(node);
        fileTree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                            fileTree.getLastSelectedPathComponent();
                    if (node == null) return;
                    FileDecorator file = (FileDecorator) node.getUserObject();
                    if (!file.getFilePath().isDirectory()){
                        addTab(file);
                    }
                }
            }
        });
        searchResults.setViewportView(fileTree);
        searchResults.revalidate();
    }
    public void createTabbedPane(){

            tabbedPane = new CustomTabbedPane();
            tabbedPane.setPreferredSize(new Dimension(800,400));
            panel.add(tabbedPane);
            panel.revalidate();

    }
    public void addTab(FileDecorator fileDecorator){
        if (tabbedPane==null){
            createTabbedPane();
        }
        FileTextPane textPane = new FileTextPane(context, fileDecorator.getFilePath());
        tabbedPane.addNewTab(fileDecorator.getFileName(),textPane);
    }
    public void closeTab(String fileName){
        tabbedPane.closeTab(fileName);
        if (tabbedPane.getTabCount()==0){
            panel.remove(panel);
            panel.revalidate();
        }
    }


}
