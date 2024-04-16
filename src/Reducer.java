import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class Reducer {
    private ServerSocket serverSocket;
    private Socket socket;
    private Map<String, List<Room>> roomsMap; //saves rooms lists by key

    public Reducer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            while (true){
                Socket socket = serverSocket.accept();
                DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                List<Room> receivedRooms = (List<Room>) in.readObject();


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
/*
    //add rooms list
    public synchronized void addRooms(String key, List<Room> rooms){
        roomsMap.put(key, rooms);
    }

    //merge all lists
    public List<Room> reduce(){
        List<Room> finalRooms = new ArrayList<>();
        for (List<Room> rooms :roomsMap.values()){
            finalRooms.addAll(rooms);

        }
        return finalRooms;
    }
    */
}
