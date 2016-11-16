# pwsat-modem-lib
Modulator and Demodulator for HAM Radio AX.25 audio signals

## Running

`sbt run`

or if the jar was already built with `sbt assembly` then execute:

`java -jar path-to-the-jar/pwsat-modem-lib.jar`

## Testing

`sbt test`

## Checkstyle

`sbt scalastyle`

## Coverage

`sbt clean coverage test`


*Please note that for proper test execution the appropriate properties (like eg. valid soundinterface name) must be set*
