package controller;

import model.Guest;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 *
 * This class if a file handler where we write and read a file that contain all
 * the guest that has booked a room on this system
 *
 * @User: Zacky Kharboutli
 * @Date: 2018-04-06
 * @Project : HotelSystem
 */


public class FileHandler {

    private int membersNr;
    private File membersFile = new File("Guests.txt");

    public FileHandler()  {

    }

    public ArrayList<Guest> readFile() throws IOException, NumberFormatException {
        ArrayList<Guest> membersList = new ArrayList<Guest>();

        if (!membersFile.exists() ){
            membersFile.createNewFile();
            return membersList;
        }

        Scanner scan = new Scanner(membersFile);

        while (scan.hasNext()) {
            String[] temp = scan.nextLine().split(";");
            membersList.add(new Guest(temp[0], temp[1], temp[2]));
        }
        scan.close();


        return membersList;
    }



    public void writeFile(ArrayList<Guest> memberlist) throws IOException {
        StringBuilder members = new StringBuilder();
        PrintWriter writer = new PrintWriter(membersFile);
        for(Guest m: memberlist){
            members.append(m.getName()+";"+m.getLastName()+";"+m.getAddress()+"\n");

        }
        //write the members file
        membersFile.createNewFile();
        writer.print(members.toString());
        writer.close();
    }

    public int getMembersNr() {
        return membersNr;
    }


}
