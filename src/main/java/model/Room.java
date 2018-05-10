package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import model.enums.*;

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
    private Smoking smoking;
    private Adjacent adjoined;
    private Quality quality;



    public Room(RoomType roomType, Boolean booked, int roomNr, Double price, City city) {
        this.city=city;
        this.booked = booked;
        this.roomNr = roomNr;
        this.price = price;
        this.roomType=roomType;
    }


    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }
    public Smoking getSmoking() {
        return smoking;
    }

    public void setSmoking(Smoking smoking) {
        this.smoking = smoking;
    }

    public Adjacent getAdjoined() {
        return adjoined;
    }

    public void setAdjoined(Adjacent adjoined) {
        this.adjoined = adjoined;
    }

    public Room(RoomType roomType, boolean booked, int roomNr, Double price, City city, Smoking smooking, Adjacent adjoined) {

        this.roomType = roomType;
        this.booked = booked;
        this.roomNr = roomNr;
        this.price = price;
        this.city = city;
        this.smoking = smooking;
        this.adjoined = adjoined;
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
                ", smoking=" + smoking +
                ", adjoined=" + adjoined +
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
