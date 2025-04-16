package com.example.warehouseproductsearchperformancedemo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.warehouseproductsearchperformancedemo.ui.components.DatabaseSeedingProgress
import com.example.warehouseproductsearchperformancedemo.ui.components.ProductList
import com.example.warehouseproductsearchperformancedemo.ui.components.SearchBar
import com.example.warehouseproductsearchperformancedemo.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarehouseSearchScreen(
    viewModel: MainViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val seedingProgress by viewModel.seedingProgress.collectAsState()
    val isDatabaseSeeded by viewModel.isDatabaseSeeded.collectAsState()
    val lastSearchTime by viewModel.lastSearchTime.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Warehouse Product Search") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!isDatabaseSeeded) {
                DatabaseSeedingProgress(progress = seedingProgress)
            } else {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { viewModel.updateSearchQuery(it) },
                        isLoading = isLoading
                    )
                    
                    if (lastSearchTime > 0 && searchResults.isNotEmpty()) {
                        Text(
                            text = "Found ${searchResults.size} results in ${lastSearchTime}ms",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    ProductList(
                        products = searchResults,
                        isLoading = isLoading,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
} 