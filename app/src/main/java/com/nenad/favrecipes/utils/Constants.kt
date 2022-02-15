package com.nenad.favrecipes.utils

object Constants {
    const val API_KEY = "37955a4240cb47c1a902413bfddc527d"
    const val BASE_URL = "https://api.spoonacular.com"
    const val API_END_POINT = "/recipes/complexSearch"
    const val id = 1
    const val stepBreakdown = false

    // Bundle
    const val RECIPE_BUNDLE_KEY = "recipeBundle"

    // API query keys and default values
    const val QUERY_SEARCH = "query"
    const val QUERY_API_KEY = "apiKey"
    const val QUERY_NUMBER = "number"
    const val QUERY_TYPE = "type"
    const val QUERY_DIET = "diet"
    const val QUERY_RECIPE_INFORMATION = "addRecipeInformation"
    const val QUERY_FILL_INGREDIENTS = "fillIngredients"


    // Room Database
    const val DATABASE_NAME = "recipes_db"
    const val RECIPES_TABLE = "recipes"
    const val FAVORITE_RECIPES_TABLE = "favorite_recipes"
    const val FOOD_JOKE_TABLE = "food_joke"

    // Bottom Sheet and Preferences
    const val DEFAULT_RECIPES_NUMBER = "50"
    const val DEFAULT_MEAL_TYPE = "main course"
    const val DEFAULT_DIET_TYPE = "gluten free"

    const val PREFS_NAME = "food_recipes_prefs"
    const val PREFS_MEAL_TYPE = "mealType"
    const val PREFS_MEAL_TYPE_ID = "mealTypeId"
    const val PREFS_DIET_TYPE = "dietType"
    const val PREFS_DIET_TYPE_ID = "dietTypeId"
    const val PREFS_BACK_ONLINE = "backOnline"


}