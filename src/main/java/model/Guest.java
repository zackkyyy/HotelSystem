package model;

import java.time.LocalDate;

/**
 * Created by zack on 2018-03-31.
 */
public class Guest {

    String name;
    String lastName;
    String address;
    String phoneNr;
    String id;
    String identityNr;
    String creditCard;
    LocalDate birthday;
    private String notes;


    public Guest(String  id , String name, String lastName, String address, String phoneNr ,String identityNr , String creditCard , LocalDate birthday, String notes)  {
        this.id=id;
        this.name=name;
        this.lastName=lastName;
        this.address=address;
        this.phoneNr=phoneNr;
        this.birthday=birthday;
        this.creditCard=creditCard;
        this.identityNr=identityNr;
        this.notes=notes;
    }

    /**
     * Empty constructor.
     */
    public Guest (){

    }

    public Guest(String name, String lastName, String address, String phoneNr, String creditCard, String identityNr) {
        this.identityNr=identityNr;
        this.name=name;
        this.lastName=lastName;
        this.address=address;
        this.phoneNr=phoneNr;
        this.creditCard=creditCard;
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

    public String getCreditCard() {
        return creditCard;
    }

    public String getIdentityNr() {
        return identityNr;
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

    public void setIdentityNr(String identityNr) {
        this.identityNr = identityNr;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public void setId(String id){
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public String toString(Guest g){
        return g.getName()+ " "+g.getLastName();

    }

    public void setBirthday(LocalDate value) {
        this.birthday=value;
    }

    public void setNotes(String text) {
        this.notes=text;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getNotes() {
        return notes;
    }
}