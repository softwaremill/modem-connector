[![Build Status](http://52.28.22.127:8080/buildStatus/icon?job=modem-connector)](http://52.28.22.127:8080/job/modem-connector)
# modem-connector
A connector library for communicating with modem software (like eg soundmodem) through AGWPE connection. This library allows to listen for received AX.25 frames on the soundmodem as well as to send AX.25 frames up.

## High Level View, Basic Architecture and Usage

![alt tag](https://github.com/softwaremill/modem-connector/blob/master/docs/images/modem-connector-docs-1.png)

Modem-Connector library communicates with SoundModem through AGWPE connection. The soundmodem can be replaced with any AGWPE
enabled modem supporting AGWPE protocol. AGWPE communication is implemented using simple JVM Socket mechanism over TCP/IP.

AGWPE Frames are sent over the communication channel between the library itself and the AGWPE enabled modem software, specific 
port and host needs to be provided either through configuration file, runtime flags or constructor injection.

AGWPE frame is characterized by its command type (single lower/upper case letter) and its payload. The library focuses mainly on 
sending and receiving AX.25 Row data frames and supports only a couple of additional commands.

When AX.25 frame is received on the SoundModem, the AGWPE frame is sent over to the Modem-Connector library, the payload of this
AGWPE frame is the raw AX.25 frame. The Ax.25 frame gets extracted and its passed over to the subscribed listeners.

### Basic Usage

```scala
  val connector: AGWPEConnector = new AGWPEConnector
  val observer: PrintLineAX25Observer = new PrintLineAX25Observer
  connector.addAX25MessageObserver(observer)
  connector.startConnection()
```
or


```scala
  val connector: AGWPEConnector = new AGWPEConnector("192.168.1.11", 8000, 3000)
  val observer: PrintLineAX25Observer = new PrintLineAX25Observer
  connector.addAX25MessageObserver(observer)
  connector.startConnection()
```

### AX.25 Listeners

The most basic listener printing the content of received AX.25 Frame to console

```scala
class PrintLineAX25Observer extends Observer[AX25Frame] {

  override def receiveUpdate(subject: AX25Frame): Unit = {
    // scalastyle:off regex
    println(subject)
    // scalastyle:on regex
  }
}
```

### Listening for Service Messages
Currently the Modem-Connector library supports listening for 3 types of messages:

 * *Version*
 * *ConnectStatus*
 * *DisconnectStatus*

*Version* Service Message is received on library startup when AGWPE Connection is sucessfully established with the SoundModem.

Listener for received Service Messages

```scala
class ServiceMessageListener extends Observer[ServiceMessage] {

override def receiveUpdate(message: ServiceMessage): Unit = {
    // scalastyle:off regex
    println(message)
    // scalastyle:on regex
  }
}

```

### Sending AX.25 Frames over to AGWPE Enabled Modem (SoundModem)

*connector* object (AGWPEConnector) created at the beggining is a single entry point for modem communication, to send the AX.25
message over to SoundModem the following method exposed by *connector* needs to be executed:

```scala
  def sendAx25Frame(frame: AX25Frame): Unit
```


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
