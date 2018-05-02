package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import model.enums.City;
import model.enums.RoomType;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Zacky Kharboutli
 * @Date: 2018-04-13
 * @Project : HotelSystem
 */
public class Room extends RecursiveTreeObject<Room> {
    private RoomType roomType;
    private boolean booked;
    private int roomNr;
    private Double price ;
    private City city;

    public Room(RoomType roomType,Boolean booked, int roomNr, Double price, City city) {
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

    public model.enums.RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(model.enums.RoomType roomType) {
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

    public model.enums.City getCity() {
        return city;
    }
    public String getStringCity(){
        return city.toString();
    }

    public void setCity(model.enums.City city) {
        this.city = city;
    }
}
