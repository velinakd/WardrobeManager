package bg.unisofia.fmi.android.wardrobeassistant.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bg.unisofia.fmi.android.wardrobeassistant.domain.Item;
import bg.unisofia.fmi.android.wardrobeassistant.domain.Outfit;

/**
 * Created by Velina on 7.2.2015 г..
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WardrobeAssistant.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.SQL_CREATE_ITEMS);
        db.execSQL(DatabaseContract.SQL_CREATE_OUTFITS);
        db.execSQL(DatabaseContract.SQL_CREATE_OUTFIT_ITEMS);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DatabaseContract.SQL_DELETE_ITEMS);
        db.execSQL(DatabaseContract.SQL_DELETE_OUTFITS);
        db.execSQL(DatabaseContract.SQL_DELETE_OUTFIT_ITEMS);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public Item[] loadItemsForOutfit(long outfitId){
        List<Item> items = new ArrayList<Item>();

        String selectQuery = "SELECT i.* FROM " + DatabaseContract.OutfitItem.TABLE_NAME + " oi, "
                + DatabaseContract.Item.TABLE_NAME + " i WHERE oi."
                + DatabaseContract.OutfitItem.COLUMN_NAME_ITEM_ID + " = i." + DatabaseContract.Item._ID + " AND oi." + DatabaseContract.OutfitItem.COLUMN_NAME_OUTFIT_ID
                + " = " + outfitId;

        Log.e("WardrobeAssistant", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Item td = extractItemFromCursor(c);
                items.add(td);
            } while (c.moveToNext());
        }
       Item[] result = new Item[items.size()];
        return  items.toArray(result);
    }


    public static Item extractItemFromCursor(Cursor cc){
        String itemPicturePath = cc.getString(cc.getColumnIndex(DatabaseContract.Item.COLUMN_NAME_PICTURE_PATH));
        String itemName = cc.getString(cc.getColumnIndex(DatabaseContract.Item.COLUMN_NAME_NAME));
        Integer itemId = cc.getInt(cc.getColumnIndex(DatabaseContract.Item._ID));
        String itemType = cc.getString(cc.getColumnIndex(DatabaseContract.Item.COLUMN_NAME_TYPE));
        Item result =  new Item(itemId, itemPicturePath, itemName, itemType);
        return result;
    }

    public  Outfit insertOutfit(Outfit outfit) {

        SQLiteDatabase db = getWritableDatabase();
// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(DatabaseContract.Outfit.COLUMN_NAME_STYLE, outfit.getStyle());
        values.put(DatabaseContract.Outfit.COLUMN_NAME_WEATHER, outfit.getWeather());

// Insert the new row, returning the primary key value of the new row
        long newRowId;

        newRowId = db.insert(
                DatabaseContract.Outfit.TABLE_NAME,
                null,
                values);
        outfit.setId(newRowId);
        return outfit;

    }

    public Outfit insertEmptyOutfit() {
        return  insertOutfit(new Outfit());

    }

    public long insertOutfitItem(Long mOutfitId, Integer itemId) {
        SQLiteDatabase db = getWritableDatabase();




// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(DatabaseContract.OutfitItem.COLUMN_NAME_OUTFIT_ID, mOutfitId);
        values.put(DatabaseContract.OutfitItem.COLUMN_NAME_ITEM_ID, itemId);

// Insert the new row, returning the primary key value of the new row
        long newRowId;

        newRowId = db.insert(
                DatabaseContract.OutfitItem.TABLE_NAME,
                null,
                values);
        return newRowId;
    }

    public Outfit[] loadAllOutfits() {
        SQLiteDatabase db = getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                DatabaseContract.Outfit._ID,
                DatabaseContract.Outfit.COLUMN_NAME_STYLE,
                DatabaseContract.Outfit.COLUMN_NAME_WEATHER,
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                DatabaseContract.Outfit._ID + " DESC";

        String selection=null;
        String[] selectionArgs=null;
        Cursor c = db.query(
                DatabaseContract.Outfit.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        List<Outfit> outfits = new ArrayList<Outfit>();
        if (c.moveToFirst()) {
            do {
                Outfit td = extractOutfitFromCursor(c);
                outfits.add(td);
            } while (c.moveToNext());
        }
        Outfit[] result = new Outfit[outfits.size()];
        return  outfits.toArray(result);

    }

    private Outfit extractOutfitFromCursor(Cursor cc) {
        Outfit result=new Outfit();
        String outfitWeather = cc.getString(cc.getColumnIndex(DatabaseContract.Outfit.COLUMN_NAME_WEATHER));
        String outfitStyle = cc.getString(cc.getColumnIndex(DatabaseContract.Outfit.COLUMN_NAME_STYLE));
        Long outfitId = cc.getLong(cc.getColumnIndex(DatabaseContract.Outfit._ID));
        result.setId(outfitId);
        result.setWeather(outfitWeather);
        result.setStyle(outfitStyle);
        return result;
    }

    public void deleteOutfit(Outfit outfit) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = DatabaseContract.Outfit._ID + " = ?";
        String[] selectionArgs = { String.valueOf(outfit.getId())};
        db.delete(DatabaseContract.Outfit.TABLE_NAME, selection, selectionArgs);
    }

    public void updateOutfit(Outfit outfit) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(DatabaseContract.Outfit.COLUMN_NAME_STYLE, outfit.getStyle());
        values.put(DatabaseContract.Outfit.COLUMN_NAME_WEATHER, outfit.getWeather());

        String selection = DatabaseContract.Outfit._ID + " = ?";
        String[] selectionArgs = { String.valueOf(outfit.getId())};

        int count = db.update(
                DatabaseContract.Outfit.TABLE_NAME,
                values,
                selection,
                selectionArgs);


    }
}
