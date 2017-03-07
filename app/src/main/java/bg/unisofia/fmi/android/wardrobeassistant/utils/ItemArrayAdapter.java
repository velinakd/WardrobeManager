package bg.unisofia.fmi.android.wardrobeassistant.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

import bg.unisofia.fmi.android.wardrobeassistant.R;
import bg.unisofia.fmi.android.wardrobeassistant.domain.Item;

/**
 * Created by Velina on 8.2.2015 г..
 */
public class ItemArrayAdapter extends BaseAdapter {
    private  Item[] mItems;
    private Context mActivity;

    public ItemArrayAdapter(Item[] items, Context activity){
        super();
      //  Log.e("------------!!!!!!!!!!!  creating item array ", ""+items);
        this.mItems = items;
        this.mActivity=activity;
    }

    @Override
    public int getCount() {
       // Log.e("------------!!!!!!!!!!!  creating item count ", ""+(mItems!=null? mItems.length:0));
        return mItems!=null ? mItems.length:0;
    }

    @Override
    public Object getItem(int position) {

       // return mItems[position];
        return null;
    }

    @Override
    public long getItemId(int position) {
      //  Log.e("------------!!!!!!!!!!!  creating item view  id", "");
  //     return mItems[position].getId();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      //  Log.e("------------!!!!!!!!!!!  creating item view ", "");
        View v = convertView;
        LayoutInflater vi = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.item_gallery_child, null);
     //   Log.e("------------!!!!!!!!!!!  creating item view ", "");
        try {

            ImageView imageView = (ImageView) v.findViewById(R.id.itemGalleryImageView);
            //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            // imageView.setPadding(8, 8, 8, 8);
            Item currentItem = mItems[position];
            try{
                Uri uri = Uri.parse(currentItem.getPicturePath());
                Bitmap bmp = decodeURI(uri.getPath());
                // BitmapFactory.decodeFile(mUrls[position].getPath());
                imageView.setImageBitmap(bmp);
            } catch (Exception e){
                // the path is not valid. It is ok.
            }

            //bmp.
            TextView txtName = (TextView) v.findViewById(R.id.itemGalleryTextView);
            txtName.setText(currentItem.getName());
        } catch (Exception e) {
            Log.e("------------!!!!!!!!!!! Problem creating item view ", "", e);
        }
        return v;
    }

    /**
     * This method is to scale down the image
     */
    public Bitmap decodeURI(String filePath){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Only scale if we need to
        // (16384 buffer for img processing)
        Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math.abs(options.outWidth - 100);
        if(options.outHeight * options.outWidth * 2 >= 16384){
            // Load, scaling to smallest power of 2 that'll get it <= desired dimensions
            double sampleSize = scaleByHeight
                    ? options.outHeight / 100
                    : options.outWidth / 100;
            options.inSampleSize =
                    (int)Math.pow(2d, Math.floor(
                            Math.log(sampleSize)/Math.log(2d)));
        }

        // Do the actual decoding
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[512];
        Bitmap output = BitmapFactory.decodeFile(filePath, options);

        return output;
    }
}
