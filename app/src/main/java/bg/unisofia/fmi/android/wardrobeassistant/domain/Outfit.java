package bg.unisofia.fmi.android.wardrobeassistant.domain;

import android.os.Bundle;

import bg.unisofia.fmi.android.wardrobeassistant.db.DatabaseContract;

/**
 * Created by Velina on 8.2.2015 г..
 */
public class Outfit {

    private Long id;
    private String style;
    private String weather;


    public static Outfit fromBundle(Bundle bundle){
        Outfit result= null;
        if(bundle!=null && bundle.containsKey("outfitId")){
            result= new Outfit();
            result.style = bundle.getString("outfitStyle");
            result.weather = bundle.getString("outfitWeather");
            result.id = bundle.getLong("outfitId");
        }
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Bundle getAsBundle(){
        Bundle result=new Bundle();
        addInBundle(result);
        return result;
    }

    private void addInBundle(Bundle bundle){
        bundle.putLong("outfitId", id);
        bundle.putString("outfitWeather", weather);
        bundle.putString("outfitStyle", style);
    }

}
