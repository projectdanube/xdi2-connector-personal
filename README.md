<a href="http://projectdanube.org/" target="_blank"><img src="http://projectdanube.github.com/xdi2/images/projectdanube_logo.png" align="right"></a>
<img src="http://projectdanube.github.com/xdi2/images/logo64.png"><br>

This is a connector plugin for the [XDI2](http://github.com/projectdanube/xdi2) server.

It can map personal data from the [Personal.com API](http://developer.personal.com/faq) to XDI. 

### Information

* [Notes](https://github.com/projectdanube/xdi2-connector-personal/wiki/Notes)
* [Mapping](https://github.com/projectdanube/xdi2-connector-personal/wiki/Mapping)
* [Sequences](https://github.com/projectdanube/xdi2-connector-personal/wiki/Sequences)

### How to build

First, you need to build the main [XDI2](http://github.com/projectdanube/xdi2) project.

After that, just run

    mvn clean install

To build the XDI2 plugin.

### How to run as standalone web application

    mvn clean install jetty:run -P war

Then access the web interface at

	http://localhost:9092/

Or access the server's status page at

	http://localhost:9092/xdi

Or use an XDI client to send XDI messages to

    http://localhost:9092/xdi/personal

### Community

Google Group: http://groups.google.com/group/xdi2

IRC: irc://irc.freenode.net:6667/xdi
