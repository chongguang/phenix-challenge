package com.carrefour

import org.scalatest.{FunSuite, Matchers}

import com.carrefour.Transaction.{parseTransaction, addPriceToTransaction}

class TransactionSpec extends FunSuite with Matchers {

  val TransactionString1: String = "1|20170510T223544+0100|2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71|531|5"
  val TransactionString2: String = "toto"
  val TransactionString3: String = "1|20170510T223544+0100|2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71|531|toto"

  val PriceMap1: Map[Long, Double] = Map(1L -> 1.0)
  val PriceMap2: Map[Long, Double] = Map(531L -> 1.0)

  test("parseTransaction") {
    parseTransaction("20170514")(TransactionString1) shouldEqual Some(Transaction(1L, "20170514", "2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71", 531L, 5L, -1L))
    parseTransaction("20170514")(TransactionString2) shouldEqual None
    parseTransaction("20170514")(TransactionString3) shouldEqual None
  }
}