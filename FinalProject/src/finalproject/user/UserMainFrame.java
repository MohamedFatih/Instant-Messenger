package finalproject.user;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import finalproject.config.IoCon;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ma7moud
 */
public class UserMainFrame extends javax.swing.JFrame {
 int i;
  private Socket mSocket;
    private String mRoomname,mUsername;
    String roomname;
    ArrayList al = new ArrayList();
    String myID;
    JSONArray onlins;
    ImageIcon img = new ImageIcon("depositphotos_80914334-Science-concept-Computer-Science-in-grunge-dark-room.jpg");
    //String Publicitem,Sciitem,Ititem,Architem,Kmitem,Gisitem,Engitem,Tcomitem;   
    /**
     * Creates new form UserMainFrame
     */
    
    public void leftingRoom(int index){
        
        
        JSONObject data=new JSONObject();
try{
    data.put("room", roomname);
    mSocket.emit("roomleft",data);
    }catch(Exception ex){}
    }
    
    public UserMainFrame() {
        initComponents();
        setSize(830,Toolkit.getDefaultToolkit().getScreenSize().height-50);
        setLocationRelativeTo(null);
        //basePanel.setBackground(new Color(255,255,255));
        bar.setBackground(new Color(25,118,210));
        getContentPane().setBackground(new Color(255,255,255));
        jTabbedPane1.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        this.setIconImage(img.getImage());
        
    }
    public void sendSo(String myID,JSONObject data) throws JSONException{
    
    this.myID = myID;
    mUsername = data.getString("username");
    JSONArray onlineList = data.getJSONArray("jarr");
    for(int i=0;i<onlineList.length();i++){
                        JSONObject e = onlineList.getJSONObject(i);
                        String id =  e.getString("id");
                        al.add(id);
                    }
    onlins = onlineList;
    
      mSocket = IoCon.getInstance().getmSocket();
      
       
      //  mSocket.emit("enterroom", LogInData);
        
        mSocket.on("private", privateMessage);
        //mSocket.on("user joined", onUserJoined);
        mSocket.on("user left", onUserLeft);
        //mSocket.on("typing", onTyping);
        //mSocket.on("stop typing", onStopTyping);
        mSocket.on("suspend me", suspended);
  }

    private Emitter.Listener privateMessage = new Emitter.Listener() {
        @Override
        
       public void call(final Object... args) {
           
            try {
                JSONObject data = (JSONObject) args[0];
                String reqType = data.getString("requestType");
                String fromuser = data.getString("fromUser");
                String room = data.getString("room");
                String rec = data.getString("receiver");
                if(reqType.equals("request")){
                int choice = JOptionPane.showOptionDialog(null, 
                fromuser+" Want To Chat With You in Private Room", 
                "Private Request", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, null, null);
                  if (choice == JOptionPane.YES_OPTION)
                    {
                        JSONObject databack = new JSONObject();
                    try{
                        databack.put("choice","yes");
                        databack.put("requestType","feedback");
                        databack.put("fromUser", rec);
                        databack.put("room", rec+fromuser);
                        databack.put("receiver", fromuser);
                       }catch(Exception ex){}
                        mSocket.emit("sprivate", databack );
                        EditData PrivateRoom = new EditData();
                            PrivateRoom.setRoomname(rec+fromuser, rec);
                        jTabbedPane1.add(fromuser, PrivateRoom);
                                initTabComponent(i , PrivateRoom);i++;
                    }else{
                            JSONObject databack = new JSONObject();
                    try{
                        databack.put("choice","no");
                        databack.put("requestType","feedback");
                        databack.put("fromUser", rec);
                        databack.put("room", rec+fromuser);
                        databack.put("receiver", fromuser);
                       }catch(Exception ex){}
                        mSocket.emit("sprivate", databack );
                            }
                } else if(reqType.equals("feedback")){
                    if(data.get("choice").equals("yes")){
                        if(!checkin(fromuser)){
                            EditData PrivateRoom = new EditData();
                            PrivateRoom.setRoomname(room, rec);
                         jTabbedPane1.add(fromuser, PrivateRoom);
                                initTabComponent(i , PrivateRoom);i++;
                       }else{
                              jTabbedPane1.setSelectedIndex(gototab(fromuser));
                          }
                }else{
                        JOptionPane.showMessageDialog(null, "Your Request for Private Chat has been Refused");
                    }
             }
            } catch (JSONException ex) {
                
            }
                
            
                    }};
    
    
    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            try {
                JSONArray username = (JSONArray) args[0];
                onlins = username;
                    al.clear();
                    for(int i=0;i<username.length();i++){
                        JSONObject e = username.getJSONObject(i);
                        String id =  e.getString("id");
                        al.add(id);
                    }
            } catch (JSONException ex) {
                
            }
                 
    }};
    
    private Emitter.Listener suspended = new Emitter.Listener() {
        @Override
        
       public void call(final Object... args) {
                mSocket.emit("disconnect");
                JOptionPane.showMessageDialog(null, "You Have Been Suspended");
                mSocket.close();
                System.exit(0);
                    }};
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initTabComponent(int i, EditData room) {
        jTabbedPane1.setTabComponentAt(i , new ButtonTabComponent(jTabbedPane1 , room));
        jTabbedPane1.setSelectedIndex(i);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jButton9 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        bar = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        jButton9.setText("jButton9");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Campus Instant Messenger");
        setBackground(new java.awt.Color(255, 64, 129));
        getContentPane().setLayout(null);

        jTabbedPane1.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                panelRemoved(evt);
            }
        });
        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(170, 50, 630, 520);

        jLabel1.setText("CHAT ROOMS");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 26, 100, 14);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/FinalDesign/icons/ic_desktop_mac_black_36dp.png"))); // NOI18N
        jButton1.setText("SCI");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/FinalDesign/icons/ic_all_inclusive_black_18dp.png"))); // NOI18N
        jButton2.setText("Public");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/FinalDesign/icons/ic_language_black_18dp.png"))); // NOI18N
        jButton3.setText("IT");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/FinalDesign/icons/ic_speaker_phone_black_18dp.png"))); // NOI18N
        jButton4.setText("TCOM");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/FinalDesign/icons/ic_videocam_black_18dp.png"))); // NOI18N
        jButton6.setText("GIS");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/FinalDesign/icons/ic_build_black_18dp.png"))); // NOI18N
        jButton5.setText("ENG");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(255, 255, 255));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/FinalDesign/icons/ic_local_library_black_18dp.png"))); // NOI18N
        jButton7.setText("KM");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(255, 255, 255));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/FinalDesign/icons/ic_rate_review_18pt_3x.png"))); // NOI18N
        jButton8.setText("ARCH");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane1.setViewportView(jPanel1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(0, 50, 170, 520);

        bar.setBackground(new java.awt.Color(233, 30, 99));
        bar.setBorder(null);
        bar.setMinimumSize(new java.awt.Dimension(56, 50));
        bar.setPreferredSize(new java.awt.Dimension(56, 80));

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/FinalDesign/icons/ic_storage_white_24dp.png"))); // NOI18N
        jMenu4.setToolTipText("Logout");
        bar.add(jMenu4);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/FinalDesign/icons/ic_mode_edit_white_24dp.png"))); // NOI18N

        jMenuItem2.setText("Edit username");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        bar.add(jMenu2);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/FinalDesign/icons/ic_group_white_24dp.png"))); // NOI18N
        jMenu3.setToolTipText("");
        jMenu3.setAlignmentX(200.0F);

        jMenuItem3.setText("online students");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        bar.add(jMenu3);

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/FinalDesign/icons/ic_power_settings_new_white_48pt.png"))); // NOI18N

        jMenuItem1.setText("logout");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        bar.add(jMenu1);

        setJMenuBar(bar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newRoom(String roomName){
        if(!checkin(roomName)){
          EditData room = new EditData();
          room.setRoomname(roomName, myID);
          jTabbedPane1.add(roomName, room);
            initTabComponent(i , room);i++;
      }else{
           jTabbedPane1.setSelectedIndex(gototab(roomName));
       }
    }
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
      newRoom(jButton2.getText());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        newRoom(jButton5.getText());
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        mSocket.emit("disconnect");
        this.setVisible(false);
            new UserLog().setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
       if(!checkin(jMenuItem2.getText())){
           ChangeInformation ci = new ChangeInformation();
           ci.data(myID,mUsername);
        jTabbedPane1.add(jMenuItem2.getText(), ci);
            initTabComponent(i , null);i++;
       }else{
           jTabbedPane1.setSelectedIndex(gototab(jMenuItem2.getText()));
       }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        newRoom(jButton1.getText());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
       if(!checkin(jMenuItem3.getText())){
           StudentsOnline2 so = new StudentsOnline2();
        so.PassData(myID, onlins , al , this);
        jTabbedPane1.add(jMenuItem3.getText(), so);
            initTabComponent(i , null);i++;
       }else{
           jTabbedPane1.setSelectedIndex(gototab(jMenuItem3.getText()));
       }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        newRoom(jButton3.getText());
    }//GEN-LAST:event_jButton3ActionPerformed

    private void panelRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_panelRemoved
      i--;
    }//GEN-LAST:event_panelRemoved

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        newRoom(jButton4.getText());
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        newRoom(jButton6.getText());
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        newRoom(jButton7.getText());
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        newRoom(jButton8.getText());
    }//GEN-LAST:event_jButton8ActionPerformed

    int gototab(String s){
        int check=0;
        for(int i=0;i<jTabbedPane1.getTabCount();i++){
            if(s.equals(jTabbedPane1.getTitleAt(i))){
                check = i;
            }
        }
        return check;
    }
    boolean checkin(String s){
        boolean check = false;
        for(int i=0;i<jTabbedPane1.getTabCount();i++){
            if(s.equals(jTabbedPane1.getTitleAt(i))){
                check = true;
            }
        }
        return check;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//        try {
//            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//    
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(UserMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(UserMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(UserMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(UserMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserMainFrame().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar bar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    protected javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
