package com.rick.tcgscanner.data.local

import com.rick.tcgscanner.data.model.Card
import com.rick.tcgscanner.data.model.GradePrices
import com.rick.tcgscanner.data.model.PortfolioItem

fun PortfolioEntity.toDomain(): PortfolioItem = PortfolioItem(
    id = this.id,
    card = Card(
        id = this.cardId,
        name = this.name,
        number = this.number,
        rarity = this.rarity,
        setName = this.setName,
        smallImage = this.smallImageUrl,
        largeImage = this.imageUrl
    ),
    quantity = this.quantity,
    condition = this.condition,
    purchasePrice = this.purchasePrice,
    notes = this.notes,
    gradePrices = GradePrices(
        ungraded = this.ungradedMarket,
        psa9 = this.psa9,
        psa10 = this.psa10,
        cgc95 = this.cgc95,
        cgc10 = this.cgc10,
        bgs95 = this.bgs95
    ),
    addedAt = this.addedAt
)

fun PortfolioItem.toEntity(): PortfolioEntity = PortfolioEntity(
    id = this.id,
    cardId = this.card.id,
    name = this.card.name,
    setName = this.card.setName ?: "",
    number = this.card.number ?: "",
    rarity = this.card.rarity ?: "",
    imageUrl = this.card.largeImage,
    smallImageUrl = this.card.smallImage,
    quantity = this.quantity,
    condition = this.condition,
    purchasePrice = this.purchasePrice,
    notes = this.notes,
    ungradedMarket = this.gradePrices.ungraded,
    psa9 = this.gradePrices.psa9,
    psa10 = this.gradePrices.psa10,
    cgc95 = this.gradePrices.cgc95,
    cgc10 = this.gradePrices.cgc10,
    bgs95 = this.gradePrices.bgs95,
    addedAt = this.addedAt
)
