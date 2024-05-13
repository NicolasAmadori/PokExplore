package com.example.pokexplore.data.remote

import com.example.pokexplore.data.database.Pokemon

data class Pokemon(
    private val info: PokemonInfo,
    private val species: PokemonSpecies,
    private val evolutionChain: EvolutionChain
) {
    val id = info.id
    val name = info.name
    val sprite = info.sprite.front
    val types = info.types.map { it.type.name }
    val abilities = info.abilities.map { it.ability.name }
    val weight = info.weight
    val height = info.height
    val stats = info.stats.associate { it.stat.name to it.value }
    val cry = info.cry.cry

    val color = species.color.name
    val description = species.flavorTextEntries.first { it.language.name == "en" }.flavorText
    val captureRate = species.captureRate
    val generation = species.generation.name

    val countryCode = when (generation) {
                            "generation-i" -> 392 //Japan
                            "generation-ii" -> 380 //Italy
                            else -> null //World
                        }

    val evolutions = mutableListOf<String>()

    init {
        var chain = evolutionChain.chain
        while (chain.evolvesTo.isNotEmpty()) {
            evolutions.add(chain.species.name)
            chain = chain.evolvesTo.first()
        }
        evolutions.add(chain.species.name)
    }

    fun getDbEntity(): Pokemon {
        return Pokemon(
            id,
            name,
            sprite,
            types,
            abilities,
            weight,
            height,
            stats,
            cry,
            color,
            description,
            captureRate,
            evolutions,
            generation,
            countryCode
        )
    }
}