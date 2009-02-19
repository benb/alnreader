package uk.ac.sanger.saa.maqreader.util
import java.io.File

class MaqmapWrapper(file:File,newFormat:Boolean) extends Iterator[MaqRecord]{
  val reader = new MaqmapReader(file,newFormat)
  var nextRecord:Option[MaqRecord]=None
  
  override def next = {
    if (nextRecord.isDefined){
      val ret = nextRecord.get
      nextRecord=None
      ret
    }else {
      hasNext
      val ret = nextRecord.getOrElse(throw new NoSuchElementException("next on finished Iterator"))  
      nextRecord=None
      ret
    }
  }
  
  override def hasNext = {
    if (nextRecord.isDefined){
      true
    }else {
      val realNext = nextRecord.getOrElse(reader.getRecord())
      if (realNext==null){
        nextRecord=None
        false
      }else{
        nextRecord=Some(realNext)
        true
      }  
    }
  }
  
}
