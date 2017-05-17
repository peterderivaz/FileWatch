package example

import scala.swing._
import akka.actor.ActorSystem
import com.beachape.filemanagement.MonitorActor
import com.beachape.filemanagement.RegistryTypes._
import com.beachape.filemanagement.Messages._
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds._

class UI(fname: String) extends MainFrame {

  title = "Watching " + fname

  val display = new TextArea { rows = 8; lineWrap = true; wordWrap = true }

  def fontsize(delta: Float) = {
    val font: Font = display.font
    val size = font.getSize() + delta
    display.font = font.deriveFont(size)
  }

  contents = new BoxPanel(Orientation.Vertical) {
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += Button("Bigger") { fontsize(3f) }
      contents += Swing.HGlue
      contents += Button("Smaller") { fontsize(-3f) }
    }
    contents += new ScrollPane(display) // Needed to allow scrolling
    border = Swing.EmptyBorder(10, 10, 10, 10)
  }
}

object Hello extends App {
  if (args.length == 0)
    println("Usage: watch <filename>")
  else {
    implicit val system = ActorSystem("actorSystem")
    val fname = args(0)
    val ui = new UI(fname)

    def go(): Unit = {
      ui.display.text = io.Source.fromFile(fname).mkString
    }

    println("Watching " + fname)
    go()
    ui.visible = true
    val fileMonitorActor = system.actorOf(MonitorActor(concurrency = 2))
    println(s"Waiting for ${fname} to be modified (press return to stop monitoring)...")
    fileMonitorActor ! RegisterCallback(
      event = ENTRY_MODIFY,
      path = Paths get fname,
      callback = { _ => go })
    io.StdIn.readLine()
    println("Quitting")
    system.terminate()
  }

}

