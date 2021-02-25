import java.time.{LocalDateTime, LocalDate, LocalTime, ZonedDateTime, ZoneId}
import java.time.format.DateTimeFormatter

import scala.concurrent.duration.Duration

package date {
  sealed trait Interval {
    def abs_duration: Duration
  }
  case class Ago(dur: Duration) extends Interval {
    def abs_duration: Duration = if(dur.toMillis < 0) { -dur } else { dur }
  }
  case class Until(dur: Duration) extends Interval {
    def abs_duration: Duration = dur
  }
  case class Same(dur: Duration) extends Interval {
    def abs_duration: Duration = dur
  }

  object Date {
    // FIXME: add TZ
    def unapply(date: Date): Option[(Int, Int, Int, Int, Int)] = {
      Some((date.inner.getYear(), date.inner.getMonthValue, date.inner.getDayOfMonth(),
            date.inner.getHour(), date.inner.getMinute()))
    }
    def iterate(from: Date, end: Date, step: Duration): Iterator[Date] = {
      if(from.isBefore(end)) {
        new Iterator[Date] {
          var curr = from
          def next = {
            val tmp = curr
            curr = curr + step
            tmp
          }
          def hasNext: Boolean = {
            curr.isAfter(end) // FIXME: make sure that isAfter doesn't trigger if isSame
          }
        }
      } else {
        new Iterator[Date] {
          var curr = from
          def next = {
            val tmp = curr
            curr = curr - step
            tmp
          }
          def hasNext: Boolean = {
            curr.isBefore(end) // FIXME: make sure that isBefore doesn't trigger if isSame
          }
        }
      }
    }
  }

  class Date private[date] (private val inner: ZonedDateTime) {
    def this(myear: DSL.Year, mmonth: DSL.Month, mday: DSL.Day,
             mhour: DSL.Hour, tz: ZoneId = ZoneId.systemDefault) = {
      this(ZonedDateTime.of(myear.year,
                            mmonth.month,
                            mday.day,
                            mhour.hours,
                            mhour.minutes,
                            0,
                            0,
                            tz))
    }
    def day = DSL.Day(inner.getDayOfMonth)
    def month = DSL.Month(inner.getMonthValue)
    def year = DSL.Year(inner.getYear)
    def hour = DSL.Hour(inner.getHour, inner.getMinute)

    def +(rhs: Duration): Date = new Date(inner.plusNanos(rhs.toNanos))
    def -(rhs: Duration): Date = new Date(inner.minusNanos(rhs.toNanos))

    def isBefore(rhs: Date): Boolean = inner.isBefore(rhs.inner) && !inner.isEqual(rhs.inner)
    def isAfter(rhs: Date): Boolean  = inner.isAfter(rhs.inner) && !inner.isEqual(rhs.inner)
    def isSame(rhs: Date): Boolean   = inner.isEqual(rhs.inner)

    def time_until(rhs: Date): Interval = {
      import java.time.temporal.ChronoUnit
      import scala.concurrent.duration._
      val res = inner.until(rhs.inner, ChronoUnit.MILLIS)
      if(res > 0) {
        Until(res.millis)
      } else if(res < 0) {
        Ago(res.millis)
      } else {
        Same(res.millis)
      }
    }

  }
}

