package com.carrefour

import org.scalatest.{FunSuite, Matchers}

import com.carrefour.Product.{parseProduct, productsToMap}

class ProductSpec extends FunSuite with Matchers {

  val ProductString1: String = "1|4.7"
  val ProductString2: String = "toto"

  val ProductList1: List[Product] = List[Product](Product(1L, 4.7, "20170514", "shop1"), Product(2L, 3.2, "20170514", "shop1"))
  val ProductList2: List[Product] = List[Product]()

  test("parseProduct") {
    parseProduct("20170514", "shop1")(ProductString1) shouldEqual Some(Product(1L, 4.7, "20170514", "shop1"))
    parseProduct("20170514", "shop1")(ProductString2) shouldEqual None
  }

  test("productsToMap") {
    productsToMap(ProductList1) shouldEqual Map[(Long, String, String), Double]((1L, "20170514", "shop1") -> 4.7, (2L, "20170514", "shop1") -> 3.2)
    productsToMap(ProductList2) shouldEqual Map[(Long, String, String), Double]()
  }
}