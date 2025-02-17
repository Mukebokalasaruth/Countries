package com.example.countries.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.countries.R
import com.example.countries.ViewModel.CountryViewModel
import com.example.countries.data.Country

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryApp(viewModel: CountryViewModel = viewModel()) {
    // Collecte des pays depuis le ViewModel
    val countries by viewModel.countries.collectAsState(initial = emptyList())  // Initialisation avec une liste vide

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Liste des Pays") })  // Titre corrigé
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(countries) { country ->
                CountryItem(country)  // Affichage de chaque item de pays
            }
        }
    }
}

@Composable
fun CountryItem(country: Country) {
    Row(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
    ) {
        // Affichage de l'image du drapeau du pays
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(country.flags.png)  // Vérifie que cette donnée existe bien
                .crossfade(true)
                .error(R.drawable.error)
                .placeholder(R.drawable.load)
                .build(),
            contentDescription = "Drapeau de ${country.name.common}",
            modifier = Modifier.width(64.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))

        // Affichage des informations du pays
        Column {
            Text(text = country.name.common, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Capitale: ${country.capital?.joinToString() ?: "N/A"}")
            Text(text = "Population: ${country.population}")
            Text(text = "Continent: ${country.continents.joinToString()}")
        }
    }
}
