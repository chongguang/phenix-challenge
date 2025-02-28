package com.carrefour

import java.io.{BufferedWriter, FileWriter}

import com.carrefour.Transaction.getTransactionsByDate

/**
  * This main function is used to generate data file of big volume for testing purpose.
  */
object DataGen {

  def main(args: Array[String]): Unit = {

    val transactions: List[Transaction] = getTransactionsByDate("20170514").get
    val transactions50Times: List[Transaction] = (1 until 40).foldLeft(transactions) {
      (acc, i) => acc ::: transactions
    }

    val writer = new BufferedWriter(new FileWriter("transactions_20170512.data"))
    for(t <- transactions50Times) {
      writer.write(t.txId + "|" + t.date + "|" + t.shopId + "|" + t.productId + "|" + t.qte + "\n")
    }
    writer.close()

  }
}