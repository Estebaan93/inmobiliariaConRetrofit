//data/request/ApiClient
package com.example.inmobiliaria.data.request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
  public static final String URL_BASE= "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/";
  private static InmobiliariaService service;
  public static InmobiliariaService getApiInmobiliaria(){
    if(service==null){
      Gson gson= new GsonBuilder()
              .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
              .create();
      Retrofit retrofit= new Retrofit.Builder()
              .baseUrl(URL_BASE)
              .addConverterFactory(ScalarsConverterFactory.create())
              .addConverterFactory(GsonConverterFactory.create(gson))
              .build();

      service= retrofit.create(InmobiliariaService.class);
    } return service;

  }

}
