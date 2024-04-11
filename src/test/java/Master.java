import java.net.*;

import java.io.*;

public class Master  {
    private Socket socket;
    private ServerSocket server;
    private DataInputStream in;

    public Master(int port){
        try{
            server = new ServerSocket(port);
            System.out.println("Server Started");

            System.out.println("Waiting for client...");

            //we accept the manager app in the given port and create a socket
            //we now have established a connection between manager app and server on the socket
            socket = server.accept();
            System.out.println("Client accepted");

            //takes input from the manager app socket
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            //mallon JSON
            String line = "";

            while(!line.equals("Goodbye")){ // Αλλαγή εδώ

                try{
                    line = in.readUTF();
                    if (!line.equals("Goodbye")) { // Έλεγχος πριν την ανάγνωση
                        System.out.println("Received room's data from Manager: " + line);
                    }
                }
                catch(IOException e){
                    System.out.println(e);
                }
            }


            System.out.println("Closing connection");
            socket.close();
            in.close();
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

    public static void main(String args[]){
        Master server = new Master(6666);
    }
}