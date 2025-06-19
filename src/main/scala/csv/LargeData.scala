package csv
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object LargeData {

  def main(args: Array[String]): Unit = {

    // Step 1: Create SparkSession
    val spark = SparkSession.builder()
      .appName("People Data Processor")
      .master("local[*]") // For local testing; use "yarn" or "cluster" in production
      .getOrCreate()

    import spark.implicits._

    // Step 2: Read CSV File
    val filePath = "src/main/resources/sample_people_10k.csv" // Adjust path as needed

    val peopleDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(filePath)

    // Step 3: Display schema and first few rows
    println("=== Schema ===")
    peopleDF.printSchema()

    println("=== Sample Data ===")
    peopleDF.show(5)

    // Step 4: Filter Conditions
    val filteredDF = peopleDF
      .filter($"Age" > 23)
      .filter($"Country" === "India")
      .filter($"Name".startsWith("A"))

    println("=== Filtered Data ===")
    filteredDF.show(10)

    // Step 5: Select Specific Columns
    val selectedDF = filteredDF.select("ID", "Name", "Age", "Email")

    println("=== Selected Columns ===")
    selectedDF.show(10)

    // Step 6: Save Filtered Data to New CSV
    selectedDF.write
      .option("header", "true")
      .mode("overwrite")
      .csv("output/filtered_people")

    // Step 7: Stop SparkSession
    spark.stop()
  }
}


