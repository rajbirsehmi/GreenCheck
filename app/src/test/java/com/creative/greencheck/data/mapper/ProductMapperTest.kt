package com.creative.greencheck.data.mapper

import com.creative.greencheck.data.remote.dto.ProductResponse
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductMapperTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `parse and map Starbucks Protein Drink JSON payload correctly`() {
        val payload = """
        {
          "code": "5711953184871",
          "product": {
            "brands": "Starbucks",
            "categories_tags": [
              "en:beverages-and-beverages-preparations",
              "en:beverages",
              "en:dairies",
              "en:dairy-drinks",
              "en:coffee-drinks",
              "en:Drink protein",
              "en:Protein drink"
            ],
            "code": "5711953184871",
            "image_front_url": "https://images.openfoodfacts.org/images/products/571/195/318/4871/front_en.12.400.jpg",
            "image_url": "https://images.openfoodfacts.org/images/products/571/195/318/4871/front_en.12.400.jpg",
            "ingredients": [
              {
                "id": "en:fat milk",
                "is_in_taxonomy": 0,
                "percent": 1.2,
                "percent_estimate": 58.62,
                "quantity_estimate": 58.62,
                "text": "fat milk"
              },
              {
                "id": "en:Starbucks Arabica coffee coffee extract",
                "is_in_taxonomy": 0,
                "percent": 20.9,
                "percent_estimate": 18.04,
                "quantity_estimate": 18.04,
                "text": "Starbucks Arabica coffee coffee extract"
              },
              {
                "id": "en:milk-proteins",
                "is_in_taxonomy": 1,
                "percent": 3.8,
                "percent_estimate": 9.06,
                "processing": "en:powder",
                "quantity_estimate": 9.06,
                "text": "milk protein",
                "vegan": "no",
                "vegetarian": "yes"
              },
              {
                "id": "en:stabiliser",
                "ingredients": [
                  {
                    "id": "en:gellan gum carrageenan",
                    "is_in_taxonomy": 0,
                    "percent_estimate": 5.55,
                    "quantity_estimate": 5.55,
                    "text": "gellan gum carrageenan"
                  }
                ],
                "is_in_taxonomy": 1,
                "percent_estimate": 5.55,
                "quantity_estimate": 5.55,
                "text": "stabilisers"
              },
              {
                "id": "en:acidity-regulator",
                "is_in_taxonomy": 1,
                "percent_estimate": 3.8,
                "quantity_estimate": 3.8,
                "text": "acidity regulator"
              },
              {
                "id": "en:sodium-citrate",
                "is_in_taxonomy": 1,
                "percent_estimate": 2.79,
                "quantity_estimate": 2.79,
                "text": "sodium citrate",
                "vegan": "yes",
                "vegetarian": "yes"
              },
              {
                "id": "en:potassium carbonate Sweetener",
                "ingredients": [
                  {
                    "id": "en:acesulfame",
                    "is_in_taxonomy": 0,
                    "percent_estimate": 2.14,
                    "quantity_estimate": 2.14,
                    "text": "acesulfame"
                  }
                ],
                "is_in_taxonomy": 0,
                "percent_estimate": 2.14,
                "quantity_estimate": 2.14,
                "text": "potassium carbonate Sweetener"
              }
            ],
            "ingredients_analysis_tags": [
              "en:palm-oil-free",
              "en:non-vegan",
              "en:vegetarian-status-unknown"
            ],
            "ingredients_text": "1.2% fat milk (79%), Starbucks Arabica coffee coffee extract) (20.9%) milk protein powder (3.8%), stabilisers (gellan gum carrageenan), acidity regulator (sodium citrate, potassium carbonate Sweetener (acesulfame).",
            "labels_tags": [
              "en:no-added-sugar"
            ],
            "product_name": "Protein Drink"
          },
          "status": 1,
          "status_verbose": "product found"
        }
        """.trimIndent()

        val response = json.decodeFromString<ProductResponse>(payload)

        assertTrue(response.isFound)
        assertEquals("5711953184871", response.code)

        val productDetails = response.product
        assertNotNull(productDetails)
        assertEquals("Protein Drink", productDetails?.name)
        assertEquals("Starbucks", productDetails?.brands)
        assertEquals("https://images.openfoodfacts.org/images/products/571/195/318/4871/front_en.12.400.jpg", productDetails?.frontUrl)

        val product = productDetails!!.toDomain()

        assertEquals("5711953184871", product.barcode)
        assertEquals("Protein Drink", product.name)
        assertEquals("Starbucks", product.brands)
        assertEquals("https://images.openfoodfacts.org/images/products/571/195/318/4871/front_en.12.400.jpg", product.imageUrl)

        // Non-vegan check
        assertTrue(product.isNonVegan)
        assertFalse(product.isVegan)

        // Labels & Palm oil check
        assertTrue(product.isPalmOilFree)
        assertEquals("Palm Oil Free", product.palmOilStatusText)
        assertTrue(product.formattedLabels.contains("No Added Sugar"))

        // Categories check
        assertTrue(product.formattedCategories.contains("Beverages"))
        assertTrue(product.formattedCategories.contains("Coffee Drinks"))

        // Ingredients check
        val ingredients = product.ingredients
        assertNotNull(ingredients)
        assertEquals(7, ingredients?.size)

        val fatMilk = ingredients?.first()
        assertEquals("fat milk", fatMilk?.text)
        assertEquals(1.2, fatMilk?.percent)
        assertEquals("1.2%", fatMilk?.formattedPercentage)

        val milkProtein = ingredients?.get(2)
        assertEquals("milk protein", milkProtein?.text)
        assertEquals(3.8, milkProtein?.percent)
        assertEquals("no", milkProtein?.vegan)
        assertTrue(milkProtein?.isNonVegan == true)

        val stabiliser = ingredients?.get(3)
        assertEquals("stabilisers", stabiliser?.text)
        assertNotNull(stabiliser?.subIngredients)
        assertEquals(1, stabiliser?.subIngredients?.size)
        assertEquals("gellan gum carrageenan", stabiliser?.subIngredients?.first()?.text)
    }
}
