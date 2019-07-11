package com.carrefour

import Aggregation.{dailyAggregations, weeklyAggregations}

object App {

  def main(args: Array[String]): Unit = {
    val today = "20170512"
    dailyAggregations(today)
    weeklyAggregations(today)
  }

}