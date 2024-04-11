
import  javax.json.*;
import java.io.DataOutputStream;

import java.net.*;
import java.io.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.stream.JsonParsingException;


import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;

public class ManagerApp{
    //test
    private Socket socket;
    private BufferedReader in; //take input from user
    private DataOutputStream out; //send it to socket

    public ManagerApp(String address, int port){
        try{ //try to establish connection
            socket = new Socket(address,port); //we make a socket object for that client
            System.out.println("Conncected"); //we didnt connect to the server. We connected to the socket

            in = new BufferedReader(new InputStreamReader(System.in));

            out = new DataOutputStream(socket.getOutputStream());
            //ask json file from user
            while (true) {
                System.out.print("Enter Room's path or type 'Exit' to exit ");
                String filePath = in.readLine();
                if (filePath.equalsIgnoreCase("Exit")) {
                    out.writeUTF("Goodbye"); //otan grafeis Exit stelnei munhma Goodbye ston Master gia na kleisei swsta ton server
                    break;

                }
                //read json file
                StringBuilder jsoncontent = new StringBuilder();
                //edw brother den kserw ti sto poutso einai auta alla fantazomai einai apla o tropos pou ta diabazei
                try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
                    String line ;
                    while ((line = fileReader.readLine()) != null){
                        jsoncontent.append(line);
                    }
                    //Room room = parseRoomFromJSON(jsoncontent.toString());
                    //sendRoomDataToServer(room);
                }
                out.writeUTF(jsoncontent.toString());
                System.out.println("Room's data sent to the server");
            }
        }
        catch(UnknownHostException e){
            System.out.println("error : unkown host ");
            System.out.println(e);
        }
        catch(IOException e){
            System.out.println("error i/o exception");
            System.out.println(e);
        }
        finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket !=null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private Room parseRoomFromJSON(String jsonData) {
        Room room = new Room();

        try {
            JsonReader jsonReader = Json.createReader(new StringReader(jsonData));
            JsonObject jsonRoom = jsonReader.readObject();

            room.setRoomName(jsonRoom.getString("roomName"));
            room.setNoOfPersons(jsonRoom.getInt("noOfPersons"));
            room.setArea(jsonRoom.getString("area"));
            room.setNoOfReviews(jsonRoom.getInt("noOfReviews"));
            room.setRoomImage(jsonRoom.getString("roomImage"));

            jsonReader.close();
            System.out.println("Room Name: " + room.getRoomName());
        } catch (JsonParsingException e) {
            e.printStackTrace();
        }

        return room;
    }

    private void sendRoomDataToServer(Room room) {

        JsonObject jsonRoom = Json.createObjectBuilder()
                .add("roomName", room.getRoomName())
                .add("noOfPersons", room.getNoOfPersons())
                .add("area", room.getArea())
                .add("noOfReviews", room.getNoOfReviews())
                .add("roomImage", room.getRoomImage())
                .build();

        JsonWriter jsonWriter = Json.createWriter(out);
        jsonWriter.writeObject(jsonRoom);
        jsonWriter.close();

    }


    public static void main(String args[]){
        ManagerApp client = new ManagerApp("127.0.0.1", 6666);
    }
}
