import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class Reducer {
    private Map<String, List<Room>> roomsMap; //saves rooms lists by key

    public Reducer() {
        roomsMap = new HashMap<>();
    }

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
}
