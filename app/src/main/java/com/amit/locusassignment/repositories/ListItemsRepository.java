package com.amit.locusassignment.repositories;

import android.arch.lifecycle.MutableLiveData;

import com.amit.locusassignment.models.DataMap;
import com.amit.locusassignment.models.ListItem;

import java.util.ArrayList;
import java.util.List;

public class ListItemsRepository {

    private static ListItemsRepository instance;
    private ArrayList<ListItem> dataSet = new ArrayList<>();

    public static ListItemsRepository getInstance(){
        if(instance == null){
            instance = new ListItemsRepository();
        }
        return instance;
    }


    // Pretend to get data from a webservice or online source
    public MutableLiveData<List<ListItem>> getListItems(){
        setListItems();
        MutableLiveData<List<ListItem>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void setListItems(){
        dataSet.add(new ListItem("pic1","PHOTO","Photo 1",new DataMap(null,null,100,false,"")));
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("Poor");
        stringArrayList.add("Ok");
        stringArrayList.add("Bad");
        dataSet.add(new ListItem("choice1","SINGLE_CHOICE","Choice 1",new DataMap(null,stringArrayList,100,false,"")));
        dataSet.add(new ListItem("comment1","COMMENT","Comment Photo 1",new DataMap(null,null,100,false,"")));
        dataSet.add(new ListItem("pic2","PHOTO","Photo 2",new DataMap(null,null,100,false,"")));
        dataSet.add(new ListItem("choice2","SINGLE_CHOICE","Choice 2",new DataMap(null,stringArrayList,100,false,"")));
        dataSet.add(new ListItem("comment2","COMMENT","Comment Photo 2",new DataMap(null,null,100,false,"")));
        dataSet.add(new ListItem("pic3","PHOTO","Photo 3",new DataMap(null,null,100,false,"")));
        dataSet.add(new ListItem("choice3","SINGLE_CHOICE","Choice 3",new DataMap(null,stringArrayList,100,false,"")));
        dataSet.add(new ListItem("comment3","COMMENT","Comment Photo 3",new DataMap(null,null,100,false,"")));
        dataSet.add(new ListItem("pic4","PHOTO","Photo 4",new DataMap(null,null,100,false,"")));
        dataSet.add(new ListItem("choice4","SINGLE_CHOICE","Choice 4",new DataMap(null,stringArrayList,100,false,"")));
        dataSet.add(new ListItem("comment4","COMMENT","Comment Photo 4",new DataMap(null,null,100,false,"")));

    }
}