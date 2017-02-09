/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pod.event.subscriber;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro Arthur
 */
public class Subscriber {

    private final static String EVENTBUS_HOST = "localhost";
    private final static Integer EVENTBUS_PORT = 10998;

    public final static String ID = "pfernandesvasconcelos@gmail.com";

    private final static String TOKEN = "---123456---";

    private Socket connect() throws IOException {
        Socket socket = new Socket(EVENTBUS_HOST, EVENTBUS_PORT);
        return socket;
    }

    private void send(Socket socket) throws IOException {

        StringBuilder builder = new StringBuilder();
        String msg = builder.append(TOKEN)
                .append(ID)
                .append(TOKEN).toString();

        socket.getOutputStream().write(msg.getBytes());
    }
    
    private String wait(Socket socket) throws IOException {
        
        InputStream is = socket.getInputStream();
        byte[] bytes = new byte[1024];
        is.read(bytes); //blocking method
        return new String(bytes).trim();
        
    }
    
    /**
     * Converte a mensagem em dados estruturados
     * - [0] publisherId
     * - [1] mensagem
     * 
     * @param msg 
     * @return 
     */
    private String[] parse(String msg) {
        return msg.replaceAll(TOKEN, "").split("\\|");
    }

    public void subscribe() {
        
        try {
            //Abre conexão com o barramento
            Socket socket = connect();
            //Solicita o registro do Subscriber
            send(socket);
            
            //Esperando mensagens
            boolean waiting = true;
            while(waiting) {
                
                //recebe mensagem
                String msg = wait(socket);
                //estrutura a mensagem
                String[] data = parse(msg);
                //notifica os dados recebidos
                notify(data[0], data[1]);
            }
            
            socket.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Subscriber.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void notify(String publisherId, String msg) {
        System.out.println(String.format("Mensagem recebida de %s: %s", publisherId, msg));
    }

}
