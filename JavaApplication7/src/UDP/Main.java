/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author andre
 */
public class Main {
    public static void main(String[] args) {
        int port = 50001;
        Server server = new Server(port);
        Client client = new Client(port);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(client);
        executorService.submit(server);
    }
}
