package finalproject.user;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import finalproject.config.IoCon;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ma7moud
 */
public class StudentsOnline2 extends javax.swing.JPanel {
    private Socket mSocket;
    String myID;
    JSONArray jar;
    ArrayList al;
    UserMainFrame umf;
    /**
     * Creates new form StudentsOnline
     */
    
    public StudentsOnline2() {
        initComponents();
        mSocket = IoCon.getInstance().getmSocket();
        mSocket.on("user left", onUserLeft);
    }
    
    void PassData(String id , JSONArray list , ArrayList arrayl , UserMainFrame umf){
        myID = id;
        jar = list;
        this.al = arrayl;
        this.umf = umf;
        
        DefaultListModel model = new DefaultListModel();
                    al.clear();
                    for(int i=0;i<list.length();i++){
            try {
                JSONObject e = list.getJSONObject(i);
                if(!myID.equals(e.getString("id"))){
                    model.addElement(e.getString("name"));
                    al.add(e.getString("id"));
                }
            } catch (JSONException ex) {
                Logger.getLogger(StudentsOnline2.class.getName()).log(Level.SEVERE, null, ex);
            }
                    }
                    jList.setModel(model);
        
    }
    
    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            try {
                JSONArray username = (JSONArray) args[0];
                    DefaultListModel model = new DefaultListModel();
                    al.clear();
                    for(int i=0;i<username.length();i++){
                        JSONObject e = username.getJSONObject(i);
                        String id =  e.getString("id");
                        if(!myID.equals(e.getString("id"))){
                            model.addElement(e.getString("name"));
                            al.add(id);
                        }
                        //System.out.println(id+"\n");
                    }
            jList.setModel(model);
            } catch (JSONException ex) {}
                 
    }};

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(13, 71, 161));

        jPanel1.setBackground(new java.awt.Color(13, 71, 161));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jList.setBackground(new java.awt.Color(13, 71, 161));
        jList.setForeground(new java.awt.Color(255, 255, 255));
        jList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { " " };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList.setAlignmentX(0.0F);
        jList.setAlignmentY(0.0F);
        jList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UsersListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Online Students");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(131, 131, 131)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(125, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel1)
                .addGap(43, 43, 43)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(81, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(315, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(229, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void UsersListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UsersListMouseClicked
         if(evt.getClickCount() == 2 ){
             int index = jList.locationToIndex(evt.getPoint());
             
             if(!umf.checkin(String.valueOf(al.get(index)))){
            
            JSONObject data=new JSONObject();
        try{
            data.put("requestType","request");
            data.put("fromUser", myID);
            data.put("room", myID+al.get(index));
            data.put("receiver", al.get(index));
           }catch(Exception ex){}
            mSocket.emit("sprivate", data );
//            Room privateRoom = new Room();
//            privateRoom.setRoomname(myID+al.get(index),mUsername);
//            privateRoom.setLocationRelativeTo(this);
//            privateRoom.setVisible(true);
            //System.out.println(myID+al.get(index));
        }
             else{
                 umf.jTabbedPane1.setSelectedIndex(umf.gototab((String)al.get(index)));
             }
         }
    }//GEN-LAST:event_UsersListMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}