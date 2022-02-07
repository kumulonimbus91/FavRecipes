package com.nenad.favrecipes.model

data class ResponseJson (
    val parsedInstructions: ParsedInstructions,
    val ingredients: Ingredients,
    val equipment: Equipment
        )