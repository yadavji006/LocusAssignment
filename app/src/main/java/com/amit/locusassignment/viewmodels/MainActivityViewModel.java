package com.amit.locusassignment.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.amit.locusassignment.models.ListItem;
import com.amit.locusassignment.repositories.ListItemsRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<ListItem>> mListItems;
    private ListItemsRepository mRepo;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();

    public void init(){
        if(mListItems != null){
            return;
        }
        mRepo = ListItemsRepository.getInstance();
        mListItems = mRepo.getListItems();
    }

    public LiveData<List<ListItem>> getListItems(){
        return mListItems;
    }

    public LiveData<Boolean> getIsUpdating(){
        return mIsUpdating;
    }
}
