package com.carrefour

import java.util.logging.Logger

import scala.io.Source

/**
  * Data model for product information in the reference files.
  * @param produit
  * @param price
  * @param date
  * @param shopId
  */
case class Product(produit: Long, price: Double, date: String, shopId: String)

object Product {

  def getProductsPriceMap(date: String): Map[Long, Double] = productsToMap(getProductsByDate(date))

  def productsToMap(products: List[Product]): Map[Long, Double] =
    products map (p => p.produit -> p.price) toMap

  /**
    * All products of all shops of a given date.
    * @param date
    * @return
    */
  def getProductsByDate(date: String): List[Product] = Shop.ShopIds.flatMap(m => parseProductsFromFile(m, date)).flatten

  /**
    * Parse all products of a file.
    * @param magasin
    * @param date
    * @return
    */
  def parseProductsFromFile(magasin: String, date: String): Option[List[Product]] = {
    try {
      Some(Source.fromResource(s"reference_prod-${magasin}_$date.data").getLines.flatMap(parseProduct(date, magasin)).toList)
    } catch {
      case e: Exception =>
        Logger.getAnonymousLogger.warning(s"Caught the following exception while parsing the product file of magasin $magasin of date $date: $e.")
        None
    }
  }

  /**
    * Parsing a string of product into a case class. It yields a warning and returns None for badly formatted strings.
    * @param date
    * @param magasin
    * @param s
    * @return
    */
  def parseProduct(date: String, magasin: String)(s: String): Option[Product] = {
    val sList = s.split('|').map(_.trim)
    try {
      Some(Product(sList(0).toLong, sList(1).toDouble, date, magasin))
    } catch {
      case e: Exception =>
        Logger.getAnonymousLogger.warning(s"Caught the following exception while parsing the product line $s: $e.")
        None
    }
  }
}