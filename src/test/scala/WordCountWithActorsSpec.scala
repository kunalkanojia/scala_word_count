import scala.concurrent.duration._

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Timeout
import org.scalatest.{MustMatchers, WordSpecLike}

class WordCountWithActorsSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with MustMatchers {

  def this() = this(ActorSystem("WordCountWithActorsSpec"))

  implicit val timeout = Timeout(5 seconds)

  "A word count helper" should {

    "return correct of word count from the files" in {

      val system = ActorSystem("word-count-system")

      val masterActor = system.actorOf(Props[WordCountMaster], name = "master")

      masterActor ! StartCounting("src/main/resources/", 20)

      expectMsgPF(20 seconds) {
        case WordCountSuccess(result) =>
          result.size mustEqual 20
          result.get("src/main/resources/File1.txt") mustBe Some(6480000)

        case _ => fail
      }
    }
  }

}
