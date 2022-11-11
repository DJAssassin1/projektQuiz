/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package boards;

import java.awt.Color;
import java.awt.TextField;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JRadioButton;
import menus.MainMenu;

/**
 *
 * @author domin
 */
public class JoinBoard extends javax.swing.JFrame {

    /**
     * Creates new form JoinBoard
     */
    public JoinBoard() {
        initComponents();
        
    }
    
    //communication
    Socket s;
    DataOutputStream dout;
    DataInputStream din;
    String str;
    
    
    //permanent stuff
    final Color waitC = Color.gray;
    final int boardSize = 500;
    final int freeSpace = 5;
    boolean playing = false;
    final int winNum = 4;
    final int maxBtns = 14;
    
    //visuals
    JButton btns[][];
    TextField field = new TextField();
    JRadioButton[] q1zNBtns;
    JCheckBox[] qXzNBtns;
    ButtonGroup btnG = new ButtonGroup();
    
    //variables
    Color myC = Color.GREEN;
    Color enemyC = Color.blue;
    ArrayList<String> a = new ArrayList();
    boolean[] ra;
    String type;
    boolean right = true;
    int numQBtns;
    int numBtns;
    String question;
    JButton pushedBtn;
    
    
    public JoinBoard(int numBtns, Socket s, String title, String sMyC, String sEnemyC) {
        initComponents();
        this.setTitle(title);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.s = s;
        this.numBtns = numBtns;
        this.a = a;
        this.ra = ra;
        this.question = question;
        this.numQBtns = numQBtns;
        
        try {
                Field field = Class.forName("java.awt.Color").getField(sMyC);
                myC = (Color)field.get(null);
                Field field2 = Class.forName("java.awt.Color").getField(sEnemyC);
                enemyC = (Color)field2.get(null);
            } catch (Exception ex) {
                showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
            }
        
        btnSend.setVisible(false);
        
        
        try{
            dout = new DataOutputStream(s.getOutputStream());
            din = new DataInputStream(s.getInputStream());
        } catch (IOException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
        
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
        for(int i = 0; i< numBtns; i++){
            for(int j = 0; j< numBtns; j++){
               btns[i][j] = new JButton();
               JButton btn = btns[i][j];
               this.add(btn);
               btn.setBackground(Color.white);
               btn.setBounds(i*boardSize/this.numBtns, j*boardSize/this.numBtns, boardSize/this.numBtns-freeSpace, boardSize/this.numBtns-freeSpace);
                btn.addActionListener(e ->
                {
                    if(playing && btn.getBackground() == Color.white){
                        playing = false;
                        pushedBtn = btn;
                        btnSend.setVisible(true);
                        labelQuest.setText(question);
                        btn.setBackground(waitC);
                        switch(type){
                            case "Q1zN":
                                for(int m=0;m!=numQBtns;m++){
                                    q1zNBtns[m].setText(a.get(m));
                                    q1zNBtns[m].setVisible(true);
                                }
                                break;
                            case "QXzN":
                                for(int m=0;m!=numQBtns;m++){
                                    qXzNBtns[m].setText(a.get(m));
                                    qXzNBtns[m].setVisible(true);
                                }
                                break;
                            case "Questions":
                                field.setVisible(true);
                                break;
                        }
                        
                    }
                });
            }
            
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

    //first play
    public void firstPlay(){
        try{
            str = din.readUTF();
            if("nothing".equals(str)){
                
            }
            else{
                if(end(str)){
                    return;
                }
                btns[Integer.parseInt(str.substring(0, str.indexOf(":")))][Integer.parseInt(str.substring(str.indexOf(":")+1))].setBackground(enemyC);
            }
            question = din.readUTF();
            type = din.readUTF();
            numQBtns = din.readInt();
            ra = new boolean[maxBtns];
            for(int i=0; i< numQBtns ;i++){
                str = din.readUTF();
                if("right".equals(str)){
                    ra[i] = true;
                    a.add(din.readUTF());
                }
                else{
                    ra[i] = false;
                    a.add(str);
                }
            }
            playing = true;
        } catch (IOException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnMainMenu = new javax.swing.JButton();
        btnSend = new javax.swing.JButton();
        labelQuest = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnMainMenu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnMainMenu.setText("Main menu");
        btnMainMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMainMenuActionPerformed(evt);
            }
        });

        btnSend.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSend.setText("Send");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
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
                    .addComponent(btnMainMenu, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(labelQuest, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSend)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelQuest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(440, 440, 440)
                .addComponent(btnMainMenu)
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMainMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMainMenuActionPerformed
        if(playing){
            int input = showConfirmDialog(null, "are you sure?");
            if(input == 0){
                try {
                    dout.writeUTF("end");
                    din.close();
                    dout.close();
                    s.close();
                } catch (IOException ex) {
                    showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
                }

                MainMenu window = new MainMenu();
                window.setVisible(true);
                dispose();
            }
        }
    }//GEN-LAST:event_btnMainMenuActionPerformed

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
                for(String rapis : a){
                    if(rapis.equals(text)){
                        right = true;
                    }
                }
                field.setVisible(false);
                break;
        }
        if(right){
            pushedBtn.setBackground(myC);
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
            if(winner(myC)){
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
        
        
        //reading new button and question
        try{
            str = din.readUTF();
            if(str.equals("nothing")){
                
            }
            else{
                if(end(str)){
                    return;
                }
                btns[Integer.parseInt(str.substring(0, str.indexOf(":")))][Integer.parseInt(str.substring(str.indexOf(":")+1))].setBackground(enemyC);
                if(winner(enemyC)){
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
            a.removeAll(a);
            question = din.readUTF();
            type = din.readUTF();
            numQBtns = din.readInt();
            for(int i=0;i<numQBtns;i++){
                str = din.readUTF();
                if(str.equals("right")){
                    ra[i]=true;
                    a.add(din.readUTF());
                }
                else{
                    ra[i]=false;
                    a.add(str);
                }
            }
            
        } catch (IOException ex) {
            showMessageDialog(null, ex, "Error", ERROR_MESSAGE);
        }
        playing = true;
    }//GEN-LAST:event_btnSendActionPerformed

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
            java.util.logging.Logger.getLogger(JoinBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JoinBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JoinBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JoinBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JoinBoard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMainMenu;
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel labelQuest;
    // End of variables declaration//GEN-END:variables
}
