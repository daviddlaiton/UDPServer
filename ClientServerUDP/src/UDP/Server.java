/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author andre
 */
public class Server {

    private ArrayList clientes;

    public Server(ArrayList clt) {
        clientes = clt;
    }

    public static void main(String args[]) throws Exception {

        ArrayList clientes = new ArrayList();
        System.out.println(args[0]);
        int portServer = Integer.parseInt(args[0]);
        Server s = new Server(clientes);
        DatagramSocket serverSocket = new DatagramSocket(portServer);
        byte[] receiveData = new byte[1024];

        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            s.procesarInfo(serverSocket, receivePacket);
            System.out.println("Llego");
        }
    }

    public void procesarInfo(DatagramSocket serverSocket, DatagramPacket receivePacket) throws Exception {

        serverSocket.receive(receivePacket);
        String ip = receivePacket.getAddress().toString();
        int port = receivePacket.getPort();
        boolean existe = false;
        Cliente c = null;
        for (int i = 0; i < clientes.size() && !existe; i++) {
            Cliente e = (Cliente) clientes.get(i);
            if (e.getIP().equals(ip) && e.getPuerto() == port) {
                existe = true;
                c = e;
                System.out.println("existe");
            }

        }

        if (!existe) {
            c = new Cliente(port, ip);
            clientes.add(c);
            System.out.println("no existe");
        }

        String sentence = new String(receivePacket.getData());

        DatagramPacket sendPacket = c.recibirLinea(sentence, receivePacket);
        serverSocket.send(sendPacket);
    }

    public class Cliente {

        private int puerto;
        private String dirIP;
        PrintWriter writer;
        double suma = 0;
        double numObtLlegaron = 0;
        int numObj = 0;

        public Cliente(int port, String ip) throws Exception {
            puerto = port;
            dirIP = ip;
            writer = new PrintWriter("Client" + dirIP.substring(1) + ".txt", "UTF-8");
        }

        public int getPuerto() {
            return puerto;
        }

        public String getIP() {
            return dirIP;
        }

        public void setIP(String ip) {
            dirIP = ip;
        }

        public void setPuerto(int port) {
            puerto = port;
        }

        public DatagramPacket recibirLinea(String sentence, DatagramPacket receivePacket) throws Exception {
            byte[] sendData = new byte[1024];
            String fecha;

            
            if (sentence.charAt(0) == '@') {
                System.out.println("Promedio: " + (suma / numObtLlegaron));
                writer.println("Promedio: " + (suma / numObtLlegaron));
                System.out.println("Numero de objetos recibidos: " + numObtLlegaron);
                writer.println("Numero de objetos recibidos: " + numObtLlegaron);
                System.out.println("Numero de objetos faltantes: " + (numObj - numObtLlegaron));
                writer.println("Numero de objetos faltantes: " + (numObj - numObtLlegaron));
                writer.close();
            } else {
                numObtLlegaron++;
                String[] splitSentence = sentence.split(" ");
                numObj = Integer.parseInt(splitSentence[3]);
                fecha = splitSentence[5] + " " + splitSentence[6];
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                Date d = sdf.parse(fecha);
                long fin = System.currentTimeMillis();
                long init = d.getTime();
                long tiempo = fin - init;
                writer.println(numObtLlegaron + ": " + tiempo + " ms");
                suma += tiempo;
            }
            InetAddress IPAddress = receivePacket.getAddress();
            String capitalizedSentence = sentence.toUpperCase();
            sendData = capitalizedSentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, puerto);

            return sendPacket;
        }
    }

}
