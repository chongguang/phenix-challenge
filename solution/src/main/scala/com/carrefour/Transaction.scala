package com.carrefour

import java.util.logging.Logger

import scala.io.Source
import Product.getProductsPriceMap
import Utils.writeToFile

case class Transaction(txId: Long, date: String, shopId: String, productId: Long, qte : Long, price: Double)

object Transaction {

  def getTransactions(dates: List[String]): List[Transaction] = dates.flatMap(d => getProductSalesByDate(d, getProductsPriceMap(d)))

  def getProductSalesByDate(date: String, priceMap: Map[Long, Double]): List[Transaction] =
    getTransactionsByDate(date) match {
      case Some(ts) => ts.flatMap(t => addPriceToTransaction(priceMap)(t))
      case _ => List()
    }


  def addPriceToTransaction(priceMap: Map[Long, Double]): Transaction => Option[Transaction] =
    t => {
      try {
        Some(Transaction(t.txId, t.date, t.shopId, t.productId, t.qte, priceMap(t.productId)))
      } catch {
        case e: Exception =>
          Logger.getAnonymousLogger.warning(s"Caught the following exception while adding price to transaction $t: $e.")
          None
      }
    }

  def write(shopId: String, date: String, productSales: List[ProductSales]): Unit = {
    val top100Sales = productSales.sortBy(-_.qte).take(100)
    val top100Revenue = productSales.sortBy(-_.revenue).take(100)
    writeToFile(s"output/top_100_ventes_${shopId}_$date.data", top100Sales.map(s => (s.productId, s.qte)))
    writeToFile(s"output/top_100_ca_${shopId}_$date.data", top100Revenue.map(s => (s.productId, s.revenue)))
  }

  def productTotalSales(priceMap: Map[Long, Double], sales: List[(Long, Long)]): List[ProductSales] =
    sales.map( s => calculateTotalPriceToSale(priceMap, s))

  def calculateTotalPriceToSale(priceMap: Map[Long, Double], sale:(Long, Long)): ProductSales =
    if (priceMap.contains(sale._1)) ProductSales(sale._1, sale._2, sale._2 * priceMap(sale._1))
    else ProductSales(sale._1, sale._2, 0.0)

  def aggTransactionsToProductSales(transactions: List[Transaction]): List[(Long, Long)] =
    transactions.foldLeft(Map[Long, Long]()) {
      (acc, t) => {
        val id = t.productId
        val q = if (acc.contains(id)) acc(id) + t.qte else t.qte
        acc + (id -> q)
      }
    }.toList

  def getTransactionsByDate(date: String): Option[List[Transaction]] =
    try {
      Some(Source.fromResource(s"transactions_$date.data").getLines.flatMap(parseTransaction(date)).toList)
    } catch {
      case e: Exception =>
        Logger.getAnonymousLogger.warning(s"Caught the following exception while parsing transaction file of date $date: $e.")
        None
    }


  def parseTransaction(date: String)(s: String): Option[Transaction] = {
    val sList = s.split('|').map(_.trim)
    try {
      Some(Transaction(sList(0).toLong, date, sList(2), sList(3).toLong, sList(4).toLong, -1L))
    } catch {
      case e: Exception =>
        Logger.getAnonymousLogger.warning(s"Caught the following exception while parsing the transaction line $s: $e.")
        None
    }
  }
}