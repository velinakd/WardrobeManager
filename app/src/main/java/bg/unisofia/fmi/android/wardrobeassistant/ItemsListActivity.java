package bg.unisofia.fmi.android.wardrobeassistant;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.URI;

import bg.unisofia.fmi.android.wardrobeassistant.db.DatabaseContract;
import bg.unisofia.fmi.android.wardrobeassistant.db.DatabaseHelper;
import bg.unisofia.fmi.android.wardrobeassistant.domain.Item;
import bg.unisofia.fmi.android.wardrobeassistant.domain.Outfit;
import bg.unisofia.fmi.android.wardrobeassistant.utils.ItemArrayAdapter;


public class ItemsListActivity extends ActionBarActivity {
    private DatabaseHelper mDbHelper;

    private Outfit mOutfit;


    private Item[] mItems =null;

    private GridView gridview = null;
  //  private Cursor cc = null;
    private ProgressDialog myProgressDialog = null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
         mDbHelper = new DatabaseHelper(this);
        Button mAddItemButton = (Button) findViewById(R.id.addItemButton);
        mOutfit = Outfit.fromBundle(getIntent().getExtras());
        if(mOutfit!=null){
            mAddItemButton.setVisibility(View.GONE);
        } else{
            mAddItemButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadItemsFromDb();
    }

    public void showAddItemScreen(View view) {
        Intent redirect = new Intent(this, EditItemActivity.class);
        startActivity(redirect);
    }

    public void loadItemsFromDb(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                DatabaseContract.Item._ID,
                DatabaseContract.Item.COLUMN_NAME_NAME,
                DatabaseContract.Item.COLUMN_NAME_TYPE,
                DatabaseContract.Item.COLUMN_NAME_PICTURE_PATH,
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                DatabaseContract.Item._ID + " DESC";

        String selection=null;
        String[] selectionArgs=null;
        Cursor cursor = db.query(
                DatabaseContract.Item.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );



        cursor.moveToFirst();
        // if Cursor is contains results
        if (cursor != null) {
            fillItemsInGrid(cursor);
           // }
        }




    }



    private void fillItemsInGrid(Cursor cursor){
        final Cursor cc = cursor;
        myProgressDialog = new ProgressDialog(ItemsListActivity.this);
        myProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       // myProgressDialog.setMessage(getResources().getString(R.string.pls_wait_txt));
        myProgressDialog.setMessage("Please wait");
        //myProgressDialog.setIcon(R.drawable.blind);
        myProgressDialog.show();
      //  Log.e("------------!!!!!!!!!!!", ""+cursor.getCount());
  //      new Thread() {
  //          public void run() {
                try {
                  //  Log.e("------------!!!!!!!!!!! In Thread", ""+cc.getCount());
                    cc.moveToFirst();
                    mItems= new Item[cc.getCount()];

                    for (int i = 0; i < cc.getCount(); i++) {
                     //   Log.d("------------!!!!!!!!!!! In Thread adding item id", ""+i);
                        cc.moveToPosition(i);
                        mItems[i]= DatabaseHelper.extractItemFromCursor(cc);
                   //     Log.d("------------!!!!!!!!!!! In Thread adding item", ""+mItems[i]);
                        //Log.e("mNames[i]",mNames[i]+":"+cc.getColumnCount()+ " : " +cc.getString(3));
                    }

                } catch (Exception e) {
                    Log.e("------------!!!!!!!!!!! Exception ", "", e);
                }
                myProgressDialog.dismiss();
 //           }
  //      }.start();
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ItemArrayAdapter(mItems, this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
               if(mOutfit==null){
                   // we are just browsing items. Show item details
                   Intent i = new Intent(ItemsListActivity.this, ItemDetailsActivity.class);
                   Log.e("intent : ", "" + position);
                   Item selectedItem = mItems[position];
                   i.putExtras(selectedItem.getAsBundle());
                   startActivity(i);
               } else {
                   // we are adding an item to a outfit. Save outfitItem and show outfit edit screen
                   long relationId = mDbHelper.insertOutfitItem(mOutfit.getId(), mItems[position].getId());
                   Log.e("------------!!!!!!!!!!! Item added to outfit ", ""+relationId);
                   Intent i = new Intent(ItemsListActivity.this, EditOutfitActivity.class);
                   i.putExtras(mOutfit.getAsBundle());
                   startActivity(i);
               }

            }
        });

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_items_list, menu);
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
}
