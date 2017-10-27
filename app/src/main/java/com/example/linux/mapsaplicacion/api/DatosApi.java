package com.example.linux.mapsaplicacion.api;

import com.example.linux.mapsaplicacion.models.WifiGratis;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by linux on 21/10/17.
 */

public interface DatosApi {
    @GET("f4kx-n3nn.json")
    Call<List<WifiGratis>> obtenerListaPeajes();
}
