package model;

import java.io.IOException;

/**
 * Created by zack on 2018-03-31.
 */
public class Guest {

    String name;
    String lastName;
    String address;
    String phoneNr;


    public Guest(String name, String lastName, String address, String phoneNr ) throws IOException {
        this.name=name;
        this.lastName=lastName;
        this.address=address;
        this.phoneNr=phoneNr;
    }

    /**
     * Empty constructor.
     */
    public Guest (){

    }


    public String getName(){
        return name ;
    }
    public String getLastName(){
        return lastName;
    }
    public String getAddress (){
        return address;
    }
    public String getPhoneNr() { return phoneNr; }
    public void setName(String name){
        this.name=name;
    }
    public void setAddress(String address){
        this.address=address;
    }
    public void setLastName(String lastName ){
        this.lastName=lastName;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }


}