package com.carrefour

import java.io.{BufferedWriter, FileWriter}
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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
        if (acc.contains(t.produit)) acc + (t.produit -> (acc(t.produit) + t.qte))
        else acc + (t.produit -> t.qte)
    }.toList.sortBy(-_._2).take(100)

  def top100Revenu(transactions: List[Transaction]): List[(Long, Double)] =
    transactions.foldLeft(Map[Long, Double]()) {
      (acc: Map[Long, Double], t: Transaction) =>
        if (acc.contains(t.produit)) acc + (t.produit -> (acc(t.produit) + t.qte * t.price))
        else acc + (t.produit -> t.qte * t.price)
    }.toList.sortBy(-_._2).take(100)

  def writeToFile(filename: String, l: List[(Long, Any)]): Unit = {
    val writer = new BufferedWriter(new FileWriter(filename))
    for(tuple <- l) {
      writer.write(tuple._1 + "|" + tuple._2 + "\n")
    }
    writer.close()
  }

}