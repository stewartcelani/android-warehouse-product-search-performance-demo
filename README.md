# Warehouse Product Search Performance Demo

This Android application serves as a proof-of-concept to test the performance of searching large SQLite tables via Room Database. The application simulates a warehouse inventory system with 100,000 products and measures search performance when filtering through code, title, and barcode fields.

## Features

- Modern UI using Jetpack Compose
- Room database with 100,000 realistic product entries
- Performance measurement for search operations
- Simple UI to search products and display results

## Technical Details

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Database**: Room Database (SQLite)
- **Architecture**: MVVM with ViewModel and StateFlow
- **Dependency Injection**: Dagger-Hilt
- **Concurrency**: Kotlin Coroutines

## Implementation Highlights

1. **Database Structure**
   - Products table with indices on searchable fields
   - Realistic data generation for testing purposes
   - Efficient batch insertion of 100,000 records

2. **Search Performance Optimizations**
   - Debounced search input (1 second delay)
   - SQL query optimization with indices
   - Background thread processing

3. **UI Responsiveness**
   - Progress indicator during database initialization
   - Loading state during search operations
   - Virtualized list via LazyColumn

## Getting Started

1. Clone the repository
2. Open in Android Studio
3. Run the application on a device or emulator

## Performance Metrics

On first launch, the application seeds the database with 100,000 products. Performance metrics are logged to the console, including:

- Database initialization time
- Search query execution time
- Memory usage during operations

## Future Enhancements

- Implement barcode scanner integration
- Add sorting and filtering options
- Implement Full-Text Search for better performance
- Create detail view for products 