/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pod.event.publisher;

import java.io.IOException;
import java.net.Socket;

/**
 * Publicador
 * 
 * @author Pedro Arthur
 */
public class Publisher {
    
    private final static String EVENTBUS_HOST = "localhost";
    private final static Integer EVENTBUS_PORT = 10999;
    private final String identify;
    
    public Publisher(String identify) {
        this.identify = identify;
    }
    
    public void publish(String subscriberId, String texto) {
        Socket socket = null;
        try{
            //abrir uma conexão
            socket = new Socket(EVENTBUS_HOST, EVENTBUS_PORT);
            //escrever a mensagem
            Protocol protocol = new Protocol();
            protocol.setPublisher(identify);
            protocol.setSubscriber(subscriberId);
            protocol.setMessage(texto);

            System.out.println("Sending: "+new String(protocol.requestData()));
            socket.getOutputStream().write(protocol.requestData());
            //receber um OK
            byte[] b = new byte[4];
            socket.getInputStream().read(b);

            if(new String(b).equals("#OK#")) {
                System.out.println("Mensagem publicada com sucesso!");
            } else {
                System.out.println("Mensagem não publicada. Tente novamente.");
            }
        }
        catch(IOException ex) {
               ex.printStackTrace();
        } finally {
            //encerrar conexão
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public String getIdentify() {
        return this.identify;
    }
}
