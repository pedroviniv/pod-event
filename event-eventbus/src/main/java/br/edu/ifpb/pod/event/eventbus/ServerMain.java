/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pod.event.eventbus;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro Arthur
 */
public class ServerMain {
    
    final static String TOKEN = "---123456---";

    /**
     * Extrai os dados da mensagem - [0]: publisherId - [1]: subscriberId - [2]:
     * textMsg
     *
     * @param request
     * @return
     */
    private static String[] extract(String request) {

        if (request.startsWith(TOKEN) && request.endsWith(TOKEN)) {
            return request.replaceAll(TOKEN, "").split("\\|");
        }

        throw new RuntimeException("Mensagem estruturada incorretamente!");

    }

    public static void main(String[] args) throws IOException {

        //Instanciar elementos principais 
        Register register = new Register();
        Notifier notifier = new Notifier(register);
        MessageManager manager = new MessageManager();
        TaskManager taskManager = new TaskManager(register, manager, notifier);

        //Programar background
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        //executar uma thread depois dos primeiros 2s e daí por diante a cada 5s
        executor.scheduleAtFixedRate(taskManager, 2000, 5000, TimeUnit.MILLISECONDS);
        
        //publisher server
        Thread pubThread = new Thread() {
            @Override
            public void run() {
                try {
                    createPublisherServer(manager); //thread    
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }  
        };
        
        pubThread.start();
        
        //publisher server
        Thread subThread = new Thread() {
            @Override
            public void run() {
                try {
                    createSubscribeServer(register); //thread    
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }  
        };
        
        subThread.start();

    }

    private static void createPublisherServer(MessageManager manager) throws IOException {
        //Publisher Server
        ServerSocket publisherServer = new ServerSocket(10999);

        while (true) {
            System.out.println("\n------ Publisher Server -------\n");
            //log
            System.out.println("1. Aguardando conexão do publisher.");
            //aguardando a conexão com o publicador
            Socket clientSocket = publisherServer.accept();
            //log
            System.out.println("2. Conexão estabelecida com o publisher.");
            //log
            System.out.println("3. Lendo a requisição.");
            //fazer leitura da requisição
            byte[] bytes = new byte[1024];
            clientSocket.getInputStream().read(bytes);
            String request = new String(bytes).trim();
            //log
            System.out.println("Mensagem recebida: "+request);
            //recuperar os dados da requisição
            String[] extractedRequest = extract(request);
            String publisherId = extractedRequest[0];
            String subscriberId = extractedRequest[1];
            String textMsg = extractedRequest[2];
            String msgId = UUID.randomUUID().toString();

            //log
            System.out.println("4. Persistindo mensagem: "+Arrays.toString(extractedRequest));
            //persistir as informações
            Message message = new Message(msgId, publisherId, subscriberId, textMsg);
            manager.publish(message);
            
            //log
            System.out.println("5. Respondendo ao publisher.");
            //Informar ao publicador que a mensagem foi publicada
            clientSocket.getOutputStream().write("#OK#".getBytes());
            //log
            System.out.println("6. Encerrando Conexão.");
            //encerrando conexão
            clientSocket.close();
        }
    }

    private static void createSubscribeServer(Register register) throws IOException {
        //Subscriber Server
        
        ServerSocket subscriberServer = new ServerSocket(10998);  
        while(true) {
            System.out.println("\n------ Subscriber Server -------\n");
            
            System.out.print("1. Aguardando conexão do subscriber... ");
            //Aceita conexão com socket            
            Socket socket = subscriberServer.accept();
            System.out.println("Conexão estabelecida com o subscriber!");
            System.out.println("2. Fazendo leitura dos dados do subscriber... ");
            //Recebe mensagem
            byte[] bytes = new byte[1024];
            socket.getInputStream().read(bytes);
            String msg = new String(bytes).trim();
            System.out.println("3. Retirando os Tokens e recebendo o ID do subscriber... ");
            String subscribeId = msg.replaceAll(TOKEN, "");
            //
            System.out.println("4. Registrando o subscriber... ");
            register.register(subscribeId, socket);
            
        }
    }

}
