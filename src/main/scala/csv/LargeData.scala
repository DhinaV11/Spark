package csv
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

   /*Read a CSV file with 10,000 people records, and:
Filter those from India
Whose age > 23
And whose name starts with letter A
   Save the filtered result into a new CSV file*/

object LargeData {

  def main(args: Array[String]): Unit = {

    // Step 1: Create SparkSession
    //SparkSession is the entry point for using Spark SQL.
    //.master("local[*]") means it will run on your local machine using all cores.
    val spark = SparkSession.builder()
      .appName("People Data Processor")
      .master("local[*]") // For local testing;
      .getOrCreate()

    /*val spark =
    This declares a variable named spark which holds the SparkSession object.
    SparkSession is the entry point for working with DataFrames and Datasets in Spark (from version 2.x onwards).

    .builder()
     This begins the building of a SparkSession using the builder pattern.
     Think of this like a configuration setup block.

     .appName("People Data Processor")
     This sets a name for your Spark application. It shows up in:

     .master("local[*]")
     This tells Spark where to run.*/



    import spark.implicits._

    // Step 2: Read CSV File
    val filePath = "src/main/resources/sample_people_10k.csv" // i added in resources

    //Loads the CSV file as a DataFrame.
    val peopleDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(filePath)

    // Step 3: Display schema and first few rows
    println("=== Schema ===")
    peopleDF.printSchema()
    //Shows the structure (columns and types) of the data.

    println("=== Sample Data ===")
    peopleDF.show(5)

    // Step 4: Filter Conditions
    //Only age above 23.
    //Country must be India.
    //Name starts with A.
    val filteredDF = peopleDF
      .filter($"Age" > 23)
      .filter($"Country" === "India")
      .filter($"Name".startsWith("A"))

    println("=== Filtered Data ===")
    filteredDF.show(10)

    // Step 5: Select Specific Columns
    //Instead of exporting the full record, we keep only:
    //ID, Name, Age, Email
    val selectedDF = filteredDF.select("ID", "Name", "Age", "Email")

    println("=== Selected Columns ===")
    selectedDF.show(10)

    // Step 6: Save Filtered Data to New CSV
    //Writes the filtered data into a new folder called output/filtered_people
    //It will generate multiple .csv files if run in distributed mode.
    //.mode("overwrite") ensures old data is replaced.
    selectedDF.write
      .option("header", "true")
      .mode("overwrite")
      .csv("output/filtered_peoples")

    // Step 7: Stop SparkSession
    spark.stop()
  }
}


