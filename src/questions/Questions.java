/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package questions;

import java.util.LinkedList;


/**
 *
 * @author djassassin
 */

/*
třída předka na všechny typy otázek
q = otázka
v = hodnota
ra = pole správných odpovědí
*/
public class Questions {
    
    String q;
    LinkedList<String> ra= new LinkedList<String>();
    
    public Questions(String q, LinkedList<String> ra) {
        this.q = q;
        this.ra = ra;
    }
    
    public String getQ() {
        return q;
    }
    
    public LinkedList<String> getRa() {
        return ra;
    }
    
}
