package sandbox.monoid

import SetMonoids._
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class MonoidSpec extends AnyWordSpec with Matchers {

  "Set Monoid" should {
    "be resolver" in {

      val momo = union[Int]

      momo.combine(Set(1), Set(2)) shouldEqual Set(1, 2)

    }

  }

}
