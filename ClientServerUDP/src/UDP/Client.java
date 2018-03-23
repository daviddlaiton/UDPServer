/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author andre
 */
public class Client {

    public static void main(String args[]) throws Exception {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Por favor la dirección IP del servidor");
        String ipAdrress = inFromUser.readLine();
        System.out.println("Por favor ingrese el puerto");
        int port = Integer.parseInt(inFromUser.readLine());
        System.out.println("Por favor ingrese el número de objetos a enviar");

        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(ipAdrress);
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        String sentence = inFromUser.readLine();
        int numberObj = Integer.parseInt(sentence);
        System.out.println("Número de objetos: " + numberObj);

        String aEnviar;
        Date fecha;
        long mS = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        for (int i = 0; i < numberObj; i++) {
            fecha = new Date(mS);

            aEnviar = "numeroSecuencia= " + (i + 1) + " numTotalObj= " + numberObj + " marcaTiempo= " + sdf.format(fecha);
            System.out.println(aEnviar);

            sendData = aEnviar.getBytes();
            mS = System.currentTimeMillis();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            clientSocket.send(sendPacket);
        }
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        String modifiedSentence = new String(receivePacket.getData());
        System.out.println(modifiedSentence);
        clientSocket.close();
    }

}
