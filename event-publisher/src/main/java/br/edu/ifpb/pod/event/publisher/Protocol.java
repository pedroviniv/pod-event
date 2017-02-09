/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pod.event.publisher;

/**
 * Protocolo de Comunicação com o Barramento de Evento
 *
 * @author Pedro Arthur
 */
public class Protocol {
    
    private final static String TOKEN = "---123456---";
    private String publisher;
    private String message;
    private String subscriber; 
    
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }
    
    /**
     * Cria a mensagem estruturada usando o protocolo
     * @return mensagem protocolada
     */
    public byte[] requestData() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(TOKEN)
                .append(publisher)
                .append("|")
                .append(subscriber)
                .append("|")
                .append(message)
                .append(TOKEN);
        
        return stringBuilder.toString().getBytes();
    }
    
}
