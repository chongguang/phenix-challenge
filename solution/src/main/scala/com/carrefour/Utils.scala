package com.carrefour

import java.io.{BufferedWriter, FileWriter}
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.carrefour.Transaction.getTransactions

object Utils {

  def last7Days(dateString: String): List[String] = {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    val dayJ = LocalDate.parse(dateString, formatter)
    val dates: List[String] = List[String](dateString)
    (1 to 6).foldLeft(dates) {
      (acc, i) => formatter.format(dayJ.minusDays(i)) :: acc
    }
  }

  def top100Sales(transactions: List[Transaction]): List[(Long, Long)] =
    transactions.foldLeft(Map[Long, Long]()) {
      (acc: Map[Long, Long], t: Transaction) =>
        if (acc.contains(t.productId)) acc + (t.productId -> (acc(t.productId) + t.qte))
        else acc + (t.productId -> t.qte)
    }.toList.sortBy(-_._2).take(100)

  def top100Revenu(transactions: List[Transaction]): List[(Long, Double)] =
    transactions.foldLeft(Map[Long, Double]()) {
      (acc: Map[Long, Double], t: Transaction) =>
        if (acc.contains(t.productId)) acc + (t.productId -> (acc(t.productId) + t.qte * t.price))
        else acc + (t.productId -> t.qte * t.price)
    }.toList.sortBy(-_._2).take(100)

  def writeToFile(filename: String, l: List[(Long, Any)]): Unit = {
    val writer = new BufferedWriter(new FileWriter(filename))
    for(tuple <- l) {
      writer.write(tuple._1 + "|" + tuple._2 + "\n")
    }
    writer.close()
  }

  def generateResultFiles(date: String, for7Days: Boolean = false): Unit = {
    val dates: List[String] = if (for7Days) last7Days(date) else List(date)
    val transactions: List[Transaction] = getTransactions(dates)

    val dateLabel: String = if (for7Days) date + "-J7" else date
    writeToFile(s"output/top_100_ventes_GLOBAL_$dateLabel.data", top100Sales(transactions))
    writeToFile(s"output/top_100_ca_GLOBAL_$dateLabel.data", top100Revenu(transactions))

    Shop.ShopIds.foreach{ s =>
      val trans = transactions.filter(t => t.shopId == s)
      writeToFile(s"output/top_100_ventes_${s}_$dateLabel.data", top100Sales(trans))
      writeToFile(s"output/top_100_ca_${s}_$dateLabel.data", top100Revenu(trans))
    }
  }

}