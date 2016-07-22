/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package finalproject.config;

import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mohamed
 */
public class IoCon {
    private Socket mSocket;
    
    private IoCon() {
         try {
           mSocket = IO.socket("http://localhost:3000");
       } catch (URISyntaxException ex) {}
        
        mSocket.connect();
    }

    public Socket getmSocket() {
        return mSocket;
    }
    
    public void reset(){
        mSocket.disconnect();
        try {
           mSocket = IO.socket("http://localhost:3000");
       } catch (URISyntaxException ex) {}
        
        mSocket.connect();
    }
    
    public static IoCon getInstance() {
        return IoConHolder.INSTANCE;
    }
    
    private static class IoConHolder {

        private static final IoCon INSTANCE = new IoCon();
    }
}
