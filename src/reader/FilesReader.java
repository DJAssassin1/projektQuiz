/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reader;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
import questions.All;
import questions.Questions;

/**
 *
 * @author domin
 */
public class FilesReader {
    
    final static Pattern refName = Pattern.compile("[N|n][á|a]zev:[ ]*(.+)");
    private static String errFile = "chyba v souboru: ";
    
    public static All FilesReader(Scanner s) {
        String name;
        String line;


        //ošetření od prázdného souboru
        if(!s.hasNextLine()){
            showMessageDialog(null, errFile + "soubor je prazdny", "Error", ERROR_MESSAGE);
            return null;
        }

        line = s.nextLine();
        while(line.equals("")||line.charAt(0)=='#'){
            if(!s.hasNextLine()){
                showMessageDialog(null, errFile + "soubor je prazdny", "Error", ERROR_MESSAGE);
                return null;
            }
            line = s.nextLine();
        }
        Matcher matchName = refName.matcher(line);
        if(!matchName.matches()){
            showMessageDialog(null, errFile + "soubor je prazdny", "Error", ERROR_MESSAGE);
            return null;
        }
        name = matchName.group(1);
        System.out.println(name);


        LinkedList<Questions> questions= new LinkedList<Questions>();

        while(s.hasNextLine()){
            Questions myQuest = QuestionsReader.Question(s);
            //ošetření chyb z QuestionsReader
            if(myQuest == null){
                return null;
            }
            questions.add(myQuest);
            //System.out.println(myQuest.getQ());
            //System.out.println(myQuest.getRa());
        }




        //zamichani
        Collections.shuffle(questions);
        return new All(name, questions);
    }
    
}
