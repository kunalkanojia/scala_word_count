import org.scalatest.{MustMatchers, WordSpec}
import org.scalatest.concurrent._
import org.scalatest.time.{Millis, Seconds, Span}


class WordCountWithFutureSpec extends WordSpec with MustMatchers with ScalaFutures{

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(20, Seconds), interval = Span(5, Seconds))

  "A word count helper" should {

    "return correct of word count from the files" in {
      val resultFuture = WordCountWithFuture.getFileCount("src/main/resources/")

      whenReady(resultFuture){ result =>
        result.size mustEqual 20
        result.get("File1.txt") mustBe Some(6480000)
        result.get("File2.txt") mustBe Some(6480000)
      }
    }
  }
}
