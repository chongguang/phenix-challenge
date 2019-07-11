package com.carrefour

import org.scalatest.FunSuite
import org.scalatest.Matchers

import com.carrefour.Utils.last7Days

class UtilesSpec extends FunSuite with Matchers {

  val EmptyTransactionList: List[Transaction] = List[Transaction]()

  val TransactionList1: List[Transaction] = List[Transaction](
    Transaction(1L, "20170101", "aaa", 1L, 5L, 1.0),
    Transaction(2L, "20170101", "bbb", 20L, 1L, 10.0)
  )

  test("last7Days") {
    last7Days("20170514") shouldEqual List[String]("20170508", "20170509", "20170510", "20170511", "20170512", "20170513", "20170514")
    last7Days("20170101") shouldEqual List[String]("20161226", "20161227", "20161228", "20161229", "20161230", "20161231", "20170101")
  }

}