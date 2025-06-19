package csv

import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.types._

object explanation {

  def main(args: Array[String]): Unit = {

    // 1. Create SparkSession
    val spark = SparkSession.builder()
      .appName("CSV Reader Example")
      .master("local[*]") // use all cores on local machine
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR") // reduce Spark log spam

    // 2. Define file path
    val filePath = "src/main/resources/data.csv"

    // 3. Define schema (optional but better than inferSchema)
    val schema = StructType(Array(
      StructField("Name", StringType, nullable = true),
      StructField("Age", IntegerType, nullable = true),
      StructField("Country", StringType, nullable = true)
    ))

    // 4. Read CSV into DataFrame
    val df: DataFrame = spark.read
      .option("header", "true")          // First row = column names
      .option("inferSchema", "false")    // Don't auto-detect data types
      .option("mode", "DROPMALFORMED")   // Skip bad or corrupted rows
      .schema(schema)                    // Use our defined schema
      .csv(filePath)                     // Load CSV

    // 5. Show data
    df.show()

    // 6. Print schema of the DataFrame
    df.printSchema()

    // 7. Sample transformation: filter only adults (age >= 18)
    val adults = df.filter("Age >= 18")
    //val address = df.filter("India")
    adults.show()
    //address.show()
    val indians = df.filter("Country = 'India'")
    indians.show()

    val filteredDF = df.filter("Age > 23 AND Country = 'India' AND Name LIKE 'A%'")
    filteredDF.show()

    val filteredDF1 = df.filter("Age > 23 AND Country = 'USA' AND Name LIKE 'A%'")
    filteredDF1.show()

    // 8. Stop SparkSession
    spark.stop()
  }
}
