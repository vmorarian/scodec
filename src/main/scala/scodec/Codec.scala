package scodec

import scalaz.{\/, StateT}

trait Codec[A] {
  def encode(a: A): Error \/ BitVector
  def decode(bits: BitVector): Error \/ (BitVector, A)
}

object Codec {

  type DecodingContext[+A] = StateT[({type λ[+a] = Error \/ a})#λ, BitVector, A]

  object DecodingContext {

    def apply[A](f: BitVector => Error \/ (BitVector, A)): DecodingContext[A] =
      StateT[({type λ[+a] = Error \/ a})#λ, BitVector, A](f)

  }

  def decode[A](codec: Codec[A], buffer: BitVector): Error \/ A = {
    codec decode buffer map { case (rest, result) => result }
  }
}