import java.time.{LocalDateTime, LocalDate, LocalTime, ZonedDateTime, ZoneId}
import java.time.format.DateTimeFormatter

package date {
  package parsing {
    case class StrDateBuilder(s: String) {
      def formatted_as(fmt: String): Date = {
        new Date(ZonedDateTime.of(
                   LocalDateTime.parse(s, DateTimeFormatter.ofPattern(fmt)),
                   ZoneId.systemDefault()
                 ))
      }
    }
  }
}
