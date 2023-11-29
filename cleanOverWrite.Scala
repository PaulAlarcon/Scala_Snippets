
import org.apache.hadoop.fs.{Path, FileSystem)

val tempAvroFilePath = "data/1/avro-temp"
modifiedDF.write.format("avro").mode("overwrite").save(tempAvroFilePath)

val fs = FileSystem.get(spark.sparkContext.hadoopConfiguration)
fs.delete(new Path(avroFilePath), true)
fs.rename(new Path(tempAvroFilePath), new Path(avroFilePath))
