/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pod.event.publisher;

/**
 *
 * @author Pedro Arthur
 */
public class ClientMain {
    
    
    public static void main(String[] args) {
        
        String subscriber = "pfernandesvasconcelos@gmail.com";
        
        Publisher publisher = new Publisher("fulano@email.com");
        publisher.publish(subscriber, "Meu nome é fulanoo! :D");
    }
}
