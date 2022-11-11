/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package boards;

import menus.MainMenu;
import java.awt.Color;
import java.awt.TextField;
import java.net.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JRadioButton;
import questions.All;
import questions.Q1zN;
import questions.QXzN;
import questions.Questions;

/**
 *
 * @author domin
 */
public class CreateBoard extends javax.swing.JFrame {
    
    //communication
    Socket s;
    DataOutputStream dout;
    DataInputStream din;
    String str;
    
    //permanent stuff
    final int boardSize = 500;
    final int freeSpace = 5;
    final int winNum = 4;
    final Color waitC = Color.gray;
    final int maxBtns = 14;
    
    //visuals
    JButton btns[][];
    JButton pushedBtn;
    TextField field = new TextField();
    JRadioButton[] q1zNBtns;
    JCheckBox[] qXzNBtns;
    ButtonGroup btnG = new ButtonGroup();
    
    //variables
    Color myColor;
    Color opponentColor;
    boolean[] ra;
    String type;
    boolean right = true;
    int numQBtns;
    int numBtns;
    boolean playing = true;
    int current = 0;
    LinkedList<Questions> questions= new LinkedList();
    All quiz;
    /**
     * Creates new form CreateBoard
     */
    public CreateBoard() {
        initComponents();
    }
    
    
    public CreateBoard(Socket s, int numBtns, All quiz, String sMyC, String sEnemyC) {
        try {
            initComponents();
            this.s = s;
            this.numBtns = numBtns;
            this.questions = quiz.getQuestions();
            this.quiz = quiz;
            try {
                Field field = Class.forName("java.awt.Color").getField(sMyC);
                myColor = (Color)field.get(null);
                Field field2 = Class.forName("java.awt.Color").getField(sEnemyC);
                opponentColor = (Color)field2.get(null);
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
            
            
            //setup
            this.setResizable(false);
            this.setLocationRelativeTo(null);
            this.setTitle(quiz.getName());
            
            dout = new DataOutputStream(s.getOutputStream());
            din = new DataInputStream(s.getInputStream());
            dout.writeUTF(quiz.getName());
            dout.writeInt(numBtns);
            
            //initialization of Questions
            field.setBounds(535,60,420,20);
            field.setText("");
            add(field);
            field.setVisible(false);
            
            
            q1zNBtns = new JRadioButton[maxBtns];
            qXzNBtns = new JCheckBox[maxBtns];
            for(int i=0; i<maxBtns; i++){
                q1zNBtns[i] = new JRadioButton();
                add(q1zNBtns[i]);
                btnG.add(q1zNBtns[i]);
                q1zNBtns[i].setBounds(555,70+30*i,450,20);
                q1zNBtns[i].setVisible(false);
                
                qXzNBtns[i] = new JCheckBox();
                add(qXzNBtns[i]);
                qXzNBtns[i].setBounds(555,40+30*i,450,20);
                qXzNBtns[i].setVisible(false);
            }
            
            //initialization of tic-tac-toe
            btns = new JButton[numBtns][numBtns];
            for(int i=0; i<this.numBtns; i++){
                for(int j=0; j<this.numBtns; j++){
                    btns[i][j] = new JButton();
                    JButton btn = btns[i][j];
                    add(btn);
                    btn.setBackground(Color.white);
                    btn.setBounds(i*boardSize/this.numBtns, j*boardSize/this.numBtns, boardSize/this.numBtns-freeSpace, boardSize/this.numBtns-freeSpace);
                    
                    
                    btn.addActionListener(e ->
                    {
                        if(playing && btn.getBackground()==Color.white){
                            pushedBtn = btn;
                            btnSend.setVisible(true);
                            labelQuest.setText(questions.get(current).getQ());
                            type = questions.get(current).getClass().getSimpleName();
                            btn.setBackground(waitC);
                            switch(type){
                                case "Q1zN":
                                    Q1zN q1zn = (Q1zN) questions.get(current);
                                    LinkedList<String> a1zn = q1zn.getA();
                                    numQBtns = a1zn.size();
                                    ra = null;
                                    ra = new boolean[numQBtns];
                                    for(int m=0;m!=numQBtns;m++){
                                        q1zNBtns[m].setText(a1zn.get(m));
                                        System.out.println(a1zn.get(m));
                                        q1zNBtns[m].setVisible(true);
                                        if(a1zn.get(m).equals((q1zn.getRa()).get(0))){
                                            ra[m] = true;
                                        }
                                        else{
                                            ra[m] = false;
                                        }
                                    }
                                    break;
                                case "QXzN":
                                    QXzN qxzn = (QXzN) questions.get(current);
                                    LinkedList<String> axzn = qxzn.getA();
                                    numQBtns = axzn.size();
                                    ra = null;
                                    ra = new boolean[numQBtns];
                                    for(int m=0;m!=qxzn.getA().size();m++){
                                        qXzNBtns[m].setText(axzn.get(m));
                                        System.out.println(axzn.get(m));
                                        qXzNBtns[m].setVisible(true);
                                        ra[m] = false;
                                        for(String raans : qxzn.getRa()){
                                            if(raans.equals(axzn.get(m))){
                                                ra[m] = true;
                                            }
                                        }
                                    }
                                    break;
                                case "Questions":
                                    field.setVisible(true);
                                    break;
                            }
                            playing = false;
                        }
                    });
                }
            }
        } catch (IOException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }
    //controling if u win
    boolean winner(Color color){
        int count1 = 1;//row
        int count2 = 1;//column
        int count3 = 1;//diagonally down
        int count4 = 1;//diagonally up
        boolean still1 = true;
        boolean still2 = true;
        boolean still3 = true;
        boolean still4 = true;
        for(int i = 0; i< numBtns; i++){
            for(int j = 0; j< numBtns; j++){
                if(btns[i][j].getBackground() == color){
                    for(int x = 1; x<winNum;x++){
                        if(i+x<numBtns){
                            if(btns[i+x][j].getBackground() == color && still1 == true){//row
                                count1++;
                            }
                            else{
                                still1 = false;
                            }
                            if(j+x<numBtns){
                                if(btns[i+x][j+x].getBackground() == color && still3 == true){//diagonally down
                                    count3++;
                                }
                                else{
                                    still3 = false;
                                }
                            }
                            if(j-x>=0){
                                if(btns[i+x][j-x].getBackground() == color && still4 == true){//diagonally up
                                    count4++;
                                }
                                else{
                                    still4 = false;
                                }
                            }
                        }
                        if(j+x<numBtns){
                            if(btns[i][j+x].getBackground() == color && still2 == true){//column
                                count2++;
                            }
                            else{
                                still2 = false;
                            }
                        }
                    }
                    //System.out.println(count1 + "  "+count2+ "  "+count3+ "  "+count4);
                    if(count1 >= winNum||count2 >= winNum||count3 >= winNum||count4 >= winNum){
                        return true;
                    }
                    count1 = 1;//row
                    count2 = 1;//column
                    count3 = 1;//diagonally down
                    count4 = 1;//diagonally up
                    still1 = true;
                    still2 = true;
                    still3 = true;
                    still4 = true;
                }
            }
        }
        return false;
    }
    
//controling if its end
    boolean end(String str){
        if(str.equals("end")){
            try {
                s.close();
                din.close();
                dout.close();
                showMessageDialog(null,"Opponent left");
            } catch (IOException ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }

            MainMenu window = new MainMenu();
            window.setVisible(true);
            dispose();
            return true;
        }
        return false;
    }
    
    
    boolean draw(){
        boolean whitespace = false;
        for(int i = 0; i < numBtns; i++){
            for(int j = 0; j < numBtns; j++){
                if(btns[i][j].getBackground()== Color.white){
                    whitespace = true;
                }
            }
        }
        return whitespace;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSend = new javax.swing.JButton();
        btnMainMenu = new javax.swing.JButton();
        labelQuest = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnSend.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSend.setText("Send");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        btnMainMenu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnMainMenu.setText("Main menu");
        btnMainMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMainMenuActionPerformed(evt);
            }
        });

        labelQuest.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(533, 533, 533)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(labelQuest, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSend))
                    .addComponent(btnMainMenu, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelQuest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 451, Short.MAX_VALUE)
                .addComponent(btnMainMenu)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        labelQuest.setText("");
        btnSend.setVisible(false);
        right = true;
        //controling right answers
        switch(type){
            case "Q1zN":
                for(int i=0; i<numQBtns; i++){
                    if(ra[i]!=q1zNBtns[i].isSelected()){
                        right = false;
                    }
                    q1zNBtns[i].setSelected(false);
                    q1zNBtns[i].setVisible(false);
                }
                break;
            case "QXzN":
                for(int i=0; i<numQBtns; i++){
                        if(ra[i]!=qXzNBtns[i].isSelected()){
                            right = false;
                        }
                        qXzNBtns[i].setSelected(false);
                        qXzNBtns[i].setVisible(false);
                    }
                break;
            case "Questions":
                right = false;
                String text = field.getText();
                field.setText("");
                for(String rapis : questions.get(current).getRa()){
                    if(rapis.equals(text)){
                        right = true;
                    }
                }
                field.setVisible(false);
                break;
        }
        if(right){
            pushedBtn.setBackground(myColor);
            for(int x = 0; x<this.numBtns;x++){
                    for(int y = 0; y<this.numBtns; y++){
                        btns[x][y].setEnabled(true);
                        if(pushedBtn == btns[x][y]){
                            try {
                                dout.writeUTF(x+":"+y);
                            } catch (IOException ex) {
                                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
                            }
                        }
                    }
            }
            
            if(winner(myColor)){
                try {
                    din.close();
                    dout.close();
                    s.close();
                } catch (IOException ex) {
                    showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
                }
                showMessageDialog(null,"You won");
                MainMenu window = new MainMenu();
                window.setVisible(true);
                dispose();
                return;
            }
            if(!draw()){
                try {
                    din.close();
                    dout.close();
                    s.close();
                } catch (IOException ex) {
                    showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
                }
                showMessageDialog(null,"Draw");
                MainMenu window = new MainMenu();
                window.setVisible(true);
                dispose();
                return;
            }
        }
        else{
            try {
                dout.writeUTF("nothing");
                pushedBtn.setBackground(Color.white);
            } catch (IOException ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        }
        
        
        current++;
        if(current>=questions.size()){
            current = 0;
            Collections.shuffle(questions);
        }
        
        
        
        
        
        try {
            dout.writeUTF(questions.get(current).getQ());
            type = questions.get(current).getClass().getSimpleName();
            dout.writeUTF(type);
        } catch (IOException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
        try{
            switch(type){
                case "Q1zN":
                    Q1zN q1 = (Q1zN) questions.get(current);
                    dout.writeInt(q1.getA().size());
                    for(String a: q1.getA()){
                        if(a.equals(q1.getRa().get(0))){
                            dout.writeUTF("right");

                        }
                        dout.writeUTF(a);
                    }
                    break;
                case "QXzN":
                    QXzN qX = (QXzN) questions.get(current);
                    dout.writeInt(qX.getA().size());
                    for(String a: qX.getA()){
                        boolean right = false;
                        for(String ra: qX.getRa()){
                            if(a.equals(ra)){
                                dout.writeUTF("right");
                                dout.writeUTF(ra);
                                right = true;
                                break;
                            }
                        }
                        if(!right){
                            dout.writeUTF(a);
                        }
                    }
                    break;
                case "Questions":
                    Questions q = questions.get(current);
                    dout.writeInt(q.getRa().size());
                    for(String ra: q.getRa()){
                        dout.writeUTF(ra);
                    }
            }
        } catch (IOException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }

        try {
            str = din.readUTF();
        } catch (IOException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
        if(str.equals("nothing")){

        }
        else{
            if(end(str)){
                return;
            }
            btns[Integer.parseInt(str.substring(0, str.indexOf(":")))][Integer.parseInt(str.substring(str.indexOf(":")+1))].setBackground(opponentColor);
            if(winner(opponentColor)){
                try {
                    din.close();
                    dout.close();
                    s.close();
                } catch (IOException ex) {
                    showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
                }
                showMessageDialog(null,"You lost");
                MainMenu window = new MainMenu();
                window.setVisible(true);
                dispose();
                return;
            }
            if(!draw()){
                try {
                    din.close();
                    dout.close();
                    s.close();
                } catch (IOException ex) {
                    showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
                }
                showMessageDialog(null,"Draw");
                MainMenu window = new MainMenu();
                window.setVisible(true);
                dispose();
                return;
            }
        }


        current++;
        if(current>=questions.size()){
            current = 0;
            Collections.shuffle(questions);
        }
        playing = true;
    }//GEN-LAST:event_btnSendActionPerformed

    private void btnMainMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMainMenuActionPerformed
        if(playing){
            int input = showConfirmDialog(null, "are you sure?");
            if(input == 0){
                try {
                    dout.writeUTF("end");
                    s.close();
                    din.close();
                    dout.close();
                } catch (IOException ex) {
                    showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
                }

                MainMenu window = new MainMenu();
                window.setVisible(true);
                dispose();
            }
        }
    }//GEN-LAST:event_btnMainMenuActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CreateBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreateBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreateBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreateBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CreateBoard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMainMenu;
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel labelQuest;
    // End of variables declaration//GEN-END:variables
}
