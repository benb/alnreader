package uk.ac.sanger.saa.cigarreader
import scala.Array.Projection
import scala.io.Source
import scala.reflect._
object CigarReader{
  def main(args:Array[String]){
    val str = "cigar::50 SRR002579.3.q1k 157 2 - 6 3158335 3158490 + 119 M 116 I 1 M 40"
    val m = Cigar(str)
    println(str)
    println(m)
    println(m.reverse)
  }
}
object MappingType extends Enumeration("M","I","D"){
  val Match, Insertion, Deletion = Value
  def apply(s:String)={
    s match{
      case "M"=>Match
      case "I"=>Insertion
      case "D"=>Deletion
    }
  }
}

class Mapping(val t:MappingType.Value,val length:Int){
  import MappingType._
  override def toString():String = {
    t.toString + " " + length
  }
}

object Mapping{
  def apply(s:Projection[String]):Seq[Mapping] = {
    val nums = s.zipWithIndex.filter{x=>
      val (sI,ind)=x
      (ind%2==1)
    }.map{x=>
      val(sI,ind)=x
      Integer.parseInt(sI)
     } 
    val typeStr = s.zipWithIndex.filter{x=>val (s,ind)=x;(ind%2==0)}.map{x=>val(t,ind)=x;t}
    typeStr.zip(nums).map{x=>
      val (t,length)=x
      Mapping(t,length)
    }
  }
  def apply(t:String,length:Int):Mapping = {Mapping(MappingType(t),length)}
  
  def apply(t:MappingType.Value,length:Int):Mapping = {
    new Mapping(t,length)
  }
}


class Cigar(val name:String,val qual:Int,val mapStart:BigInt,val mapEnd:BigInt,val chr:String,val readStart:Int,val readEnd:Int,val dir:Boolean,val swScore:Int,val mapping:Seq[Mapping]){
  
  lazy val mapLeft=min(mapStart,mapEnd)
  lazy val mapRight=max(mapStart,mapEnd)
  
  
  def mappedBases() = {
    mapping.filter{_.t==MappingType.Match}.foldLeft(0){_+_.length}
  }
  
  implicit def toBigIntI(x:Int)={BigInt(x)}
  
  def readMax()={
    max(readStart,readEnd)
  }
  
  def readMin()={
    min(readStart,readEnd)
  }
  override def toString():String ={
    //cigar::50 SRR002579.3.q1k 157 2 - 6 3158335 3158490 + 119 M 156 
    "cigar::"+qual+" "+name+" "+readStart+ " " + readEnd + " " + {if (dir)"+"else{"-"}} + " " + chr + " " + mapStart + " " + mapEnd + " + " + swScore + " " + mapping.mkString(""," ","")
  }
  def reverse():Cigar = {
    new Cigar(name,qual,mapStart,mapEnd,chr,readEnd,readStart,!dir,swScore,mapping.reverse)
  }
  def overlaps(pos:Int,window:Int):Boolean={
    ((pos>(mapStart-window)) && (pos<(mapEnd+window)))
  }
  def overlaps(otherChr:String,pos:Int,window:Int):Boolean={
    (otherChr==chr && overlaps(pos,window))
  }
  def overlapRead(c:Cigar):Int={
    var other = c
       
    if (other.readMin > readMax){0}
    else if (other.readMax < readMin){0}
    else {(min(other.readMax,readMax)-max(other.readMin,readMin)).intValue +1}
  }
  
  private def min(i:BigInt,j:BigInt)={
    if (i<j){i}else{j}
  }
  private def max(i:BigInt,j:BigInt)={
    if(i<j){j}else{i}
  }
  
  def overlaps(range:(BigInt,BigInt),window:Int):Boolean = {
 
    val(i,j)=range
    
    intOverlap(min(i,j)-window,max(i,j)+window,min(mapStart,mapEnd),max(mapStart,mapEnd))
  }
  def overlaps(otherChr:String,range:(BigInt,BigInt),window:Int):Boolean={
    
    otherChr==chr && overlaps(range,window)
  }
  def overlaps(otherChr:String,range:Range,window:Int):Boolean={
    overlaps(otherChr,(BigInt(range.first),BigInt(range.last)),window)
  }
  
  private def intOverlap(low:BigInt,high:BigInt,low2:BigInt,high2:BigInt):Boolean={
    ((low<=low2 && high>=low2)||(low <= high2 && low >= low2))
  }
  
  
}

object Cigar{
  implicit def toBigInt(x:String)={BigInt(x)}
    implicit def toInt(x:String)={Integer.parseInt(x)}
  def apply(s:String)= {
    
    val fields = s.split(" ")
    val qual=fields(0).split("::")(1)
    val name = fields(1)
    val mapStart=fields(6)
    val mapEnd=fields(7)
    val readStart=fields(2)
    val readEnd=fields(3)
    val dir = {fields(4)=="+"}
    val chr = fields(5)
    val mapping=Mapping(fields.slice(10,s.length))
    val swScore=fields(9)
    val x = new Cigar(name,qual,mapStart,mapEnd,chr,readStart,readEnd,dir,swScore,mapping)
    
    x 
  }
  
  def fromAlign(s:String)={
    
    val fields = s.split("\\s+")
    val qual=fields(0).split("::")(1)
    val name=fields(2)
    val chr=fields(3)
    val readStart:Int=fields(4)
    val readEnd:Int=fields(5)
    val mapStart=fields(6)
    val mapEnd=fields(7)
    val dir={fields(8)=="F"}
    val swScore=fields(9)
    new Cigar(name,qual,mapStart,mapEnd,chr,readStart,readEnd,
                      dir,swScore,(new Mapping(MappingType.Match,Math.abs(readEnd-readStart))::List()))
  }
}

