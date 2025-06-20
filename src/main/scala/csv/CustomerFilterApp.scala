package csv

import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.functions._

object CustomerFilterApp {

  def main(args: Array[String]): Unit = {

    // Step 1: Create SparkSession (Driver Program)
    val spark = SparkSession.builder()
      .appName("Customer Filter App")
      .master("local[*]")  // Runs locally using all cores
      .getOrCreate()

    import spark.implicits._

    // Step 2: Read CSV file into DataFrame
    val customerDF: DataFrame = spark.read
      .option("header", "true")
      .option("inferSchema", "true") // Infer column types
      .csv("src/main/resources/customers.csv")

    // Step 3: Transformation - Filter Indian customers aged above 25
    val filteredDF = customerDF
      .filter($"country" === "India" && $"age" > 25)

    // Step 4: Action - Show the results
    println("Filtered Indian Customers (Age > 25):")
    filteredDF.show()

    // Step 5: Save result to output folder (optional action)
    // filteredDF.write.mode("overwrite").csv("output/filtered_customers")

    // Step 6: Stop Spark session
    spark.stop()
  }
}

