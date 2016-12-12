# modem-connector
A connector library for communicating with modem software (like eg soundmodem) through AGWPE connection. This library allows to listen for received AX.25 frames on the soundmodem as well as to send AX.25 frames up.

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
