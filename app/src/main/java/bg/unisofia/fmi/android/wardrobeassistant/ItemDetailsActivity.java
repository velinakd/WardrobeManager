package bg.unisofia.fmi.android.wardrobeassistant;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import bg.unisofia.fmi.android.wardrobeassistant.db.DatabaseContract;
import bg.unisofia.fmi.android.wardrobeassistant.db.DatabaseHelper;
import bg.unisofia.fmi.android.wardrobeassistant.domain.Item;


public class ItemDetailsActivity extends ActionBarActivity {

    Item mItem;
    TextView mItemNameTV;
    TextView mItemTypeTV;
    ImageView mItemPictureIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Intent intent = getIntent();
        mItem = Item.fromBundle(intent.getExtras());
        mItemNameTV = (TextView) findViewById(R.id.itemNameTV);
        mItemTypeTV = (TextView) findViewById(R.id.itemTypeTV);
        mItemPictureIV = (ImageView) findViewById(R.id.itemPictureIV);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mItemNameTV.setText(mItem.getName());
        mItemTypeTV.setText(mItem.getType());
        try {
            Uri uri = Uri.parse(mItem.getPicturePath());
            mItemPictureIV.setImageURI(uri);
        } catch (Exception e) {
            //It's ok
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_details, menu);
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

    public void editItem(View view) {
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtras(mItem.getAsBundle());
        startActivity(intent);
    }

    private void deleteItemFromDatabase() {
        DatabaseHelper mDbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selection = DatabaseContract.Item._ID + " = ?";
        String[] selectionArgs = { String.valueOf(mItem.getId())};
        db.delete(DatabaseContract.Item.TABLE_NAME, selection, selectionArgs);
    }

    public void deleteItem(View view) {
        deleteItemFromDatabase();
        Intent intent = new Intent(this, ItemsListActivity.class);
        startActivity(intent);
    }
}
