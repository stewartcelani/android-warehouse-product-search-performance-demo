package com.example.warehouseproductsearchperformancedemo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warehouseproductsearchperformancedemo.data.Product
import com.example.warehouseproductsearchperformancedemo.data.ProductDao
import com.example.warehouseproductsearchperformancedemo.util.DatabaseSeeder
import com.example.warehouseproductsearchperformancedemo.util.PerformanceUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@HiltViewModel
class MainViewModel @Inject constructor(
    private val productDao: ProductDao,
    private val databaseSeeder: DatabaseSeeder
) : ViewModel() {

    private val TAG = "PerformanceMetrics"
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery
    
    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults
    
    private val _seedingProgress = MutableStateFlow(0)
    val seedingProgress: StateFlow<Int> = _seedingProgress
    
    private val _isDatabaseSeeded = MutableStateFlow(false)
    val isDatabaseSeeded: StateFlow<Boolean> = _isDatabaseSeeded
    
    // Performance metrics
    private val _lastSearchTime = MutableStateFlow(0L)
    val lastSearchTime: StateFlow<Long> = _lastSearchTime
    
    private var searchJob: Job? = null
    
    init {
        // Start seeding the database when ViewModel is created
        viewModelScope.launch {
            val seedTime = measureTimeMillis {
                seedDatabase()
            }
            Log.d(TAG, "Database seeding completed in $seedTime ms")
        }
        
        // Set up debounced search
        viewModelScope.launch {
            searchQuery
                .debounce(300) // 1 second debounce
                .collectLatest { query ->
                    if (query.isNotEmpty()) {
                        performSearch(query)
                    } else {
                        _searchResults.value = emptyList()
                    }
                }
        }
    }
    
    private suspend fun seedDatabase() {
        _isLoading.value = true
        databaseSeeder.seedDatabase { progress ->
            _seedingProgress.value = progress
            if (progress == 100) {
                _isDatabaseSeeded.value = true
                _isLoading.value = false
                PerformanceUtils.logMemoryUsage()
            }
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    private suspend fun performSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _isLoading.value = true
            try {
                val searchQuery = "%$query%"
                var results: List<Product>
                
                val searchTime = measureTimeMillis {
                    results = productDao.searchProducts(searchQuery)
                }
                
                _lastSearchTime.value = searchTime
                _searchResults.value = results
                
                Log.d(TAG, "Search for '$query' completed in $searchTime ms, found ${results.size} results")
                PerformanceUtils.logMemoryUsage()
                
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // For benchmarking specific search scenarios
    fun performBarcodeSearchTest(exactBarcode: String) {
        viewModelScope.launch {
            val searchTime = measureTimeMillis {
                val result = productDao.findProductByExactBarcode(exactBarcode)
                Log.d(TAG, "Exact barcode search result: $result")
            }
            Log.d(TAG, "Exact barcode search completed in $searchTime ms")
        }
    }
} 