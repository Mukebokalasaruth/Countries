package com.example.countries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.countries.ui.theme.CountriesTheme
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CountriesTheme {
                CountryApp()
            }
        }
    }
}

@Composable
fun CountryApp() {
    // Etat pour la région choisie et les pays récupérés
    var region by remember { mutableStateOf("home") }
    var countries by remember { mutableStateOf<List<String>>(emptyList()) }
    var query by remember { mutableStateOf("") }

    // Lancer la récupération des pays en fonction de la région choisie
    LaunchedEffect(region) {
        if (region == "home") return@LaunchedEffect
        fetchCountries(region) { countries = it }
    }

    // Page d'accueil : Choix de la région
    if (region == "home") {
        // Page d'accueil : Choix entre afficher tous les pays ou les pays d'Afrique
        HomePage { selectedRegion ->
            region = selectedRegion
        }
    } else {
        // Afficher la liste des pays une fois la région sélectionnée
        val filteredCountries = countries.filter {
            it.contains(query, ignoreCase = true)
        }

        Column {
            // Barre de recherche pour filtrer les pays
            SearchBar(query) { query = it }

            // Affichage des pays filtrés
            CountryList(filteredCountries)
        }
    }
}

@Composable
fun HomePage(onRegionSelected: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Message "Karibu"
        Text("Karibu", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        // Choix de la région
        Button(
            onClick = { onRegionSelected("all") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voir tous les pays", style = MaterialTheme.typography.titleLarge)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onRegionSelected("africa") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voir tous les pays d'Afrique", style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = { Text("Rechercher un pays...") },
        singleLine = true
    )
}

@Composable
fun CountryList(countries: List<String>) {
    LazyColumn {
        items(countries.size) { index ->
            Text(
                text = countries[index],
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

fun fetchCountries(region: String, updateCountries: (List<String>) -> Unit) {
    val url = when (region) {
        "africa" -> "https://restcountries.com/v3.1/region/africa"
        "all" -> "https://restcountries.com/v3.1/all"
        else -> ""
    }

    try {
        val result = URL(url).readText()
        val jsonArray = JSONArray(result)
        val countryList = mutableListOf<String>()

        // Extraction des noms de pays et de leurs drapeaux
        for (i in 0 until jsonArray.length()) {
            val country = jsonArray.getJSONObject(i)
            val name = country.getJSONObject("name").getString("common")
            val flag = country.getString("flags")
            countryList.add("$name $flag")
        }

        // Mise à jour des pays dans l'état
        updateCountries(countryList)
    } catch (e: Exception) {
        // Gérer les erreurs de réseau ici
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CountryApp()
}
