package com.carrefour

/**
  * A ProductSales represents an aggregated information of a product has been sold for which quantity and which total revenue
  * @param productId
  * @param qte
  * @param revenue
  */
case class ProductSales(productId: Long, qte: Long, revenue: Double)