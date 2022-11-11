/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package writer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import questions.Q1zN;
import questions.QXzN;
import questions.Questions;

/**
 *
 * @author domin
 */
public class FilesWriter {

    public static boolean writer(LinkedList<Questions> quests, String name, String file) {
        try{
            FileWriter writer = new FileWriter(file);
            writer.write("Název: "+name+'\n');
            for(int i = 0;i<quests.size();i++){
                writer.write("Otázka ");
                switch(quests.get(i).getClass().getSimpleName()){
                    case "Q1zN":
                        Q1zN q1 = (Q1zN) quests.get(i);
                        writer.write("1zN: "+q1.getQ()+'\n');
                        for(String str : q1.getA()){
                            if(str.equals(q1.getRa().get(0))){
                                writer.write("Správně: "+str+'\n');
                            }
                            else{
                                writer.write("Špatně: "+str+'\n');
                            }
                        }
                        writer.write('\n');
                        break;
                    case "QXzN":
                        QXzN qX = (QXzN) quests.get(i);
                        writer.write("XzN: "+qX.getQ()+'\n');
                        for(String str : qX.getA()){
                            boolean good = false;
                            for(String ra : qX.getRa()){
                                if(str.equals(ra)){
                                    writer.write("Správně: "+str+'\n');
                                    good = true;
                                    break;
                                }
                            }
                            if(!good){
                                writer.write("Špatně: "+str+'\n');
                            }
                        }
                        writer.write('\n');
                        break;
                    case "Questions":
                        writer.write("pis: "+quests.get(i).getQ()+'\n');
                        for(String str: quests.get(i).getRa()){
                            writer.write("Správně: "+str+'\n');
                        }
                        writer.write('\n');
                        break;
                }
            }
            writer.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    
}
