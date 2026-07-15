package com.rick.tcgscanner.ui.navigation

sealed class Screen(val route: String) {
    data object Scan : Screen("scan")
    data object Portfolio : Screen("portfolio")
    data object Dashboard : Screen("dashboard")

    data object CardDetail : Screen("card_detail/{cardId}") {
        fun createRoute(cardId: String) = "card_detail/$cardId"
    }

    data object PortfolioDetail : Screen("portfolio_detail/{portfolioId}") {
        fun createRoute(portfolioId: Int) = "portfolio_detail/$portfolioId"
    }
}
