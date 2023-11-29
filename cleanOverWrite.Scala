import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.hadoop.fs.{FileSystem, Path}

object AvroDataWriter {
  def overwriteAvroFile(avroFilePath: String, tempAvroFilePath: String, modifiedDF: DataFrame): Unit = {
    // Create a Spark session
    val spark = SparkSession.builder.appName("AvroDataWriter").getOrCreate()

    // Handle exceptions and ensure proper cleanup
    try {
      val fs = FileSystem.get(spark.sparkContext.hadoopConfiguration)
      if (fs.exists(new Path(avroFilePath))) {
        fs.delete(new Path(avroFilePath), true)
      }

      modifiedDF.write.format("avro").mode("overwrite").save(tempAvroFilePath)

      if (fs.exists(new Path(tempAvroFilePath))) {
        fs.rename(new Path(tempAvroFilePath), new Path(avroFilePath))
      } else {
        throw new RuntimeException("Write to temporary location failed.")
      }

    } catch {
      case e: Exception =>
        e.printStackTrace()

    }
  }
