/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kephis.ecs_kesws;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author kim
 */
public class MiddlewareService {

    /**
     * @param args the command line arguments
     */
    
    private static ExecutorService executor = null;
    private static volatile Future incomingMessageTask = null;
    private static volatile Future outgoingMessageTask = null;
    public  void main(String[] args) { 
     executor = Executors.newFixedThreadPool(3);
 
        while (true)
        {
            try {
            startMessageProcessors();
            Thread.sleep(1000);
        } catch (Exception e) {
            System.err.println("Caught exception: " + e.getMessage());
        }
    }
}

private static void startMessageProcessors() throws Exception {
        if (incomingMessageTask == null
                || incomingMessageTask.isDone()
                || incomingMessageTask.isCancelled())
        {
         //incomingMessageTask = executor.submit(new IncomingMessageProcessor());
        }
 
        if (outgoingMessageTask == null
                || outgoingMessageTask.isDone()
                || outgoingMessageTask.isCancelled())
        {
             //outgoingMessageTask = executor.submit(new OutgoingMessageProcessor());
        }
    }
    }
    

