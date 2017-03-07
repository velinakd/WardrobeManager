package bg.unisofia.fmi.android.wardrobeassistant.db;

import android.provider.BaseColumns;

/**
 * Created by Velina on 7.2.2015 г..
 */
public class DatabaseContract {
    public DatabaseContract(){};

    public static abstract class Item implements BaseColumns{
        public static final String TABLE_NAME="item";
        public static final String COLUMN_NAME_NAME="name";
        public static final String COLUMN_NAME_TYPE="type";
        public static final String COLUMN_NAME_PICTURE_PATH="picturepath";

    }

    public static abstract class Outfit implements BaseColumns{
        public static final String TABLE_NAME="outfit";
        public static final String COLUMN_NAME_STYLE="style";
        public static final String COLUMN_NAME_WEATHER="weather";
    }

    public static abstract class OutfitItem implements BaseColumns{
        public static final String TABLE_NAME="OutfitItem";
        public static final String COLUMN_NAME_OUTFIT_ID="outfitid";
        public static final String COLUMN_NAME_ITEM_ID="itemid";

    }



    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ITEMS =
            "CREATE TABLE " + Item.TABLE_NAME + " (" +
                    Item._ID + " INTEGER PRIMARY KEY," +
                    Item.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    Item.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP +
                    Item.COLUMN_NAME_PICTURE_PATH + TEXT_TYPE +
            " )";

    public static final String SQL_CREATE_OUTFITS =
            "CREATE TABLE " + Outfit.TABLE_NAME + " (" +
                    Outfit._ID + " INTEGER PRIMARY KEY," +
                    Outfit.COLUMN_NAME_STYLE + TEXT_TYPE + COMMA_SEP +
                    Outfit.COLUMN_NAME_WEATHER + TEXT_TYPE +
                    " )";

    public static final String SQL_CREATE_OUTFIT_ITEMS =
            "CREATE TABLE " + OutfitItem.TABLE_NAME + " (" +
                    OutfitItem._ID + " INTEGER PRIMARY KEY," +
                    OutfitItem.COLUMN_NAME_ITEM_ID+ INTEGER_TYPE + COMMA_SEP +
                    OutfitItem.COLUMN_NAME_OUTFIT_ID + INTEGER_TYPE +
                    " )";

    public static final String SQL_DELETE_ITEMS =
            "DROP TABLE IF EXISTS " + Item.TABLE_NAME;
    public static final String SQL_DELETE_OUTFITS =
            "DROP TABLE IF EXISTS " + Outfit.TABLE_NAME;
    public static final String SQL_DELETE_OUTFIT_ITEMS =
            "DROP TABLE IF EXISTS " + OutfitItem.TABLE_NAME;

}
