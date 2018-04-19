package model;

import java.time.LocalDate;

/**
 * Created by IntelliJ IDEA.
 *
 * @User: Zacky Kharboutli
 * @Date: 2018-04-13
 * @Project : HotelSystem
 */


public class Reservation {

    private String guestName;
    private String reservation_ID;
    private String roomNr;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private int price;
    private Boolean checkedIn;

    public Reservation(){

    }

    public Reservation(String guestName, String reservation_ID, String roomNr, LocalDate arrivalDate, LocalDate departureDate, int price, Boolean checkedIn) {
        this.guestName = guestName;
        this.reservation_ID = reservation_ID;
        this.roomNr = roomNr;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.price = price;
        this.checkedIn = checkedIn;
    }

    public String getGuestName() {

        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getReservation_ID() {
        return reservation_ID;
    }

    public void setReservation_ID(String reservation_ID) {
        this.reservation_ID = reservation_ID;
    }

    public String getRoomNr() {
        return roomNr;
    }

    public void setRoomNr(String roomNr) {
        this.roomNr = roomNr;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Boolean getCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(Boolean checkedIn) {
        this.checkedIn = checkedIn;
    }
}
