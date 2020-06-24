
object Date {
  case class DateSelector(val value: Int) {
    def day = Day(value)
    def month = Month(value)
    def year = Year(value)
  }
  case class HourBuilder(val min: Int) {
    def ::(hour: Int) = Hour(hour, min)
  }
  implicit def intToSelector(i: Int) = DateSelector(i)
  implicit def intToHourBuilder(i: Int) = HourBuilder(i)
  implicit def listToDate(d: DateFragmentList) = d.build(new DateBuilder)
}

class Date(val year: Year, val month: Month, val day: Day, val hour: Hour)

