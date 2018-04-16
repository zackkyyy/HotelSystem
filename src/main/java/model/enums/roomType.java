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

}
