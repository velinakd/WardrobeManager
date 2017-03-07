package bg.unisofia.fmi.android.wardrobeassistant;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bg.unisofia.fmi.android.wardrobeassistant.db.DatabaseContract;
import bg.unisofia.fmi.android.wardrobeassistant.db.DatabaseHelper;
import bg.unisofia.fmi.android.wardrobeassistant.domain.Item;


public class EditItemActivity extends ActionBarActivity {

    private static final String PICTURES_ALBUM_NAME = "WardrobeAssistant";
    private static final String LOG_TAG = "WardrobeAssistant";
    static final int REQUEST_TAKE_PHOTO = 1;
    private static int nextId=0;
    private DatabaseHelper mDbHelper;

    private String mCurrentPicturePath;
    private EditText mItemNameET;
    private EditText mItemTypeET;
    private ImageView mItemPictureIV;
    private Item mItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        mItemNameET = (EditText) findViewById(R.id.itemNameEditText);
        mItemTypeET = (EditText) findViewById(R.id.itemTypeEditText);
        mItemPictureIV = (ImageView) findViewById(R.id.imageView);

        Intent intent = getIntent();
        mItem = Item.fromBundle(intent.getExtras());
        if(mItem!=null){
            mItemNameET.setText(mItem.getName());
            mItemTypeET.setText(mItem.getType());
            mCurrentPicturePath = mItem.getPicturePath();
            try {
                Uri uri = Uri.parse(mItem.getPicturePath());
                mItemPictureIV.setImageURI(uri);
            } catch (Exception e) {
                //It's ok
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        Log.e(LOG_TAG, Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString());
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getAlbumStorageDir(PICTURES_ALBUM_NAME);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
      //  mCurrentPicturePath = "file:" + image.getAbsolutePath();
        mCurrentPicturePath =  image.getAbsolutePath();
        return image;
    }

    public void takePicture(View view) {
        if(isExternalStorageWritable()) {
            Log.i("WardrobeAssistant", "--------!!!!external storage writable");
            dispatchTakePictureIntent();
        } else {
            //TODO Notify user
            Log.i("WardrobeAssistant", "--------!!!!external storage not writable");
        }
    }

    private void dispatchTakePictureIntent() {
        Log.i("WardrobeAssistant", "--------!!!!Dispatch method");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                Log.i("WardrobeAssistant", "exception");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                Log.i("WardrobeAssistant", "--------!!!!picture is not null");
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void saveItemInDB(){
        DatabaseHelper mDbHelper = new DatabaseHelper(this);
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String itemName = mItemNameET.getText().toString();
        String itemType = mItemTypeET.getText().toString();


// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Item.COLUMN_NAME_NAME, itemName);
        values.put(DatabaseContract.Item.COLUMN_NAME_TYPE, itemType);
        values.put(DatabaseContract.Item.COLUMN_NAME_PICTURE_PATH, mCurrentPicturePath);

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        if(mItem == null) {
            newRowId = db.insert(
                    DatabaseContract.Item.TABLE_NAME,
                    null,
                    values);
        } else {
            String selection = DatabaseContract.Item._ID + " = ?";
            String[] selectionArgs = { String.valueOf(mItem.getId())};

            int count = db.update(
                    DatabaseContract.Item.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }
    }

    public void saveItem(View view) {
        saveItemInDB();
        Intent intent = new Intent(this, ItemsListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new  File(mCurrentPicturePath);
            if(imgFile.exists()){
                Bitmap imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                mItemPictureIV = (ImageView) findViewById(R.id.imageView);
                mItemPictureIV.setImageBitmap(imageBitmap);
            }
        }
    }

}
