package csv

import org.apache.spark.sql.SparkSession

object ReadApp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Simple App")
      .master("local[*]")
      .getOrCreate()

    val data = Seq(("Alice", 28), ("Bob", 35))
    val df = spark.createDataFrame(data).toDF("Name", "Age")

    df.show()

    spark.stop()
  }
}
