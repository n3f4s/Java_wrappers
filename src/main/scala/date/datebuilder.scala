class DateBuilder {
  var hour: Option[Hour] = None
  var day: Option[Day] = None
  var month: Option[Month] = None
  var year: Option[Year] = None
  def with_hour(h: Hour) = {
    hour = Some(h)
    this
  }
  def with_day(h: Day) = {
    day = Some(h)
    this
  }
  def with_month(h: Month) = {
    month = Some(h)
    this
  }
  def with_year(h: Year) = {
    year = Some(h)
    this
  }
  def build = new Date(
    year.getOrElse(Year(1970)),
    month.getOrElse(Month(1)),
    day.getOrElse(Day(1)),
    hour.getOrElse(Hour(12, 0)),
  )
}
