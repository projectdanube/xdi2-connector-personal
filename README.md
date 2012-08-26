<a href="http://projectdanube.org/" target="_blank"><img src="http://peacekeeper.github.com/xdi2/images/projectdanube_logo.png" align="right"></a>
<img src="http://peacekeeper.github.com/xdi2/images/logo64.png"><br>

This is a connector module for the [XDI²](http://github.com/peacekeeper/xdi2) library.

It can map personal data from the [Personal.com API](http://developer.personal.com/faq) to XDI. 

### Information

* [Notes](https://github.com/peacekeeper/xdi2-connector-personal/wiki/Notes)
* [Mapping](https://github.com/peacekeeper/xdi2-connector-personal/wiki/Mapping)
* [Sequences](https://github.com/peacekeeper/xdi2-connector-personal/wiki/Sequences)

### How to build

Just run

    mvn clean install

To build all components.

### How to run

    mvn jetty:run

Then use an XDI client to send XDI messages to

    http://localhost:9090/xdi/personal

### Community

Google Group: http://groups.google.com/group/xdi2
