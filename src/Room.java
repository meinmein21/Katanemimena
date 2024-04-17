
import com.google.gson.Gson;
import java.util.Date;

public class Room {
    private String roomName;
    private int noOfPersons;
    private String area;
    private int noOfReviews;
    private String roomImage;
    private String availableDates;//krathseis
    private static int noOfReservations;
    private Date startDate;
    private Date endDate;


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

    public int getNoOfReservations(){
        return noOfReservations;
    }

    public Date getStartDate(){
        return startDate;
    }

    public Date getEndDate(){
        return endDate;
    }

    public void setStartDate(Date date){
        this.startDate = date;
    }

    public void setEndDate(Date date){
        this.endDate = date;
    }

    public String getAvailableDates(){
        return  availableDates;
    }
}
