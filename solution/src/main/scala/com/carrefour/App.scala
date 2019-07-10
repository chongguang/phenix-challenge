package com.carrefour


import Utils.generateResultFiles

object App {

  def main(args: Array[String]): Unit = {
    val dayJ: String = "20170514"

    generateResultFiles(dayJ)
    generateResultFiles(dayJ, for7Days = true)
  }

}