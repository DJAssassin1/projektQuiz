/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reader;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
import questions.Q1zN;
import questions.QXzN;
import questions.Questions;

/**
 *
 * @author djassassin
 */
public class QuestionsReader {

    Scanner s;
    
    public QuestionsReader(Scanner s) {
        this.s = s;
    }
    
    
    
    private static String Reader(String line, int groupNum, Pattern pat){
        if(line.equals(""))
            return "clear";
        if(line.charAt(0)=='#'){
            return "#";
        }
        Matcher matcher = pat.matcher(line);
        if(!matcher.matches()){
            return null;
        }
        return matcher.group(groupNum);
    }
    
    
    public static Questions Question(Scanner s){
        //regularni vyrazy
        final Pattern refQuest= Pattern.compile("[O|o]t[á|a]zka (.+):[ ]*(.+)");
        final Pattern refValue= Pattern.compile("[H|h]odnota:[ ]*(\\S+)");
        final Pattern refRight= Pattern.compile("[S|s]pr[á|a]vn[ě|e]:[ ]*(.+)");
        final Pattern refBad= Pattern.compile("[Š|S|š|s]patn[ě|e]:[ ]*(.+)");
        
        
        final String errFile = "nedodelany soubor: "; 
        
        //potrebne promene
        String typeOfQ;//typ otazky
        String question;//otazka
        String line;//radek
        
        
        
        
        //OTAZKA
        if(!s.hasNextLine()){
            showMessageDialog(null, errFile + "Otazka - nedostatecny pocet radku", "Error", ERROR_MESSAGE);
            return null;
        }
        line = s.nextLine();
        typeOfQ = Reader(line, 1, refQuest);
        
        
        if(typeOfQ == null){
            showMessageDialog(null, errFile + "chyba v typu otazek:\n"+line, "Error", ERROR_MESSAGE);
            return null;
        }
        while(typeOfQ.equals("#")||typeOfQ.equals("clear")){
            if(!s.hasNextLine()){
                showMessageDialog(null, errFile + "Otazka - nedostatecny pocet radku", "Error", ERROR_MESSAGE);
                return null;
            }
            line = s.nextLine();
            typeOfQ = Reader(line, 1, refQuest);
            if(typeOfQ == null){
                showMessageDialog(null, errFile + "chyba v typu otazek:\n"+line, "Error", ERROR_MESSAGE);
                return null;
            }
        }
        question = Reader(line, 2, refQuest);
        
        
       

        //osetreni
        if(typeOfQ.equals("clear")||question.equals("clear")){
            showMessageDialog(null, errFile + "odstavec navic", "Error", ERROR_MESSAGE);
            return null;
        }
        
        
        
        
        
        
        if(!s.hasNextLine()){
            showMessageDialog(null, errFile + "0 odpovedi", "Error", ERROR_MESSAGE);
            return null;
        }
        line = s.nextLine();
        
        LinkedList<String> ra= new LinkedList<String>();
        LinkedList<String> a= new LinkedList<String>();
        String answerBad = "";
        String answer = "";
        int i = 0;
        
        switch(typeOfQ){
            case "1zN":
                while(!line.equals("")){
                    answer = Reader(line, 1, refRight);
                    answerBad = Reader(line, 1, refBad);
                    if(answer == null && answerBad == null){
                        showMessageDialog(null, errFile + "spatne zadana odpoved:\n" + line, "Error", ERROR_MESSAGE);
                        return null;
                    }
                    else if(answer == null){
                        a.add(answerBad);
                    }
                    else if(answerBad == null){
                        a.add(answer);
                        ra.add(answer);
                    }
                    else if(answer.equals("#")){
                    }
                    else{
                        showMessageDialog(null, errFile + "neco neocekavaneho na radku: \n"+line, "Error", ERROR_MESSAGE);
                        return null;
                    }
                    if(!s.hasNextLine()){
                        if(i<2 || i>14){
                            showMessageDialog(null, errFile + "spatny pocet odpovedi: \n"+line, "Error", ERROR_MESSAGE);
                            return null;
                        }
                        else if(ra.isEmpty()){
                            showMessageDialog(null, errFile + "0 spravnych odpovedi: \n"+line, "Error", ERROR_MESSAGE);
                            return null;
                        }
                        return new Q1zN(question, ra, a);
                    }
                    line = s.nextLine();
                    i++;
                }
                if(i<2 || i>14){
                    showMessageDialog(null, errFile + "spatny pocet odpovedi:\n "+line, "Error", ERROR_MESSAGE);
                    return null;
                }
                else if(ra.isEmpty()){
                    showMessageDialog(null, errFile + "0 spravnych odpovedi: \n"+line, "Error", ERROR_MESSAGE);
                    return null;
                }
                return new Q1zN(question, ra, a);
                
                
                
            //xzn je stejný jako 1zn protože je to více méně to samé
            case "XzN":
                while(!line.equals("")){
                    answer = Reader(line, 1, refRight);
                    answerBad = Reader(line, 1, refBad);
                    if(answer == null && answerBad == null){
                        showMessageDialog(null, errFile + "spatne zadana odpoved:\n" + line, "Error", ERROR_MESSAGE);
                        return null;
                    }
                    else if(answer == null){
                        a.add(answerBad);
                    }
                    else if(answerBad == null){
                        a.add(answer);
                        ra.add(answer);
                    }
                    else if(answer.equals("#")){
                    }
                    else{
                        showMessageDialog(null, errFile + "neco neocekavaneho na radku:\n"+line, "Error", ERROR_MESSAGE);
                        return null;
                    }
                    if(!s.hasNextLine()){
                        if(i<2 || i>14){
                            showMessageDialog(null, errFile + "spatny pocet odpovedi: \n"+line, "Error", ERROR_MESSAGE);
                            return null;
                        }
                        else if(ra.isEmpty()){
                            showMessageDialog(null, errFile + "0 spravnych odpovedi: \n"+line, "Error", ERROR_MESSAGE);
                            return null;
                        }
                        return new QXzN(question, ra, a);
                    }
                    line = s.nextLine();
                    i++;
                }
                if(i<2 || i>14){
                    showMessageDialog(null, errFile + "spatny pocet odpovedi: \n"+line, "Error", ERROR_MESSAGE);
                    return null;
                }
                else if(ra.isEmpty()){
                    showMessageDialog(null, errFile + "0 spravnych odpovedi: \n"+line, "Error", ERROR_MESSAGE);
                    return null;
                }
                return new QXzN(question, ra, a);
                
            case "pis":
                while(!line.equals("")){
                    answer = Reader(line, 1, refRight);
                    if(answer == null){
                        showMessageDialog(null, errFile + "spatne zadana odpoved:\n"+line, "Error", ERROR_MESSAGE);
                        return null;
                    }
                    else if(answer.equals("#")){
                    }
                    else{
                        ra.add(answer);
                    }
                    if(!s.hasNextLine()){
                        return new Questions(question, ra);
                    }
                    line = s.nextLine();
                }
                return new Questions(question, ra);
            
            
            
            default:
                showMessageDialog(null, errFile + "neco necekaneho", "Error", ERROR_MESSAGE);
                return null;
        }
    }
    
}
