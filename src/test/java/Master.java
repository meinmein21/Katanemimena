import java.net.*;
import java.io.*;
import  javax.json.*;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.stream.JsonParsingException;

public class Master  {
    private Socket socket;
    private ServerSocket server;
    private DataInputStream in;

    public int Hashfunction(String roomName) {
        return roomName.length();
    }


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
                        // Ανάλυση του JSON για να πάρεις το roomName
                        JsonObject jsonObject = Json.createReader(new StringReader(line)).readObject();
                        String roomName = jsonObject.getString("roomName");
                        // Κάλεσε την Hashfunction με το roomName ως όρισμα
                        int hashResult = Hashfunction(roomName);
                        System.out.println("Hash result for roomName " + roomName + ": " + hashResult);


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