sealed trait DateFragment {
  def /(next: DateFragment) = (End chain this chain next)
}
case class Day(val day: Int) extends DateFragment
case class Month(val month: Int) extends DateFragment
case class Year(val year: Int) extends DateFragment
case class Hour(val hours: Int, val minutes: Int)

sealed trait DateFragmentList {
  def chain(next: DateFragment) = Node(next, this)
  def /(next: DateFragment) = chain(next)
  def at(hour: Hour) = build(new DateBuilder() with_hour hour)
  def build(prev: DateBuilder): Date
}
case class Node(val fragment: DateFragment, val next: DateFragmentList) extends DateFragmentList {
  def build(prev: DateBuilder) = next.build(fragment match {
    case d: Day ⇒ (prev with_day d)
    case d: Month ⇒ (prev with_month d)
    case d: Year ⇒ (prev with_year d)
  })
}
case object End extends DateFragmentList {
  def build(prev: DateBuilder) = prev build
}
