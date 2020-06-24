import org.scalatest.funsuite.AnyFunSuite

import Date._

class DateTest extends AnyFunSuite {
  test("Inner: chain building") {
    val date: DateFragmentList = 24.day / 5.month / 2020.year
    val chain = Node(Year(2020),
                     Node(Month(5),
                          Node(Day(24),
                               End)))
    assert(date == chain)
  }
  test("DSL: Hour construction") {
    val hour = 20::0
    assert(hour.hours == 20 && hour.minutes == 0)
  }
  test("DSL: Full date construction") {
    val date: Date = (24.day / 5.month / 2020.year) at (20::0)
    assert(date.day.day == 24, "Days")
    assert(date.month.month == 5, "Months")
    assert(date.year.year == 2020, "Years")
    assert(date.hour.hours == 20 && date.hour.minutes == 0, "Hours")
    val date2: Date = (24.day / 5.month / 2020.year)
    assert(date2.day.day == 24, "Days")
    assert(date2.month.month == 5, "Months")
    assert(date2.year.year == 2020, "Years")
  }
  test("String parsing") {
    assert(false, "TODO")
  }
  test("Date manipulation") {
    assert(false, "TODO")
  }

}
