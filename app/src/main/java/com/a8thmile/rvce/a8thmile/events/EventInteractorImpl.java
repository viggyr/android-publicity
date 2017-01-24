package com.a8thmile.rvce.a8thmile.events;

import android.util.Log;

import com.a8thmile.rvce.a8thmile.api.EventClient;
import com.a8thmile.rvce.a8thmile.api.ServiceGenerator;
import com.a8thmile.rvce.a8thmile.models.EventFields;
import com.a8thmile.rvce.a8thmile.models.EventResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vignesh on 23/1/17.
 */

public class EventInteractorImpl implements EventInteractor{

    @Override
    public void callEventApi(String token,final onEventRequestFinishedListener listener) {
        EventClient eventClient= ServiceGenerator.createService(EventClient.class);

        Call<EventResponse> call= eventClient.getEvent(token);

        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                Log.v("test","response "+response);
                listener.onSucess(response.body());
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                listener.onFailure("Server Communication error");
            }
        });


    }
}

