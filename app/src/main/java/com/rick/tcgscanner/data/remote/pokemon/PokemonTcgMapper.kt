package com.rick.tcgscanner.data.remote.pokemon

import com.rick.tcgscanner.data.model.Card
import com.rick.tcgscanner.data.model.Price
import com.rick.tcgscanner.data.model.TcgPlayerPrices

fun PokemonCardDto.toCard(): Card = Card(
    id = this.id,
    name = this.name,
    supertype = this.supertype,
    subtypes = this.subtypes ?: emptyList(),
    hp = this.hp,
    types = this.types ?: emptyList(),
    number = this.number,
    rarity = this.rarity,
    setName = this.set?.name,
    setSeries = this.set?.series,
    smallImage = this.images?.small ?: "",
    largeImage = this.images?.large ?: "",
    tcgplayerPrices = this.tcgplayer?.toModel()
)

fun PokemonTcgPlayerDto.toModel(): TcgPlayerPrices = TcgPlayerPrices(
    normal = this.normal?.toPrice(),
    holofoil = this.holofoil?.toPrice(),
    reverseHolofoil = this.reverseHolofoil?.toPrice()
)

fun PokemonPriceDto.toPrice(): Price = Price(
    low = this.low,
    mid = this.mid,
    high = this.high,
    market = this.market,
    directLow = this.directLow
)
