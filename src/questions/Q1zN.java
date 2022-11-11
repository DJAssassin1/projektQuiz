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
public class Q1zN extends Questions{

    LinkedList<String> a= new LinkedList<String>();
    
    public Q1zN(String q, LinkedList<String> ra, LinkedList<String> a) {
        super(q, ra);
        this.a = a;
    }
    
    
    public LinkedList<String> getA() {
        return a;
    }
}
