package app;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Innokentiy on 26.06.2019.
 */
public class Tree {
    private Node topNode;
    private HashMap<File, Node> nodes;
    private List<File> fileList;
    public Tree(List<File> matchingFiles){
        nodes = new HashMap<>();
        for (int i = 0; i < matchingFiles.size(); i++) {
            File curFile = matchingFiles.get(i);
            Node curNode = new Node(curFile);
            nodes.put(curNode.value, curNode);
            if (curFile.getParentFile()!=null) {
                addParent(curNode);
            }

        }
        while((getTopNode().childrenList.size()==1)&& (getTopNode().childrenList.get(0).value.isDirectory())){
            setTopNode(getTopNode().childrenList.get(0));
        }
        /*for (Map.Entry<File,Node> e:nodes.entrySet()) {
            Node cur= e.getValue();
            String s = cur.value.getName();
            if (cur.parent!=null){
                s+= "\n\tparent: "+cur.parent.value;
            }
            if (cur.childrenList!=null){
                s+="\n\tchildren: ";
                for (int i = 0; i < cur.childrenList.size(); i++) {
                    s+= cur.childrenList.get(i).value+"; ";
                }
            }
            System.out.println(s);
        }*/

//        fileList = new ArrayList<>();
//        fillFileList(topNode);

    }

    public Node getTopNode() {
        return topNode;
    }
    private void setTopNode(Node node){
        nodes.remove(topNode);
        topNode = node;
    }

    private void fillFileList(Node curNode){
        fileList.add(curNode.value);


        if (curNode.childrenList!=null) {
            for (int i = 0; i < curNode.childrenList.size(); i++) {
                fillFileList(curNode.childrenList.get(i));
            }
        }

    }
    public String toString(){
        StringBuilder s = new StringBuilder();
        fileList.stream().map((e)->s.append(e).append("\n")).collect(Collectors.toList());
        return s.toString();
    }
    private void addParent(Node curNode){
        if (curNode!=null) {
            if (curNode.value.getParentFile()!=null){

                Node parentNode;
                if (nodes.get(curNode.value.getParentFile())==null) {
                     parentNode= new Node(curNode.value.getParentFile());
                    nodes.put(parentNode.value, parentNode);

                }else {
                    parentNode = nodes.get(curNode.value.getParentFile());
                }
                curNode.parent = parentNode;
                if (parentNode.childrenList==null) {
                    parentNode.childrenList = new ArrayList<>();
                }
                if (!parentNode.childrenList.contains(curNode)) {
                        parentNode.addChilden(curNode);
                        addParent(parentNode);
                    }


            }else if (topNode==null){
                topNode=curNode;
            }
        }
    }


    public class Node{
        public File value;
        public Node parent;
        public List<Node> childrenList;
        public Node(File value){
            this.value = value;
        }
        public Node(File value, Node parent){
            this.value = value;
            this.parent = parent;
        }
        private void addChilden(Node children){
            if (childrenList==null){
                childrenList = new ArrayList<>();

            }
            childrenList.add(children);
        }

        public File getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            if (!value.equals(node.value)) return false;
            return true;
        }

        @Override
        public int hashCode() {
            int result = value.hashCode();
            return result;
        }
    }
}

