This library contains wrappers around functionnalities from Java standard library that doesn't have
Scala equivalent. It aims to give a more scala-like and easier to use interface around those functionnalities

# Date
The date library aim to provide a unique interface for date creation and manipulation. It provide a DSL to do that.
FIXME: add what to import...
## Creation
### Now
FIXME: TODO
### Using DSL
The DSL provide the operator `/` and `::` to create dates. `/` is used to create the day and `::` the hours, both can be combinated with the `at` operator. Those three operators allow the construction of date in a way close to natural language. Since the order isn't fixed, we need to use the `y`, `m` and `d` functions to express the format of the date.
For example, to create the date `31/12/2020` at `20:00` we can do the following:
```scala
import date.conversions._
val date = (31.d / 5.m / 2020.y) at (20::0)
// or
val date = (5.m / 31.d / 2020.y) at (20::0)
// or
val date = ( 2020.y / 5.m / 31.d) at (20::0)
```
The order for hours is fixed as `hour::minutes`. Since scala doesn't like numbers with leading zeros, single digit minutes have to be written as single digit numbers.
The hour can be omited. If it's the case, it'll be set as `12:00`:
```scala
val date = (2020.yearValue / 5.monthValue / 31.dayOfMonth)
```
Due to some implicit conversions happening in the DSL backend, it might be necessary to add type
annotation on the variable to make sure the compiler infer the right type

### Parsing Strings
An other solution to build date is to parse string. The formatting is the same as the one used by Java standard library: [](TODO: actual link).
This library add a `formatted_as` function to `String`. It allow to parse date string this way:
```scala
val date = ("24/05/20 20:00" formatted_as "dd/MM/yy HH:mm")
```

## Comparison
### Pattern matching
Dates can be deconstructed using pattern matching, they have the following pattern: `Date(year, month, date, hour, minute)`
For example, the following date will be deconstructed like that:
```scala
import date.conversions._
val date = (31.d / 5.m / 2020.y) at (20::0)
date match {
  case Date(2020, 5, 31, 20, 0) ⇒ assert(true)
  case _ ⇒ assert(false)
}
```
### Comparison between dates
Dates have the functions `isSame`, `isBefore` and `isAfter` to compare two dates together.
example:
```scala
assert(start isBefore end)
assert(start isAfter end)
assert(start isSame end)
```
`isBefore` and `isAfter` are strict comparison meaning that two date are equals (so `date.isSame(date2) == true`) the `isBefore` and `isAfter` will be `false`.
### Computing duration between date
Todo

## Manipulation
### Adding/Substracting duration
TODO
### Retreiving informations
TODO
#### TimeZone!!!!
TODO

