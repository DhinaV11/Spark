package csv

import org.apache.spark.sql.SparkSession

object readcsv {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Read CSV")
      .master("local[*]")
      .getOrCreate()

    // ✅ Replace with your actual path if different
    val filePath = "src/main/resources/AAPL.csv"

    val df = spark.read
      .option("header", "true")       // use first row as header
      .option("inferSchema", "true")  // auto-detect data types
      .csv(filePath)

    df.show()

    spark.stop()
  }
}
