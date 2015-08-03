[forklabs](http://code.google.com/p/forklabs/) >> **forklabs-javaxpcom**

The _forklabs-javaxpcom_ open source project provides the skeleton to build a web crawler using Mozilla and SWT.



# Introduction #
The Crawler is designed to painlessly navigate to web pages and wait for them to load and to give access to the underlying document using JavaXPCOM.

The source code includes [an example Crawler](http://code.google.com/p/forklabs-javaxpcom/source/browse/src/test/java/ca/forklabs/javaxpcom/ProjectPageCrawler.java) that will come to this page and read the label on each of the top most menu (Project Home, Downloads, ...). From this example Crawler it is simple to build a more complex Crawler that can follow links, fill in forms and collect even more data.

Finally, one can create a Crawler from an existing Display (and thus control the event thread) or one can let the Crawler create its own SWT deamon thread.


# Getting Started #
Using this library to make an automated crawler is quite easy. To make a more interactive crawler or leverage other facets of the library is much more difficult. Long ago, the [Laboratorio de Algoritmia Distribuida y Redes](http://ladyr.es/) published _[XULRunner with Java: JavaXPCOM Tutorial](http://forklabs-javaxpcom.googlecode.com/hg/docs/XPCOMGuide.htm)_ (local copy) to help with such endeavour. Please note that the guide has been reproduced as is and might contain outdated information. However its essence should still be there to help you with your own projects.

# DOM Interaction #

## DOM Navigation ##
To help navigate and quickly find nodes of interest in the document, the crawler offers a selection mechanism based on node properties (i.e. node name, node attribute name, node attribute value). This mechanism is accessed with the [Selector API](http://forklabs-javaxpcom.googlecode.com/hg/docs/apidocs/ca/forklabs/javaxpcom/select/Selector.html) and method [selector()](http://forklabs-javaxpcom.googlecode.com/hg/docs/apidocs/ca/forklabs/javaxpcom/Crawler.html#selector()) on the crawler itself.


## DOM Inspection ##
Included in this package is [an utility class](http://forklabs-javaxpcom.googlecode.com/hg/docs/apidocs/ca/forklabs/javaxpcom/util/XPCOMInspector.html) targetted at developers whose purpose is to inspect DOM nodes. It will print on the standard output the main qualities of a node, such as but not limited to its type, its names, its attributes and its childrens.


## DOM Conversion ##
Another [utility class](http://forklabs-javaxpcom.googlecode.com/hg/docs/apidocs/ca/forklabs/javaxpcom/util/XPCOMConverter.html) allows easy conversion by hiding the calls to interface querying making the code much more readable.


# Download #
A [distribution](http://code.google.com/p/forklabs-javaxpcom/downloads/list) is available for download. It contains the source, the library, the documentation as well as a Windows version of SWT and a Windows version of XULRunner.


## Source ##
The [source code can be browsed](http://code.google.com/p/forklabs-javaxpcom/source/browse/) through the Mercurial web interface.


## Javadoc ##
The [Javadoc API can also be browsed](http://forklabs-javaxpcom.googlecode.com/hg/docs/apidocs/index.html) through the Mercurial web interface.


# Dependencies #
The crawler depends on two external tools. The first is SWT, a GUI library from Eclipse, and XULRunner from Mozilla. The correct versions for your operating system is required.
  * http://www.eclipse.org/swt/
  * http://releases.mozilla.org/pub/mozilla.org/xulrunner/releases/
The 32-bit Windows version each of these tools is included in the Mercurial repository. When installing XULRunner, make sure its path [does not contain any non-ASCII characters](https://bugs.eclipse.org/bugs/show_bug.cgi?id=326089).

The crawler also depends on library [forklabs-baselib](http://code.google.com/p/forklabs-baselib/), a sibling project containing core language features.
  * [Home page](http://code.google.com/p/forklabs-baselib/)
  * [Download page](http://code.google.com/p/forklabs-baselib/downloads/list)
That library is included in the distribution and has a compile entry in the POM.


# Compatibility #
It is recommended to run your own crawlers on a 32-bit JVM as compatibility problems were encountered while trying to run a crawler on 64-bit Windows machine. Digging down, it was found that [XULRunner does not have any official 64-bit version](http://releases.mozilla.org/pub/mozilla.org/xulrunner/releases/) (at least for Windows). This lack of version has for effect that the [64-bit version of SWT for Windows](http://download.eclipse.org/eclipse/downloads/drops/R-3.6-201006080911/index.php#SWT) is missing a glue library between SWT and XUL.

To fix it, one could hack SWT source code to compile the missing native library under 64-bit and then use [the unofficial 64-bit version of XULRunner](http://wiki.mozilla-x86-64.com/Download). But for the moment I do not have the resources for that.

## Stack Traces ##
This stacktrace is using a 32-bit version of SWT on a 64-bit JVM:
```
Exception in thread "SWT-Thread" java.lang.UnsatisfiedLinkError: Cannot load 32-bit SWT libraries on 64-bit JVM
	at org.eclipse.swt.internal.Library.loadLibrary(Unknown Source)
	at org.eclipse.swt.internal.Library.loadLibrary(Unknown Source)
	at org.eclipse.swt.internal.C.<clinit>(Unknown Source)
	at org.eclipse.swt.widgets.Display.<clinit>(Unknown Source)
```

This stacktrace is using a 64-bit version of SWT on a 32-bit JVM:
```
Exception in thread "SWT-Thread" java.lang.UnsatisfiedLinkError: Cannot load 64-bit SWT libraries on 32-bit JVM
	at org.eclipse.swt.internal.Library.loadLibrary(Unknown Source)
	at org.eclipse.swt.internal.Library.loadLibrary(Unknown Source)
	at org.eclipse.swt.internal.C.<clinit>(Unknown Source)
	at org.eclipse.swt.widgets.Display.<clinit>(Unknown Source)
```

This stack trace is running a 64-bit version of SWT on a 64-bit JVM:
```
Caused by: org.eclipse.swt.SWTError: No more handles (java.lang.UnsatisfiedLinkError: Could not load SWT library. Reasons: 
	no swt-xulrunner-win32-3650 in java.library.path
	no swt-xulrunner-win32 in java.library.path
	Can't load library: C:\Users\Forklabs\AppData\Local\Temp\swtlib-64\swt-xulrunner-win32-3650.dll
	Can't load library: C:\Users\Forklabs\AppData\Local\Temp\swtlib-64\swt-xulrunner-win32.dll
)
	at org.eclipse.swt.SWT.error(Unknown Source)
	at org.eclipse.swt.SWT.error(Unknown Source)
	at org.eclipse.swt.browser.Mozilla.initXULRunner(Unknown Source)
	at org.eclipse.swt.browser.Mozilla.create(Unknown Source)
	at org.eclipse.swt.browser.Browser.<init>(Unknown Source)
```


# Acknowledgement #
I wish to thank the [Laboratorio de Algoritmia Distribuida y Redes](http://ladyr.es/) for its help in kickstarting me with SWT, Mozilla and JavaXPCOM as well as allowing me to publish some of their code and their documentation.