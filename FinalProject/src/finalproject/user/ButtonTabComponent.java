package finalproject.user;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import finalproject.config.IoCon;
import io.socket.client.Socket;
import javax.swing.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.json.JSONObject;
 
/**
 * Component to be used as tabComponent;
 * Contains a JLabel to show the text and 
 * a JButton to close the tab it belongs to 
 */
public class ButtonTabComponent extends JPanel {
    private Socket mSocket = IoCon.getInstance().getmSocket();
    private final JTabbedPane pane;
    JLabel tabLabel;
    boolean talock;
    EditData ed;
 
    public ButtonTabComponent(final JTabbedPane pane , EditData room) {
        //unset default FlowLayout' gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.pane = pane;
        //umf = (UserMainFrame) this.pane.getRootPane().getParent();
        setOpaque(false);
        
        if(room != null){
            ed = room;
            JTextArea ta = room.jTextArea1;
            ta.getDocument().addDocumentListener(TextAreaListener);
            this.talock = room.talock;
        }
        //make JLabel read titles from JTabbedPane
        final JLabel label = new JLabel() {
            public String getText() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                return null;
            }
        };
        this.pane.addChangeListener(tabselected);
        
        tabLabel = label;
        add(tabLabel);
        //add more space between the label and the button
        tabLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        //tab button
        JButton button = new TabButton();
        add(button);
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }        //add more space to the top of the component

 
    private class TabButton extends JButton implements ActionListener {
        public TabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close this tab");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
        }
 
        public void actionPerformed(ActionEvent e) {
            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1) {
                //umf.leftingRoom(i);
                if(ed != null){
                    JSONObject data=new JSONObject();
                try{
                    data.put("room", ed.mRoomname);
                    mSocket.emit("roomleft",data);
                    }catch(Exception ex){}
                }
                pane.remove(i);
            }
        }
 
        //we don't want to update UI for this button
        public void updateUI() {
        }
 
        //paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
                g2.setColor(Color.BLUE);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }
 
    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }
 
        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };
    
    
    private final DocumentListener TextAreaListener = new DocumentListener() {

        @Override
        public void removeUpdate(DocumentEvent e) {

        }

        @Override
        public void insertUpdate(DocumentEvent e) {
             int i = pane.indexOfTabComponent(ButtonTabComponent.this);
             int y = pane.getSelectedIndex();
            if (i == y) {
                return;
            }
            else if(!ed.talock){
                tabLabel.setForeground(Color.red);
            }
        }

        @Override
        public void changedUpdate(DocumentEvent arg0) {
            
        }
    };
    
    private final ChangeListener tabselected  = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
            int index = sourceTabbedPane.getSelectedIndex();
            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if(i == index){
            tabLabel.setForeground(Color.BLACK);
            }
        }
          
    };
}
