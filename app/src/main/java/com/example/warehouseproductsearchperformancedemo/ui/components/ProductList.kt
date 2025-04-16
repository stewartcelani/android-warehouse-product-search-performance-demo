package com.example.warehouseproductsearchperformancedemo.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.warehouseproductsearchperformancedemo.data.Product

@Composable
fun ProductList(
    products: List<Product>,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    if (products.isEmpty() && !isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No products found",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(
                items = products,
                key = { it.id }
            ) { product ->
                ProductItem(
                    product = product,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
} 