import org.apache.spark.sql.{SparkSession, DataFrame, Row}
import org.apache.spark.sql.functions.col

case class Person(
  name: String, 
  age: Int, 
  address: Option[String], 
)

object Person {
  def apply(row: Row): Person = {
    val fieldss = classOf[Person].getDeclaredFields.map(_.getName)

    val fv = fieldss.map(f =>
      f match {
        case "name" => row.getAs[String](f)
        case "age" => row.getAs[Int](f)
        case "address" => Option(row.getAs[String](f))
      }
    )

    Person(fv(0).asInstanceOf[String], fv(1).asInstanceOf[Option[Int]], fv(2).asInstanceOf[Option[String]])
  }
}

object optionalFieldsSchema extends App {

  val spark = SparkSession.builder().appName("Example").master("local").getOrCreate()

  val avroFilePath = "???"

  val untypedDataFrame: DataFrame = spark.read.format("avro").load(avroFilePath)

  val typedDataset = untypedDataFrame.rdd.map(Person.apply).toDS()

  typedDataset.show()
}
