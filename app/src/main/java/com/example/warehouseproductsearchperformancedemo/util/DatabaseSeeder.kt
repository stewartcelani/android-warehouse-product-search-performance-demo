package com.example.warehouseproductsearchperformancedemo.util

import com.example.warehouseproductsearchperformancedemo.data.AppDatabase
import com.example.warehouseproductsearchperformancedemo.data.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.random.Random

class DatabaseSeeder @Inject constructor(private val database: AppDatabase) {

    // Supplier names for generating random supplier data
    private val suppliers = listOf(
        "Global Supply Co.", 
        "TechParts Inc.", 
        "FarmFresh Distributors",
        "ElectroParts Ltd.",
        "Acme Industries",
        "MegaStore Supplies",
        "Quality Hardware Corp",
        "Eastern Manufacturing",
        "Western Distributors",
        "Northern Goods Ltd.",
        "Southern Products Inc.",
        "Continental Exports",
        "Precision Parts Co.",
        "Bulk Warehouse Supplies",
        "Reliable Goods Inc.",
        "Prime Distribution Center",
        "EcoFriendly Materials",
        "Industrial Solutions Group",
        "Commercial Supply Chain",
        "Wholesale Distributors Inc."
    )
    
    // Product categories for more realistic titles
    private val categories = mapOf(
        "ELEC" to listOf(
            "LED Light", "Power Adapter", "HDMI Cable", "Circuit Board", "Capacitor",
            "Resistor", "Battery Pack", "Solar Panel", "Voltage Regulator", "Transistor"
        ),
        "TOOL" to listOf(
            "Hammer", "Wrench Set", "Power Drill", "Measuring Tape", "Screwdriver",
            "Circular Saw", "Pliers", "Level Tool", "Socket Set", "Utility Knife"
        ),
        "FOOD" to listOf(
            "Organic Flour", "Canned Beans", "Rice", "Pasta", "Cooking Oil",
            "Spice Mix", "Dried Fruit", "Baking Powder", "Sugar", "Salt"
        ),
        "AUTO" to listOf(
            "Oil Filter", "Brake Pad", "Spark Plug", "Wiper Blade", "Air Filter",
            "Timing Belt", "Radiator Cap", "Transmission Fluid", "Battery Terminal", "Headlight Bulb"
        ),
        "CHEM" to listOf(
            "Industrial Cleaner", "Adhesive", "Paint", "Solvent", "Lubricant",
            "Epoxy Resin", "Rust Converter", "Degreaser", "PVC Cement", "Silicone Sealant"
        ),
        "FURN" to listOf(
            "Office Chair", "Desk", "Bookshelf", "Cabinet", "Table",
            "Filing Cabinet", "Ergonomic Keyboard Tray", "Monitor Stand", "Lamp", "Drawer Unit"
        ),
        "TEXT" to listOf(
            "Cotton Fabric", "Polyester Blend", "Upholstery Material", "Canvas", "Denim",
            "Microfiber Cloth", "Nylon Webbing", "Elastic Band", "Velcro Strip", "Thread Spool"
        ),
        "PACK" to listOf(
            "Cardboard Box", "Bubble Wrap", "Packing Tape", "Plastic Container", "Shipping Label",
            "Foam Insert", "Padded Envelope", "Shrink Wrap", "Pallet Wrap", "Void Fill"
        )
    )
    
    private val adjectives = listOf(
        "Premium", "Standard", "Industrial", "Commercial", "Professional", 
        "Basic", "Advanced", "Essential", "Heavy-Duty", "Lightweight",
        "Organic", "Synthetic", "Reinforced", "Insulated", "Waterproof"
    )
    
    private val specifications = listOf(
        "Large", "Small", "Medium", "Compact", "Portable",
        "High-Capacity", "Long-Lasting", "Quick-Release", "Adjustable", "Universal",
        "Foldable", "Stackable", "Expandable", "Modular", "Customizable"
    )
    
    private val measurements = listOf(
        "10mm", "50cm", "1L", "5kg", "2m", 
        "100g", "250ml", "12in", "30cm", "6oz",
        "15cm", "500g", "750ml", "25mm", "8ft"
    )
    
    // Generate product code based on category and ID
    private fun generateCode(category: String, id: Long): String {
        val sequence = id.toString().padStart(5, '0')
        return "$category-$sequence"
    }
    
    // Generate realistic product title
    private fun generateTitle(category: String, id: Long): String {
        val baseItems = categories[category] ?: categories.values.random()
        val baseItem = baseItems.random()
        
        // Randomly select title format
        return when ((id % 4).toInt()) {
            0 -> "${adjectives.random()} $baseItem ${measurements.random()}"
            1 -> "$baseItem, ${specifications.random()}, ${measurements.random()}"
            2 -> "${specifications.random()} ${adjectives.random()} $baseItem"
            else -> "$baseItem ${measurements.random()} ${adjectives.random()} Grade"
        }
    }
    
    // Generate valid-format barcode
    private fun generateBarcode(): String {
        val prefix = (100..999).random() // GS1 prefix (simplified)
        val middle = (1000000..9999999).random() // 7 digits
        
        // Simple check digit calculation (simplified version)
        val digits = "$prefix$middle"
        var sum = 0
        digits.forEachIndexed { index, c ->
            val digit = c.toString().toInt()
            // Alternate multiplier: 3 for even positions, 1 for odd
            val multiplier = if (index % 2 == 0) 3 else 1
            sum += digit * multiplier
        }
        val checkDigit = (10 - (sum % 10)) % 10
        
        // UPC-A (12 digits) with check digit
        return "$prefix$middle$checkDigit"
    }
    
    // Generate stock values with distribution
    private fun generateStock(): Double {
        return when (Random.nextInt(10)) {
            in 0..2 -> Random.nextDouble(0.0, 10.0) // 30% low stock
            in 3..7 -> Random.nextDouble(10.0, 1000.0) // 50% medium stock
            else -> Random.nextDouble(1000.0, 10000.0) // 20% high stock
        }.roundToTwoDecimals()
    }
    
    // Helper to round double to 2 decimal places
    private fun Double.roundToTwoDecimals(): Double {
        return (this * 100).roundToInt() / 100.0
    }
    
    // Main function to seed the database
    suspend fun seedDatabase(onProgress: (Int) -> Unit) = withContext(Dispatchers.IO) {
        val currentCount = database.productDao().getCount()
        if (currentCount > 0) {
            onProgress(100)
            return@withContext // Database already seeded
        }
        
        val batchSize = 1000 // Insert 1000 products at a time for better performance
        val totalProducts = 100000
        
        for (i in 0 until totalProducts step batchSize) {
            val products = (i until minOf(i + batchSize, totalProducts)).map { id ->
                val categoryKey = categories.keys.toList().random()
                
                Product(
                    id = id.toLong(),
                    code = generateCode(categoryKey, id.toLong()),
                    title = generateTitle(categoryKey, id.toLong()),
                    barcode = generateBarcode(),
                    supplier = suppliers.random(),
                    stock = generateStock(),
                    isActive = Random.nextInt(100) < 75 // 75% active
                )
            }
            
            database.productDao().insertAll(products)
            val progress = ((i + batchSize) * 100) / totalProducts
            onProgress(minOf(progress, 100))
        }
        
        onProgress(100)
    }
} 