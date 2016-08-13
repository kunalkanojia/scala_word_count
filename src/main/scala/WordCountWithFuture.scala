import java.io.File
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.io.Source

object WordCountWithFuture {

  def getFileCount(path: String): Future[Map[String, Int]] = {
    for {
      files <- getFilesList(path)
      result <- processFiles(files)
    } yield {
      result.toMap
    }
  }

  private def processFiles(files: Seq[File]): Future[Seq[(String, Int)]] = {
    val wordCountFutures = files.map(getWordCount)
    Future.sequence(wordCountFutures)
  }

  private def getWordCount(file: File): Future[(String, Int)] =
    Future {
      val wordCount = Source.fromFile(file).getLines.filterNot(_.trim.isEmpty).foldLeft(0)(_ + _.split("\\W+").length)
      (file.getName, wordCount)
    } recover {
      case e: java.io.IOException =>
        println(s"Oops Somethings wrong: ${e.getMessage}")
        (file.getName, 0)
    }

  private def getFilesList(rootPath: String): Future[Seq[File]] = Future { new File(rootPath).listFiles.filter(_.isFile).toList }

}