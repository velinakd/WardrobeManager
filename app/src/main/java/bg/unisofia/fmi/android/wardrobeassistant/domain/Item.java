package bg.unisofia.fmi.android.wardrobeassistant.domain;

import android.os.Bundle;

/**
 * Created by Velina on 8.2.2015 г..
 */
public class Item {
    private String picturePath;
    private String name;
    private Integer id;
    private String type;

    public Item(){

    }

    public Item(Integer id, String picturePath, String name, String type) {
        this.picturePath = picturePath;
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public static Item fromBundle(Bundle bundle){
       Item result= null;
       if(bundle!=null && bundle.containsKey("itemId")){
           result= new Item();
           result.picturePath = bundle.getString("itemPicturePath");
           result.name = bundle.getString("itemName");
           result.id = bundle.getInt("itemId");
           result.type = bundle.getString("itemType");
       }
       return result;
    }
    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Bundle getAsBundle(){
        Bundle result=new Bundle();
        addInBundle(result);
        return result;
    }

    private void addInBundle(Bundle bundle){
        bundle.putInt("itemId", id);
        bundle.putString("itemName", name);
        bundle.putString("itemType", type);
        bundle.putString("itemPicturePath", picturePath);
    }


}
