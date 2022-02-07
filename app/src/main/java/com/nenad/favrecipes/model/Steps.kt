package com.nenad.favrecipes.model

data class Steps (
    val number: Int,
    val step: String = "",
    val ingredients: Ingredients,
    val equipment: Equipment
        )