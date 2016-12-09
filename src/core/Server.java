package core;

import sun.net.ConnectionResetException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Server {
    private String filePath;
    private int portNumber;

    public Server(String filePath, int portNumber) {
        this.filePath = filePath;
        this.portNumber = portNumber;

    }


    public String readRegister() {
        String contacts = "";

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNext()) {
                contacts += scanner.nextLine().replace(",", " ") + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    public void startServer() {
        new Thread(
                () -> {
                    try {
                        ServerSocket serverSocket = new ServerSocket(portNumber);

                        while (true) {
                            Socket clientSocket = serverSocket.accept();

                            InputStream inputStream = clientSocket.getInputStream();
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                            OutputStream outputStream = clientSocket.getOutputStream();
                            PrintWriter writer = new PrintWriter(outputStream);

                            BufferedReader reader = new BufferedReader(inputStreamReader);
                            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                                if (line.equals("getall")) {
                                    writer.println(readRegister());
                                    writer.flush();
                                    System.out.println("Contacts sent to Address Book");
                                } else if (line.equals("exit")) {
                                    System.out.println("Shutting down session.");
                                    break;
                                }
                            }

                            reader.close();
                            writer.close();
                            clientSocket.close();
                        }
                    } catch (SocketException e){
                        System.out.println("Connection to client lost.");
                    }
                    catch (Exception e) {
                        System.out.println("An error occurred" + " " + e);
                    }

                }).start();
    }
}