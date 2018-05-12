package model;

import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Zacky Kharboutli
 * @Date: 2018-04-13
 * @Project : HotelSystem
 */
    public class Reservation {
    private ObjectId id;
    private ArrayList<Integer> rooms;
    private String guest;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private Double price;
    private Boolean checkedIn;

    public Reservation(){

    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getCheckedIn() {
        return checkedIn;
    }


    public ArrayList<Integer> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Integer> rooms) {
        this.rooms = rooms;
    }
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }


    public void setCheckedIn(Boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", rooms=" + rooms +
                ", guest='" + guest + '\'' +
                ", arrivalDate=" + arrivalDate +
                ", departureDate=" + departureDate +
                ", price=" + price +
                ", checkedIn=" + checkedIn +
                '}';
    }
}
