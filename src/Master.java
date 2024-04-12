import java.net.*;
import java.io.*;
import com.google.gson.Gson;

//
public class Master  {
    private Socket socket;
    private ServerSocket server;
    private DataInputStream in;
    private Gson gson;

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

            gson = new Gson(); // Initialize Gson instance
            //mallon JSON
            String line = "";

            while(!line.equals("Goodbye")){ // Αλλαγή εδώ

                try{
                    line = in.readUTF();
                    if (!line.equals("Goodbye")) { // Έλεγχος πριν την ανάγνωση
                        System.out.println("Received room's data from Manager: " + line);
                        Room room = gson.fromJson(line, Room.class);

                        // Print room details
                        System.out.println("Room Name: " + room.getRoomName());
                        System.out.println("No. of Persons: " + room.getNoOfPersons());
                        System.out.println("Area: " + room.getArea());
                        System.out.println("No. of Reviews: " + room.getNoOfReviews());
                        System.out.println("Room Image: " + room.getRoomImage());

                        // Call Hashfunction with roomName as argument
                        int hashResult = Hashfunction(room.getRoomName());
                        System.out.println("Hash result for roomName " + room.getRoomName() + ": " + hashResult);


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