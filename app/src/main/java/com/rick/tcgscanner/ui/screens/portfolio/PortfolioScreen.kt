package com.rick.tcgscanner.ui.screens.portfolio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.rick.tcgscanner.R
import com.rick.tcgscanner.TcgApplication
import com.rick.tcgscanner.data.model.PortfolioItem
import com.rick.tcgscanner.ui.navigation.Screen
import java.text.NumberFormat

@Composable
fun PortfolioRoute(navController: NavController) {
    val app = LocalContext.current.applicationContext as TcgApplication
    val viewModel: PortfolioViewModel = viewModel(
        factory = PortfolioViewModelFactory(app.appContainer.portfolioRepository)
    )
    PortfolioScreen(
        viewModel = viewModel,
        onItemClick = { item ->
            navController.navigate(Screen.PortfolioDetail.createRoute(item.id))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(viewModel: PortfolioViewModel, onItemClick: (PortfolioItem) -> Unit) {
    val items by viewModel.portfolio.collectAsState()
    val query by viewModel.searchQuery.collectAsState()

    val filtered = items.filter { item ->
        val text = query.trim().lowercase()
        text.isEmpty() ||
                item.card.name.lowercase().contains(text) ||
                (item.card.setName?.lowercase()?.contains(text) ?: false) ||
                (item.card.rarity?.lowercase()?.contains(text) ?: false)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.portfolio)) }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = viewModel::onSearch,
                label = { Text("Search") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filtered, key = { it.id }) { item ->
                    PortfolioListItem(item = item, onClick = { onItemClick(item) })
                }
            }
        }
    }
}

@Composable
private fun PortfolioListItem(item: PortfolioItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            AsyncImage(
                model = item.card.smallImage,
                contentDescription = item.card.name,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.card.name, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = listOfNotNull(item.card.setName, item.card.rarity, "×${item.quantity}")
                        .joinToString(" · "),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = NumberFormat.getCurrencyInstance().format(item.gradePrices.ungraded * item.quantity),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
