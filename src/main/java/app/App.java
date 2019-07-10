package app;

import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by Innokentiy on 23.06.2019.
 */
public class App {

    public static void main(String[] args) throws IOException, InterruptedException {
        AppContext context = new AppContext();
    }



    /*public static void KMPSearch(BufferedReader reader, String subs) throws IOException {
        int[] indexes = KMPindexes(subs);
        int textIndex = 0;
        int patternIndex = 0;
        List<Character> buffered = new LinkedList<>();
//        while (textIndex<s.length()){
        int symbolsRead=0;
        int curSym;
        while ((curSym=reader.read())!=-1){
            symbolsRead++;
            char curChar = (char) curSym;
            if (subs.charAt(patternIndex)==curChar){
//            if (subs.charAt(patternIndex)==s.charAt(textIndex)){
                textIndex++;
                patternIndex++;
            }
            if (patternIndex == subs.length()){
                System.out.println("вхождение на "+(textIndex-patternIndex)+" символе");
                patternIndex=indexes[patternIndex-1];
            }
//            else if (textIndex < s.length() && subs.charAt(patternIndex) != s.charAt(textIndex)) {
            else if (curSym!=-1 && subs.charAt(patternIndex) != curChar) {
                // Do not match lps[0..lps[j-1]] characters,
                // they will match anyway
                if (patternIndex != 0)
                    patternIndex = indexes[patternIndex - 1];
                else
                    textIndex++;
            }
        }
    }
    public static int[] KMPindexes(String s){
        int[] indexes = new int[s.length()];
        indexes[0] = 0;
        int length=0;
        int i =1;
        while (i<indexes.length){
            if (s.charAt(i)==s.charAt(length)){
                length++;
                indexes[i]=length;
                i++;
            }else {
                if (length!=0){
                    length=indexes[length-1];
                }else {
                    indexes[i]=0;
                    i++;
                }
            }
        }
        return indexes;
    }*/
}
