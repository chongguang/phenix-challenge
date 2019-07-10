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

    writeToFile(s"output/top_100_ventes_GLOBAL_$dayJ.data", top100Sales(transactionsDayJ))
    writeToFile(s"output/top_100_ca_GLOBAL_$dayJ.data", top100Revenu(transactionsDayJ))

    writeToFile(s"output/top_100_ventes_GLOBAL_$dayJ-J7.data", top100Sales(transactions7Days))
    writeToFile(s"output/top_100_ca_GLOBAL_$dayJ-J7.data", top100Revenu(transactions7Days))

    Shop.ShopIds.foreach{ s =>
      val trans = transactions7Days.filter(t => t.shopId == s)
      writeToFile(s"output/top_100_ventes_${s}_$dayJ-J7.data", top100Sales(trans))
      writeToFile(s"output/top_100_ca_${s}_$dayJ-J7.data", top100Revenu(trans))
    }

    Shop.ShopIds.foreach{ s =>
      val trans = transactionsDayJ.filter(t => t.shopId == s)
      writeToFile(s"output/top_100_ventes_${s}_$dayJ.data", top100Sales(trans))
      writeToFile(s"output/top_100_ca_${s}_$dayJ.data", top100Revenu(trans))
    }
  }






}