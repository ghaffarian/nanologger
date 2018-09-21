# Nano-Logger
*A Simple, Nimble, Thread-safe Logging Utility in Java*. 

While there are many great logging utilities for Java ( such as [Log4j](https://logging.apache.org/log4j/), [Logback](https://logback.qos.ch/), [Java Core Logging API](https://docs.oracle.com/javase/8/docs/api/java/util/logging/package-summary.html) ) sometimes you just need something plain simple, without adding any additional dependencies, XML configurations, DTDs, etc. That's exactly why I developed my own simple logging utility in Java: **Nano-Logger**.

 - It's ***simple***, because it's all a single class with simple static methods (no need for instantiation, object sharing, complex initialization and configurations), consisting an easy API with clear javadoc!
 - It's ***nimble***, because it's all a single class, using only standard Java, without any 3rd-party dependencies, no XML configurations, and no DTDs!
 - It's ***thread-safe***, because all static logging operations are atomic, and can be safely called from different threads, without worrying about synchronization!

## How to use it?
This simple utility can be used in two ways:
 1. Either copy the single `Logger` class to a proper package in your project source-code.
 2. Or add the JAR release as a 3rd-party library to your project CLASSPATH.

## What about licensing issues?
Nano-Logger is licensed under the terms of the **LGPL-v3.0**. If you read the LICENSE, you'll see there's no problem using it in your open-source or proprietary software project.

NOTE: If you believe the LGPL-v3 license prohibits you from using this code, then contact me in order to discuss the matter and resolve it.

## What does the API look like?
Just check out the public interface of the single `Logger` class and it's javadoc. It's just plain simple and easy to use!

You can also check out the JUnit test codes, which serve as a live and executable API documentation.

Here's an example usage:

    // to be completed ...

## Where is this project going?
Although I'm already satisfied with the current state of this utility class ( it just does the job for me ), I'm considering a few improvements as future work:
 - Adding support for `printf` style formatting APIs.
 - Making the API consistent with one of the well-known logging-API facades ( such as [SLF4J](https://www.slf4j.org/) or [Apache Commons Logging](http://commons.apache.org/proper/commons-logging/) ). I haven't thoroughly studied this yet; so, this will only happen if it doesn't conflict with the main goals of the project (***being simple and nimble***).
