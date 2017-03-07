package bg.unisofia.fmi.android.wardrobeassistant;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import bg.unisofia.fmi.android.wardrobeassistant.db.DatabaseHelper;
import bg.unisofia.fmi.android.wardrobeassistant.domain.Outfit;


public class EditOutfitActivity extends ActionBarActivity {

    Outfit mOutfit;
    DatabaseHelper mDatabaseHelper;
    EditText mOutfitStyleET;
    EditText mOutfitWeatherET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_outfit);
        mDatabaseHelper = new DatabaseHelper(this);
        Intent intent = getIntent();
        mOutfit = Outfit.fromBundle(intent.getExtras());
        mOutfitStyleET = (EditText) findViewById(R.id.outfitStyleET);
        mOutfitWeatherET = (EditText) findViewById(R.id.outfitWeatherET);



    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mOutfit==null){
            //Outfit doesn't exist. We have to create it now to be able to add items to it
            mOutfit= mDatabaseHelper.insertEmptyOutfit();
        } else{
            mOutfitStyleET.setText(mOutfit.getStyle());
            mOutfitWeatherET.setText(mOutfit.getWeather());
        }
        FragmentManager fm = getFragmentManager();
        OutfitFragment updatedFragment=OutfitFragment.newInstance(mOutfit.getId());
        FragmentTransaction transaction = fm.beginTransaction();

        transaction.replace(R.id.editOutfitLayout, updatedFragment);
        Log.e("------------!!!!!!!!!!!  fragment replaced", "");
        transaction.commit();
       // fm.findFragmentById(R.id.editOutfitFragment);
    }

    public void addItemToOutfit(View view){
        Intent intent = new Intent(EditOutfitActivity.this, ItemsListActivity.class);
        intent.putExtras(mOutfit.getAsBundle());
        startActivity(intent);
    }

    public void saveOutfit(View view){
        mOutfit.setStyle(mOutfitStyleET.getText().toString());
        mOutfit.setWeather(mOutfitWeatherET.getText().toString());

        mDatabaseHelper.updateOutfit(mOutfit);
        Intent intent = new Intent(EditOutfitActivity.this, OutfitsListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_outfit, menu);
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
