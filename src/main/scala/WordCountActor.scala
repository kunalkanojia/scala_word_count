
import akka.actor.{Actor, ActorRef, Props}
import java.io._
import scala.io._

case class FileToCount(fileName: String)
case class WordCount(fileName: String, count: Int)
case class StartCounting(docRoot: String, numActors: Int)
case class WordCountSuccess(result: Map[String, Int])

//TODO
class WordCountMaster extends Actor {

  var files: Seq[String] = Nil
  var wordCount: Map[String, Int] = Map.empty
  var requester: ActorRef = _

  def receive = {
    case StartCounting(docRoot, numActors) =>
      requester = sender()
      val workers = createWorkers(numActors)
      files = scanFiles(docRoot)
      beginSorting(files, workers)

    case WordCount(fileName, count) =>
      wordCount = wordCount + (fileName -> count)
      if(wordCount.size == files.size) {
        requester ! WordCountSuccess(wordCount)
      }
  }

  private def createWorkers(numActors: Int) = {
    for (i <- 0 until numActors) yield context.actorOf(Props[WordCountWorker], name = s"wc-worker-${i}")
  }

  private def scanFiles(docRoot: String) = new File(docRoot).list.map(docRoot + _)

  private def beginSorting(fileNames: Seq[String], workers: Seq[ActorRef]) {
    fileNames.zipWithIndex.foreach( e => {
      workers(e._2 % workers.size) ! FileToCount(e._1)
    })
  }

}

class WordCountWorker extends Actor {

  def receive = {
    case FileToCount(fileName: String) =>
      val count = countWords(fileName)
      sender ! WordCount(fileName, count)
  }

  def countWords(fileName: String) = {
    Source.fromFile(fileName).getLines.filterNot(_.trim.isEmpty).foldLeft(0)(_ + _.split("\\W+").length)
  }

}
