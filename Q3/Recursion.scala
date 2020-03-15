
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper


object JsonTestApp extends App {
  val jSonString = "{\"a\": {\"b\":{\"c\":\"d\"}}}".stripMargin

  val objectMapper = new ObjectMapper() with ScalaObjectMapper
  objectMapper.registerModule(DefaultScalaModule)
  objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  type myelement = scala.collection.Map[String, AnyRef]
  var p : AnyRef = null

  val rootNode:myelement = objectMapper.readValue[myelement](jSonString)
  println(rootNode)

  var key: String = null

  {
    key = "a"
    getValue(rootNode, key)
    printValues
  }

  {
    key  = "b"
    getValue(rootNode, key)
    printValues
  }

  {
    key = "c"
    getValue(rootNode, key)
    printValues
  }

  {
    key  = "g"
    getValue(rootNode, key)
    printValues
  }





  def printValues = println(s"key: $key ------> $p")

  def getValue(rootNode: myelement, key: String): AnyRef = {
    assert(!key.isEmpty)

    for ((k, v) <- rootNode) {
      if ( key == k ) // if key matches we have found it
        p = v.toString
      else {
        if (!v.isInstanceOf[myelement]) // must be last element (no more maps to traverse)
          p = "not found"
        else
          getValue(v.asInstanceOf[myelement], key) // call again with to search one more level
      }
    }
    p
  }

}
