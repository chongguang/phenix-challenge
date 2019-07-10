package com.carrefour

import Transaction.getTransactions
import Utils.{last7Days, top100Revenu, top100Sales, writeToFile}

object App {

  def main(args: Array[String]): Unit = {
    val dayJ: String = "20170514"
    val dateJ: List[String] = List[String](dayJ)
    val last7Dates: List[String] = last7Days(dayJ)
    val transactionsDayJ: List[Transaction] = getTransactions(dateJ)
    val transactions7Days: List[Transaction] = getTransactions(last7Dates)
    //transactions.take(10).foreach(println)
    val top100SalersDayJ = top100Sales(transactionsDayJ)
    writeToFile(s"output/top_100_ventes_GLOBAL_$dayJ.data", top100SalersDayJ)
    val top100RevenuProductsDayJ = top100Revenu(transactionsDayJ)
    writeToFile(s"output/top_100_ca_GLOBAL_$dayJ.data", top100RevenuProductsDayJ)

    val top100Salers7Days = top100Sales(transactions7Days)
    writeToFile(s"output/top_100_ventes_GLOBAL_$dayJ-J7.data", top100Salers7Days)
    val top100RevenuProducts7Days = top100Revenu(transactions7Days)
    writeToFile(s"output/top_100_ca_GLOBAL_$dayJ-J7.data", top100RevenuProducts7Days)
  }






}