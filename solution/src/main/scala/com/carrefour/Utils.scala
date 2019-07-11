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

  def writeToFile(filename: String, l: List[(Long, Any)]): Unit = {
    val writer = new BufferedWriter(new FileWriter(filename))
    for(tuple <- l) {
      writer.write(tuple._1 + "|" + tuple._2 + "\n")
    }
    writer.close()
  }

}