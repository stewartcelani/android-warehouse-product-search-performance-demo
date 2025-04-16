package com.example.warehouseproductsearchperformancedemo.util

import android.util.Log
import kotlin.system.measureTimeMillis

/**
 * Utility class to help with performance measurements
 */
object PerformanceUtils {
    private const val TAG = "PerformanceMetrics"
    
    /**
     * Measures and logs the execution time of a suspending function
     * 
     * @param operationName Name of the operation being measured
     * @param operation The suspending function to measure
     * @return The result of the operation
     */
    suspend fun <T> measureAndLogTime(operationName: String, operation: suspend () -> T): T {
        var result: T
        val time = measureTimeMillis {
            result = operation()
        }
        
        Log.d(TAG, "$operationName completed in $time ms")
        return result
    }
    
    /**
     * Logs the memory usage of the application
     */
    fun logMemoryUsage() {
        val runtime = Runtime.getRuntime()
        val usedMemoryMB = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)
        val totalMemoryMB = runtime.totalMemory() / (1024 * 1024)
        val freeMemoryMB = runtime.freeMemory() / (1024 * 1024)
        
        Log.d(TAG, "Memory Usage: $usedMemoryMB MB (used) / $totalMemoryMB MB (total) / $freeMemoryMB MB (free)")
    }
} 