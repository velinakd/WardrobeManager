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
import android.widget.Button;
import android.widget.TextView;

import bg.unisofia.fmi.android.wardrobeassistant.db.DatabaseContract;
import bg.unisofia.fmi.android.wardrobeassistant.db.DatabaseHelper;
import bg.unisofia.fmi.android.wardrobeassistant.domain.Outfit;


public class OutfitsListActivity extends ActionBarActivity {


    private DatabaseHelper mDbHelper;
    private Outfit[] mOutfits;
    private int mCurrentIndex=0;
    private Outfit mCurrentOutfit;
    private TextView mOutfitStyleTV;
    private TextView mOutfitWeatherTV;
    private TextView mOutfitIdTV;

    private Button mNextButton;
    private Button mPrevButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfits_list);
        mDbHelper=new DatabaseHelper(this);

        mOutfitStyleTV= (TextView) findViewById(R.id.styleTV);
        mOutfitWeatherTV= (TextView) findViewById(R.id.weatherTV);
        mOutfitIdTV= (TextView) findViewById(R.id.outfitIdTV);
        mNextButton =(Button) findViewById(R.id.nextButton);
        mPrevButton =(Button) findViewById(R.id.prevButton);



    }


    public void deleteOutfit(View view){
        if(mCurrentOutfit!=null){
            mDbHelper.deleteOutfit(mCurrentOutfit);
            loadOutfitsFromDb();
        };
    }

    public void showNextOutfit(View view){
        Log.e("------------!!!!!!!!!!!  Next", ""+mCurrentIndex);
        mCurrentIndex++;
        updateDisplayedOutfit();
    }



    public void showPreviousOutfit(View view){
        Log.e("------------!!!!!!!!!!!  Prev", ""+mCurrentIndex);
        mCurrentIndex--;
        updateDisplayedOutfit();
    }


    private void updateDisplayedOutfit() {
        if(mCurrentIndex>=0 && mOutfits!=null && mCurrentIndex<mOutfits.length){
            mCurrentOutfit = mOutfits[mCurrentIndex];
        }

        if(mCurrentOutfit!=null){
            mOutfitIdTV.setText(String.valueOf(mCurrentOutfit.getId()));
            mOutfitStyleTV.setText(mCurrentOutfit.getStyle());
            mOutfitWeatherTV.setText(mCurrentOutfit.getWeather());

            mPrevButton.setVisibility(mCurrentIndex>0? View.VISIBLE : View.GONE );
            mNextButton.setVisibility(mOutfits!=null && mCurrentIndex<mOutfits.length-1 ? View.VISIBLE : View.GONE );

            FragmentManager fm = getFragmentManager();
            OutfitFragment updatedFragment=OutfitFragment.newInstance(mCurrentOutfit.getId());
            FragmentTransaction transaction = fm.beginTransaction();

            transaction.replace(R.id.mainPannelLayout, updatedFragment);
            Log.e("------------!!!!!!!!!!!  fragment replaced", "");
            transaction.commit();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        loadOutfitsFromDb();
    }

    private void loadOutfitsFromDb() {
        mOutfits = mDbHelper.loadAllOutfits();
        updateDisplayedOutfit();
    }


    public void addOutfit(View view){
        Intent intent = new Intent(OutfitsListActivity.this, EditOutfitActivity.class);
        startActivity(intent);
    }

    public void editOutfit(View view){
        Intent intent = new Intent(OutfitsListActivity.this, EditOutfitActivity.class);
        if(mCurrentOutfit!=null){
            intent.putExtras(mCurrentOutfit.getAsBundle());
        }
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_outfits_list, menu);
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
