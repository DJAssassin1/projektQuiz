/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package questions;

import java.util.LinkedList;

/**
 *
 * @author domin
 */
public class All {
    LinkedList<Questions> questions= new LinkedList<Questions>();
    String name;

    public All(String name, LinkedList<Questions> questions) {
        this.name = name;
        this.questions = questions;
    }

    public LinkedList<Questions> getQuestions() {
        return questions;
    }

    public String getName() {
        return name;
    }
    
    
}
