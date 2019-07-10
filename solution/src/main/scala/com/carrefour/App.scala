package com.carrefour

import Transaction.getTransactions
import Utils.{last7Days, top100Revenu, top100Sales}

object App {

  def main(args: Array[String]): Unit = {
    val dayJ: String = "20170514"
    val lastDate: List[String] = List[String](dayJ)
    val last7Dates: List[String] = last7Days(dayJ)
    last7Dates.foreach(println)
    val transactions: List[Transaction] = getTransactions(lastDate)
    println(transactions.size)
    //transactions.take(10).foreach(println)
    val top100Salers = top100Sales(transactions)
    top100Salers.take(10).foreach(println)

    println("-------------")
    val top100RevenuProducts = top100Revenu(transactions)
    top100RevenuProducts.take(10).foreach(println)
  }






}