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

    private String guest_ID;
    private String reservation_ID;
    private String room_ID;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private int price;
    public Reservation(){

    }

    public Reservation(String reservation_ID , String guest_ID , String room_ID , LocalDate arrival, LocalDate departureDate , int price){
        this.arrivalDate = arrival;
        this.departureDate = departureDate;
        this.guest_ID=guest_ID;
        this.reservation_ID=reservation_ID;
        this.room_ID=room_ID;
        this.price=price;
    }

    /**
     * Setters and getters
     */
    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public String getGuest_ID() {
        return guest_ID;
    }

    public String getReservation_ID() {
        return reservation_ID;
    }

    public String getRoom_ID() {
        return room_ID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setReservation_ID(String reservation_ID) {
        this.reservation_ID = reservation_ID;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setGuest_ID(String guest_ID) {
        this.guest_ID = guest_ID;
    }

    public void setRoom_ID(String room_ID) {
        this.room_ID = room_ID;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
