package com.rick.tcgscanner.ui.screens.portfolio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.rick.tcgscanner.R
import com.rick.tcgscanner.TcgApplication
import com.rick.tcgscanner.data.model.GradePrices
import com.rick.tcgscanner.data.model.PortfolioItem
import com.rick.tcgscanner.ui.screens.carddetail.GradePriceField

@Composable
fun PortfolioDetailRoute(portfolioId: Int, onBack: () -> Unit) {
    val app = LocalContext.current.applicationContext as TcgApplication
    val viewModel: PortfolioDetailViewModel = viewModel(
        factory = PortfolioDetailViewModelFactory(
            app.appContainer.portfolioRepository,
            portfolioId
        )
    )
    PortfolioDetailScreen(viewModel = viewModel, onBack = onBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioDetailScreen(viewModel: PortfolioDetailViewModel, onBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Card details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cancel))
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is PortfolioDetailUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is PortfolioDetailUiState.Error -> Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
                is PortfolioDetailUiState.Success -> PortfolioDetailContent(
                    item = state.item,
                    viewModel = viewModel,
                    onSaved = onBack
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PortfolioDetailContent(
    item: PortfolioItem,
    viewModel: PortfolioDetailViewModel,
    onSaved: () -> Unit
) {
    val conditions = listOf("Mint", "Near Mint", "Excellent", "Good", "Played", "Poor")
    var conditionExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(
            model = item.card.largeImage,
            contentDescription = item.card.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        )

        Text(text = item.card.name, style = MaterialTheme.typography.headlineSmall)
        if (!item.card.setName.isNullOrBlank()) {
            Text(text = "${item.card.setName} · ${item.card.number ?: ""}", style = MaterialTheme.typography.bodyMedium)
        }
        if (!item.card.rarity.isNullOrBlank()) {
            Text(text = "Rarity: ${item.card.rarity}", style = MaterialTheme.typography.bodyMedium)
        }

        OutlinedTextField(
            value = viewModel.quantity.toString(),
            onValueChange = viewModel::updateQuantity,
            label = { Text(stringResource(R.string.quantity)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = conditionExpanded,
            onExpandedChange = { conditionExpanded = !conditionExpanded }
        ) {
            OutlinedTextField(
                value = viewModel.condition,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.condition)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = conditionExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = conditionExpanded,
                onDismissRequest = { conditionExpanded = false }
            ) {
                conditions.forEach { condition ->
                    DropdownMenuItem(
                        text = { Text(condition) },
                        onClick = {
                            viewModel.updateCondition(condition)
                            conditionExpanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = viewModel.purchasePrice,
            onValueChange = viewModel::updatePurchasePrice,
            label = { Text(stringResource(R.string.purchase_price)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.notes,
            onValueChange = viewModel::updateNotes,
            label = { Text(stringResource(R.string.notes)) },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Grading prices",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        GradePriceEditorPortfolio(
            prices = viewModel.gradePrices,
            onValueChange = viewModel::updateGradePrices
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { viewModel.delete(onSaved) },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.delete))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { viewModel.save(onSaved) },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

@Composable
private fun GradePriceEditorPortfolio(
    prices: GradePrices,
    onValueChange: (GradePrices) -> Unit
) {
    val fields = listOf(
        GradePriceField(stringResource(R.string.price_ungraded), prices.ungraded) { prices.copy(ungraded = it) },
        GradePriceField(stringResource(R.string.price_psa9), prices.psa9) { prices.copy(psa9 = it) },
        GradePriceField(stringResource(R.string.price_psa10), prices.psa10) { prices.copy(psa10 = it) },
        GradePriceField(stringResource(R.string.price_cgc95), prices.cgc95) { prices.copy(cgc95 = it) },
        GradePriceField(stringResource(R.string.price_cgc10), prices.cgc10) { prices.copy(cgc10 = it) },
        GradePriceField(stringResource(R.string.price_bgs95), prices.bgs95) { prices.copy(bgs95 = it) }
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        fields.chunked(2).forEach { rowFields ->
            Row(modifier = Modifier.fillMaxWidth()) {
                rowFields.forEachIndexed { index, field ->
                    OutlinedTextField(
                        value = if (field.value == 0f) "" else field.value.toString(),
                        onValueChange = { text ->
                            val newValue = text.toFloatOrNull() ?: 0f
                            onValueChange(field.update(newValue))
                        },
                        label = { Text(field.label, maxLines = 1) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f)
                    )
                    if (index == 0) Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}
