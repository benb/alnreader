package uk.ac.sanger.saa.cigarreader

import scala.io.Source

object CigarReverse{
  def main(args:Array[String])={
    args.foreach{a=>
      Source.fromFile(a).getLines.foreach{l=>
        println(Cigar(l).reverse)
      }
    }
  }
}
