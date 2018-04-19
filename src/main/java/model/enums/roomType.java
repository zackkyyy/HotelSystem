package model.enums;

/**
 * Created by IntelliJ IDEA.
 *
 * @User: Zacky Kharboutli
 * @Date: 2018-04-14
 * @Project : HotelSystem
 */


public enum roomType {
    SINGLE {

        @Override
        public String toString() {
            return "Single";
        }
    },

    DOUBLE {

        @Override
        public String toString() {
            return "Double";
        }
    },

    TRIPLE {

        @Override
        public String toString() {
            return "Triple";
        }

    },
    APARTMENT {

        @Override
        public String toString() {
            return "Apartment";
        }

    };

        public static roomType toEnum(String str){
            str=str.toUpperCase();
            if(str==APARTMENT.toString()){
                return APARTMENT;
            }else if(str==SINGLE.toString()){
                return SINGLE;
            }else if(str==DOUBLE.toString()){
                return DOUBLE;
            }else if(str==TRIPLE.toString()){
                return TRIPLE;
            }
            return null;
        }

}
