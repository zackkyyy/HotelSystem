package model;

import org.bson.types.ObjectId;

import java.time.LocalDate;

/**
 * Created by IntelliJ IDEA.
 *
 * @User: Zacky Kharboutli
 * @Date: 2018-04-13
 * @Project : HotelSystem
 */


    public class Reservation {
    private ObjectId room;
    private ObjectId guest;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private Double price;
    private Boolean checkedIn;

    public Reservation(){

    }

    public Reservation(ObjectId room, ObjectId guest, LocalDate arrivalDate, LocalDate departureDate, Double price, Boolean checkedIn) {
        this.room = room;
        this.guest = guest;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.price = price;
        this.checkedIn = checkedIn;
    }

    public ObjectId getRoom() {
        return room;
    }

    public void setRoom(ObjectId room) {
        this.room = room;
    }

    public ObjectId getGuest() {
        return guest;
    }

    public void setGuest(ObjectId guest) {
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

    public void setCheckedIn(Boolean checkedIn) {
        this.checkedIn = checkedIn;
    }
}
