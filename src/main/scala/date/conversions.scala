package date {
  package object conversions {
    case class DateSelector(val value: Int) {
      import date.DSL._
      def dayOfMonth = Day(value)
      def monthValue = Month(value)
      def yearValue = Year(value)
      def d = Day(value)
      def m = Month(value)
      def y = Year(value)
    }
    implicit def intToSelector(i: Int) = DateSelector(i)
    implicit def intToHourBuilder(i: Int) = DSL.HourBuilder(i)
    implicit def strToStrDateBuilder(s: String) = parsing.StrDateBuilder(s)
  }
  package object Internal {
    implicit def listToDate(d: DateFragmentList) = d.build(new DateBuilder)
  }
}
