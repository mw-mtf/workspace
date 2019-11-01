CharStream = require "../streams/CharStream"
XmlCharacterStream = require "../streams/XmlCharacterStream"
LogStream = require "../streams/LogStream"
pipe = require "../streams/Pipe"

charStream = new CharStream("teste02.xml")
xmlCharacterStream = new XmlCharacterStream()
logStream = new LogStream()

pipe logStream, xmlCharacterStream, charStream

charStream.start()