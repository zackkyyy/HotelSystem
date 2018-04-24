package model;

import java.time.LocalDate;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Zacky Kharboutli
 * @Date: 2018-04-13
 * @Project : HotelSystem
 */


public class CheckIn {
    private String guest_ID;
    private String room_ID;
    private LocalDate date;
    private String checkin_ID;
    private String reservation_ID;

    public CheckIn() {

    }

    public CheckIn(String checkin_id, String reservation_id , String guest_ID , String room_ID, LocalDate date){
        this.checkin_ID =checkin_id;
        this.guest_ID=guest_ID;
        this.date=date;
        this.room_ID=room_ID;
        this.reservation_ID =reservation_id;
    }


    @Override
    public String toString() {
        return super.toString();
    }

    public String getCheckin_ID() {
        return checkin_ID;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getGuest_ID() {
        return guest_ID;
    }

    public String getReservation_id() {
        return reservation_ID;
    }

    public void setReservation_id(String reservation_id) {
        this.reservation_ID = reservation_id;
    }

    public String getRoom_ID() {
        return room_ID;
    }

    public void setCheckin_ID(String checkin_ID) {
        this.checkin_ID = checkin_ID;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setGuest_ID(String guest_ID) {
        this.guest_ID = guest_ID;
    }

    public void setRoom_ID(String room_ID) {
        this.room_ID = room_ID;
    }

}
