# Nano-Logger
A Simple, Nimble, Thread-safe Logging Utility in Java.
 - It's ***simple***, because it's all a single class with static methods (no need for instantiation and object sharing), consisting an easy API with clear javadoc!
 - It's ***nimble***, because it's all a single class, using only standard Java, without any 3rd-party dependencies!
 - It's ***thread-safe***, because all static logging operations are atomic, and can be safely called from different threads, without worrying about synchronization!

## How to use it?
This simple utility can be used in two ways:
 1. Either copy the single `Logger` class to a proper package in your project source-code.
 2. Or add the JAR release as a 3rd-party library to your project CLASSPATH.

## What about licensing issues?
Nano-Logger is licensed under the terms of the **LGPL-v3.0**. If you read the LICENSE, you'll see there's no problem using it in your open-source or proprietary software project.

## What does the API look like?
Just check out the public interface of the single `Logger` class and it's javadoc. It's just super easy!

You can also check out the JUnit test codes, which serve as a live and executable API documentation.

Here's an example usage:

    // to be completed ...
