import org.scalatest.funsuite.AnyFunSuite

import date.conversions._
import date._
import scala.concurrent.duration._

// FIXME: better testing ← add randomisation

object Utility {
  val rand_gen = scala.util.Random
  def random_day = rand_gen.nextInt(27) + 1
  def random_month = rand_gen.nextInt(11) + 1
  def random_year = rand_gen.nextInt(2100)
  def random_hour = rand_gen.nextInt(24)
  def random_min = rand_gen.nextInt(60)
  def random_date: Date = {
    val year = random_year
    val month = random_month
    val day = random_day
    val hour = random_hour
    val min = random_min
    (year.y / month.m / day.d) at (hour :: min)
  }
  def random_duration = rand_gen.nextInt.day
}

class DateTest extends AnyFunSuite {
  test("Date Building: DSL") {
    val date: Date = (24.d / 5.m / 2020.y) at (20::0)
    date match {
      case Date(2020, 5, 24, 20, 0) ⇒ assert(true)
      case _ ⇒ {
        assert(false,
               s"${date.year.year}/${date.month.month}/${date.day.day} ${date.hour.hours}:${date.hour.minutes}")
      }
    }
  }
  test("Date Building: String Parsing") {
    val date = ("24/05/20 20:00" formatted_as "dd/MM/yy HH:mm")
    date match {
      case Date(2020, 5, 24, 20, 0) ⇒ assert(true)
      case _ ⇒ {
        assert(false,
               s"${date.year.year}/${date.month.month}/${date.day.day} ${date.hour.hours}:${date.hour.minutes}")
      }
    }
  }
  test("Date Building: Object construction") {
    // FIXME add TZ
    val date = new Date(2020.y, 5.m, 24.d, DSL.Hour(20, 0))
    date match {
      case Date(2020, 5, 24, 20, 0) ⇒ assert(true)
      case _ ⇒ {
        assert(false,
               s"${date.year.year}/${date.month.month}/${date.day.day} ${date.hour.hours}:${date.hour.minutes}")
      }
    }
  }
  test("Date Manipulation") {
    val date = new Date(2020.y, 5.m, 24.d, DSL.Hour(20, 0))
    val date2 = date + 1.day
    val date3 = date - 1.day
    date2 match {
      case Date(2020, 5, 25, 20, 0) ⇒ assert(true)
      case _ ⇒ {
        assert(false,
               s"${date2.year.year}/${date2.month.month}/${date2.day.day} ${date2.hour.hours}:${date2.hour.minutes}")
      }
    }
    date3 match {
      case Date(2020, 5, 23, 20, 0) ⇒ assert(true)
      case _ ⇒ {
        assert(false,
               s"${date3.year.year}/${date3.month.month}/${date3.day.day} ${date3.hour.hours}:${date3.hour.minutes}")
      }
    }
  }
  test("Iteration Over Date Range: Exact end") {
    def cmp: (((Date, Date)) ⇒ Boolean) = { case (d1, d2) ⇒ d1.isSame(d2) }
    def acmp(a1: Array[Date], a2: Array[Date]) = a1.zip(a2).forall(cmp)
    val start = (24.d / 5.m / 2020.y) at (20::0)
    val end = (29.d / 5.m / 2020.y) at (20::0)
    val forward = Date.iterate(start, end, 1.day).toArray
    val backward = Date.iterate(end, start, 1.day).toArray
    val f_res = Array(
      ((24.d / 5.m / 2020.y) at (20::0)),
      ((25.d / 5.m / 2020.y) at (20::0)),
      ((26.d / 5.m / 2020.y) at (20::0)),
      ((27.d / 5.m / 2020.y) at (20::0)),
      ((28.d / 5.m / 2020.y) at (20::0)),
      ((29.d / 5.m / 2020.y) at (20::0)),
    )
    val b_res = f_res.reverse
    assert(acmp(f_res, forward))
    assert(acmp(b_res, backward))
  }
  test("Iteration Over Date Range: End between last step and last step + 1") {
    def cmp: (((Date, Date)) ⇒ Boolean) = { case (d1, d2) ⇒ d1.isSame(d2) }
    def acmp(a1: Array[Date], a2: Array[Date]) = a1.zip(a2).forall(cmp)
    val start = (24.d / 5.m / 2020.y) at (20::0)
    val end = (29.d / 5.m / 2020.y) at (20::0)
    val forward = Date.iterate(start, end, 2.day).toArray
    val backward = Date.iterate(end, start, 2.day).toArray
    val f_res = Array(
      ((24.d / 5.m / 2020.y) at (20::0)),
      ((26.d / 5.m / 2020.y) at (20::0)),
      ((28.d / 5.m / 2020.y) at (20::0)),
    )
    val b_res = Array(
      ((29.d / 5.m / 2020.y) at (20::0)),
      ((27.d / 5.m / 2020.y) at (20::0)),
      ((25.d / 5.m / 2020.y) at (20::0)),
    )
    assert(acmp(f_res, forward))
    assert(acmp(b_res, backward))
  }
  test("Date Comparison: Hardcoded test") {
    // FIXME: improve with randomness and split test by property
    val start = (24.d / 5.m / 2020.y) at (20::0)
    val end = (29.d / 5.m / 2020.y) at (20::0)
    assert(start isBefore end)
    assert(!(end isBefore start))
    assert(!(end isBefore end))
    assert(!(start isBefore start))

    assert(end isAfter start)
    assert(!(start isAfter end))
    assert(!(end isAfter end))
    assert(!(start isAfter start))

    assert(start isSame start)
    assert(end isSame end)
    assert(!(end isSame start))
    assert(!(start isSame end))
  }
  test("Date comparison: exclusivity") { // FIXME find the real name of operation property
    val d1 = Utility.random_date
    val d2 = Utility.random_date
    if(d1 isBefore d2) {
      assert(!(d1 isAfter d2))
      assert(!(d1 isSame d2))
    } else if(d1 isAfter d2) {
      assert(!(d1 isBefore d2))
      assert(!(d1 isSame d2))
    } else {
      assert(!(d1 isBefore d2))
      assert(!(d1 isAfter d2))
    }
  }
  test("Date comparison: switching order") { // FIXME find the real name of operation property
    val d1 = Utility.random_date
    val d2 = Utility.random_date
    if(d1 isBefore d2) {
      assert(d2 isAfter d1)
    } else if(d1 isAfter d2) {
      assert(d2 isBefore d1)
    } else {
      assert(d2 isSame d1)
    }
  }
  test("Date comparison: self equality") { // FIXME find the real name of operation property
    val d1 = Utility.random_date
    assert(!( d1 isAfter d1 ))
    assert(!( d1 isBefore d1 ))
    assert(d1 isSame d1)
  }
  test("Date Comparison: Duration") {
    val start = (24.d / 5.m / 2020.y) at (20::0)
    val end = (29.d / 5.m / 2020.y) at (20::0)
    start.time_until(end) match {
      case Until(d) ⇒ assert(d == 5.day)
      case _ ⇒ assert(false)
    }
    end.time_until(start) match {
      case Ago(d) ⇒ assert(d == -5.day)
      case _ ⇒ assert(false)
    }
    end.time_until(end) match {
      case Same(_) ⇒ assert(true)
      case _ ⇒ assert(false)
    }
  }
  test("Timezones") {
    // Comparison between different date on different timezones
    // TZ conversions
    assert(false)
  }
}
