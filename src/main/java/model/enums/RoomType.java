package model.enums;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Zacky Kharboutli
 * @Date: 2018-04-14
 * @Project : HotelSystem
 */


public enum RoomType {
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

	public static RoomType toEnum(String str){
		if(str.equals("Apartment")){
			return APARTMENT;
		} else if(str.equals("Single")){
			return SINGLE;
		} else if(str.equals("Double")){
			return DOUBLE;
		} else if(str.equals("Triple")){
			return TRIPLE;
		}
		return null;
	}

}
