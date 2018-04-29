package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import model.enums.city;
import model.enums.roomType;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Zacky Kharboutli
 * @Date: 2018-04-13
 * @Project : HotelSystem
 */


public class Room extends RecursiveTreeObject<Room> {
    private roomType roomType;
    private boolean booked;
    private int roomNr;
    private Double price ;
    private city city;


    public Room(roomType roomType,Boolean booked, int roomNr, Double price, city city) {
        this.city=city;
        this.booked = booked;
        this.roomNr = roomNr;
        this.price = price;
        this.roomType=roomType;
    }


    public Room() {
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomType=" + roomType +
                ", booked=" + booked +
                ", roomNr=" + roomNr +
                ", price=" + price +
                ", city=" + city +
                '}';
    }

    public model.enums.roomType getRoomType() {
        return roomType;
    }

    public void setRoomType(model.enums.roomType roomType) {
        this.roomType = roomType;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public int getRoomNr() {
        return roomNr;
    }

    public void setRoomNr(int roomNr) {
        this.roomNr = roomNr;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public model.enums.city getCity() {
        return city;
    }
    public String getStringCity(){
        return city.toString();
    }

    public void setCity(model.enums.city city) {
        this.city = city;
    }
}
