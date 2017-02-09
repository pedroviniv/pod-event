/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pod.event.eventbus;

/**
 * Classe que representa uma mensagem enviada por um publisher
 * @author Pedro Arthur
 */
public class Message {
    
    private String identify;
    private String publisherId;
    private String subscriberId;
    private String text;

    public Message(String identify, String publisherId, String subscriberId, String text) {
        this.identify = identify;
        this.publisherId = publisherId;
        this.subscriberId = subscriberId;
        this.text = text;
    }

    public String getIdentify() {
        return identify;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public String getText() {
        return text;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    //Num mundo real deveria ser implementado um equals e hashcode

    @Override
    public String toString() {
        return "Message{" + "identify=" + identify + ", publisherId=" + publisherId + ", subscriberId=" + subscriberId + ", text=" + text + '}';
    }
}
