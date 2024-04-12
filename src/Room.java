import com.google.gson.Gson;

public class Room {
    private String roomName;
    private int noOfPersons;
    private String area;
    private int noOfReviews;
    private String roomImage ;


    public Room(){
    }

    public String getRoomName() {
        return roomName;
    }


    public int getNoOfPersons() {
        return noOfPersons;
    }


    public String getArea() {
        return area;
    }

    public int getNoOfReviews() {
        return noOfReviews;
    }


    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setNoOfPersons(int noOfPersons) {
        this.noOfPersons = noOfPersons;
    }

    public void setNoOfReviews(int noOfReviews) {
        this.noOfReviews = noOfReviews;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
