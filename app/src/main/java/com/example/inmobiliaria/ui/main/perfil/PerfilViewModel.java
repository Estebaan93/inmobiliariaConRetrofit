//ui/main/PerfilFragment/PerfilViewModel
package com.example.inmobiliaria.ui.main.perfil;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PerfilViewModel extends AndroidViewModel {

  private MutableLiveData<String> mText;

  public PerfilViewModel(@NonNull Application application) {
    super(application);
  }

  public LiveData<String>getMText(){
    if(mText==null){
      mText= new MutableLiveData<>();
    }
    return mText;
  }


}