package csv
import org.apache.spark.sql.SparkSession              // For creating SparkSession
import org.apache.spark.sql.DataFrame                 // For using DataFrame APIs (optional but good practice)
import org.apache.spark.sql.functions._

object DataFrameOperations {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("CustomerTransactionProcessor")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    // Step 1: Read CSVs
    val customerDF = spark.read.option("header", "true")
      .option("inferSchema", "true")
      .csv("src/main/resources/customers.csv")

    val transactionDF = spark.read.option("header", "true")
      .option("inferSchema", "true")
      .csv("src/main/resources/transactions.csv")

    // Step 2: Deduplicate customer records
    val dedupedCustomerDF = customerDF.dropDuplicates("CustomerID")

    // Step 3: Filter only successful transactions
    val successTxnDF = transactionDF.filter($"Status" === "Success")

    // Step 4: Join customers with successful transactions
    val joinedDF = dedupedCustomerDF.join(successTxnDF, "CustomerID")

    // Step 5: Select relevant columns
    val finalDF = joinedDF.select("CustomerID", "Name", "Country", "Age", "TransactionID", "Amount")

    // Step 6: Show and write output
    finalDF.show()
    finalDF.write
      .option("header", "true")
      .mode("overwrite")
      .csv("output/customer_success_txns")

    spark.stop()
  }

}
