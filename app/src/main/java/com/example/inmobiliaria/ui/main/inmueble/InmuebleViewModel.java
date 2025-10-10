//ui/main/inmueble/InmuebleViewModel
package com.example.inmobiliaria.ui.main.inmueble;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InmuebleViewModel extends AndroidViewModel {
  private MutableLiveData<String> mText;

  public InmuebleViewModel(@NonNull Application application) {
    super(application);
  }

  public LiveData<String>getMText(){
    if(mText==null){
      mText= new MutableLiveData<>();
    }
    return mText;
  }


}