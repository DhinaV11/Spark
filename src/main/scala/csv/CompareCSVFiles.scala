package csv

import org.apache.spark.sql.SparkSession

object CompareCSVFiles {

  def main(args: Array[String]): Unit = {
    // Step 1: Start SparkSession
    val spark = SparkSession.builder()
      .appName("Compare CSV Files")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    // Step 2: Read CSV files
    val df1 = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("src/main/resources/file1.csv")

    val df2 = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("src/main/resources/file2.csv")

    // Step 3: Get common rows (intersection)
    val commonDF = df1.intersect(df2)

    // Step 4: Save the common data to a new CSV file
    commonDF.write
      .option("header", "true")
      .mode("overwrite")
      .csv("output/common_records")

    spark.stop()
  }
}

