package model.enums;

/**
 * Created by IntelliJ IDEA.
 *
 * @User: Zacky Kharboutli
 * @Date: 2018-05-10
 * @Project : HotelSystem
 */


public enum Quality {
    ONESTAR {
        @Override
        public String toString() {
            return "★";
        }
    },


    TWOSTARS {
        @Override
        public String toString() {
            return "★★";
        }
    },
    THREESTARS {
        @Override
        public String toString() {
            return "★★★";
        }
    },
    FOURSTARS {
        @Override
        public String toString() {
            return "★★★★";
        }
    },
    FIVESTARS {
        @Override
        public String toString() {
            return "★★★★★";
        }
    };
    public static Quality toEnum(String str){
        if(str.equals("★")){
            return ONESTAR;
        } else if(str.equals("★★")){
            return TWOSTARS;
        } else if(str.equals("★★★")){
            return THREESTARS;
        } else if(str.equals("★★★★")){
            return FOURSTARS;
        } else if(str.equals("★★★★★")){
            return FIVESTARS;
        }
        return null;
    }


}
