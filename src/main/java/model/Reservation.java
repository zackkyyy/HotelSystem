package model;

import java.time.LocalDate;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Zacky Kharboutli
 * @Date: 2018-04-13
 * @Project : HotelSystem
 */
    public class Reservation {
    private int room;
    private String guest;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private Double price;
    private Boolean checkedIn;

    public Reservation(){

    }

    public Reservation(int room, String guest, LocalDate arrivalDate, LocalDate departureDate, Double price, Boolean checkedIn) {
        this.room = room;
        this.guest = guest;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.price = price;
        this.checkedIn = checkedIn;
    }

    public Reservation(int roomID, String guestID, LocalDate arrivalDate, LocalDate departureDate, Double price) {
        this.room = roomID;
        this.guest = guestID;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.price = price;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
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

    @Override
    public String toString() {
        return "Reservation{" +
                "room =" + room +
                ", guest =" + guest +
                ", arrivalDate =" + arrivalDate +
                ", departureDate =" + departureDate +
                ", price =" + price +
                '}';
    }

    public void setCheckedIn(Boolean checkedIn) {
        this.checkedIn = checkedIn;
    }
}
