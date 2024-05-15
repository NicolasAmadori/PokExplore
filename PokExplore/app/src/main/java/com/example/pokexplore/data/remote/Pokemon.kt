package com.example.pokexplore.data.remote

data class Pokemon(
    private val info: PokemonInfo,
    private val species: PokemonSpecies,
    private val evolutionChain: EvolutionChain
) {
    val id = info.id
    val name = info.name
    val types = info.types.map { it.type.name }
    val abilities = info.abilities.map { it.ability.name }
    val weight = info.weight
    val height = info.height
    val stats = info.stats.associate { it.stat.name to it.value }

    val description = species.flavorTextEntries.first { it.language.name == "en" }.flavorText
    val captureRate = species.captureRate
    val generation = species.generation.name

    val countryCode = when (generation) {
                            "generation-i" -> "jp" //Japan
                            "generation-ii" -> "it" //Italy
                            else -> null //World
                        }

//    val evolutions = mutableMapOf<Int, String>()
//
//    init {
//        var chain = evolutionChain.chain
//        while (chain.evolvesTo.isNotEmpty()) {
//            evolutions.add(chain.species.name)
//            chain = chain.evolvesTo.first()
//        }
//        evolutions.add(chain.species.name)
//    }

//    fun getDbEntity(): Pokemon {
//        return Pokemon(
//            id,
//            name,
//            types,
//            abilities,
//            weight,
//            height,
//            stats,
//            description,
//            captureRate,
//            evolutions,
//            generation,
//            countryCode
//        )
//    }
}