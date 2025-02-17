package com.example.countries.model

import com.example.countries.data.Country
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface CountryApi {
    @GET("v3.1/all")
    suspend fun getCountries(): List<Country>
    companion object{
        fun create(): CountryApi{
            return Retrofit.Builder()
                .baseUrl("https://restcountries.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CountryApi::class.java)
        }
    }
}