package com.carrefour

import Transaction._
import Product._
import Utils._
import scala.io.Source
import java.util.logging.Logger

object App {

  def main(args: Array[String]): Unit = {
    val today = "20170512"
    dailyAggregations(today)
    weeklyAggregations(today)
  }

  def dailyAggregations(date: String): Unit = {
    val transactions: List[Transaction] = getTransactionsByDate(date).get
    Shop.ShopIds.foreach(id => aggAndWrite(date, transactions.filter(t => t.shopId == id), id))
    aggAndWrite(date, transactions, "GLOBAL")
  }

  def weeklyAggregations(lastDate: String): Unit = for {
    shop <- "GLOBAL" :: Shop.ShopIds
    agg <- List("ca", "ventes")
  } yield aggLast7Days(lastDate, shop, agg)

  def aggAndWrite(date: String, transactions: List[Transaction], shopId: String): Unit = {
    val agg = aggTransactionsToProductSales(transactions)
    val priceMap = getProductsPriceMap(date)
    val sales = productTotalSales(priceMap, agg)
    write(shopId, date, sales)
  }

  def aggLast7Days(lastDay: String, shopId: String, aggregation: String): Unit = {
    val dates: List[String] = last7Days(lastDay)
    val dailyInfo: List[Map[Long, AggValue[Any]]] = dates.map( d => aggregationToMap(shopId, d, aggregation))
    val top1007days: Map[Long, Any] = dailyInfo.foldLeft(Map[Long, Any]()) {
      (aggregatedMap, map) => {
        val l = map.toList
        l.foldLeft(aggregatedMap){
          (acc, m) => if (acc.contains(m._1)) acc + (m._1 -> (acc(m._1) + m._2.get)) else acc + (m._1 -> m._2)
        }
      }
    }
    writeToFile(s"output/top_100_${aggregation}_${shopId}_$lastDay-J7.data", top1007days.toList.take(100))
  }

  def aggregationToMap(shopId: String, date: String, aggregation: String): Map[Long, AggValue[Any]] = {
    val agg = try{
      Some(Source.fromFile(s"output/top_100_${aggregation}_${shopId}_$date.data").getLines.toList)
    } catch {
      case e: Exception =>
        Logger.getAnonymousLogger.warning(s"Caught the following exception while loading aggregation file: $e.")
        None
    }

    agg match {
      case Some(l) => l.map(s => {
        val sp = s.split('|')
        sp.head.toLong -> (if (aggregation == "ca") AggValue[Double](sp(1).toDouble) else AggValue[Long](sp(1).toLong))
      }).toMap
      case _ => if (aggregation == "ca") Map[Long, AggValue[Double]]() else Map[Long, AggValue[Long]]()
    }
  }

  case class AggValue[+T: Numeric](element: T) {
    def get[T]: T = {
      element.asInstanceOf[T]
    }
  }

}