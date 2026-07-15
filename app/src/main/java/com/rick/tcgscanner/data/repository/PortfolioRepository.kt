package com.rick.tcgscanner.data.repository

import com.rick.tcgscanner.data.local.PortfolioDao
import com.rick.tcgscanner.data.local.toDomain
import com.rick.tcgscanner.data.local.toEntity
import com.rick.tcgscanner.data.model.PortfolioItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PortfolioRepository(private val dao: PortfolioDao) {

    val portfolio: Flow<List<PortfolioItem>> = dao.getAll().map { list ->
        list.map { it.toDomain() }
    }

    suspend fun getItem(id: Int): PortfolioItem? = dao.getById(id)?.toDomain()

    suspend fun save(item: PortfolioItem): Long {
        return if (item.id == 0) {
            dao.insert(item.toEntity())
        } else {
            dao.update(item.toEntity())
            item.id.toLong()
        }
    }

    suspend fun delete(item: PortfolioItem) = dao.delete(item.toEntity())

    suspend fun totalValue(): Float = portfolio.map { list ->
        list.sumOf { (it.gradePrices.ungraded * it.quantity).toDouble() }.toFloat()
    }.first()
}
