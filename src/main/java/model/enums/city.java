package model.enums;

/**
 * Created by IntelliJ IDEA.
 *
 * @User: Zacky Kharboutli
 * @Date: 2018-04-14
 * @Project : HotelSystem
 */


public enum  city {
    VÄXJÖ {

        @Override
        public String toString() {
            return "Växjö";
        }
    },


    KALMAR {

        @Override
        public String toString() {
            return "Kalmar";
        }
    };

    public static city toEnum(String str){
        str=str.toUpperCase();
        if(str.equals(KALMAR.toString())){
            return KALMAR;
        }else if(str.equals(VÄXJÖ.toString())){
            return VÄXJÖ;
        }
        return null;
    }
}
