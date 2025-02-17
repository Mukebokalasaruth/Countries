package com.example.countries.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countries.data.Country
import com.example.countries.model.CountryApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CountryViewModel : ViewModel() {
    private val  _countries= MutableStateFlow<List<Country>>(emptyList())
    val  countries = _countries.asStateFlow()

    private val api = CountryApi.create()

    init {
        fetchCountries()
    }

    private  fun  fetchCountries(){
        viewModelScope.launch {
            try {
                _countries.value = api.getCountries()

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}