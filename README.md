[![Build Status](https://travis-ci.org/apis-and-processors/processor-tools.svg?branch=master)](https://travis-ci.org/apis-and-processors/processor-tools)
[![Download](https://api.bintray.com/packages/apis-and-processors/java-libraries/processor-tools/images/download.svg) ](https://bintray.com/apis-and-processors/java-libraries/processor-tools/_latestVersion)

# processor-tools

Utilities for dealing with generics and types

## Latest release

Can be sourced from jcenter like so:

    <dependency>
      <groupId>com.github.aap</groupId>
      <artifactId>processor-tools</artifactId>
      <version>0.0.1</version>
      <classifier>sources|tests|javadoc|all</classifier> (Optional)
    </dependency>
    
## Motivation

In designing the [api-processor](https://github.com/apis-and-processors/api-processor) I needed a way to look at a given class and resolve all of it's potential types and then compare those types to their runtime implementations passed in by the end user. While libraries like [guava](https://github.com/google/guava) and [typetools](https://github.com/jhalterman/typetools) do type-related work, and indeed the former is used here to some extent, I required a different approach that had built in support for comparing classes with potentially N number of types.

Futhermore I needed a way to work with primitives in a type-esque related way and to be able to convert said primitives to their boxed types and then back again with ease.

While the main work horse of this library is `TypeUtils` it is by no means the only thing it does. This library is intended to house all common and generic code which can be used by the various libraries under the `apis-and-processors` umbrella.

## Setup and How to use

While this library has many tools most folks will be interested in `TypeUtils` and how to convert a given class with types to a `ClassType`. Consider the following class:


    abstract class HelloWorld implements Function<Integer, Boolean>, Comparable<String> {
        @Override
        public Boolean apply(final Integer instance) {
            return null;
        }
    }

It implements Function with types `Integer` and `Boolean` on top of implementing Comparable with type `String`. To convert this into a `ClassType` one can either use the class definition itself:

    ClassType helloWorldType = TypeUtils.parseClassType(HelloWorld.class);

or use an instance of HelloWorld:

    ClassType helloWorldType = TypeUtils.parseClassType(new SubClassOfHelloWorld());

The `helloWorldType` ClassType is a typical Node data structure which logically looks like:

    ClassType:some.path.to.HelloWorld
        ClassType:java.util.function.Function
	    ClassType:java.lang.Integer
	    ClassType:java.lang.Boolean
	ClassType:java.lang.Comparable
	    ClassType:java.lang.String
	  
Because `ClassType` implements comparable you can compare any node to any other and the compare process will iterate between all possible nodes checking for consistency. The possible values returned from said comparison are as follows:

    -1 : mismatch between any 2 nodes (i.e. java.lang.Integer does not match java.lang.Boolean)
     0 : all nodes match
     1 : when source has an unknown type (e.g. java.lang.Object) when comparing to target
     2 : when target has an unknown type (e.g. java.lang.Object) when comparing to source
     3 : when source or target both have unknown types when comparing to their counterparts
    
## Documentation

javadocs can be found via [github pages here](https://apis-and-processors.github.io/processor-tools/docs/javadoc/)

## Examples

The [various tests] provide many examples that you can use in your own code.

## Components

- guava \- used in helping to parse types from objects
    
## Testing

Running tests can be done like so:

    ./gradlew clean build
	
# Additional Resources

* [Guava](https://github.com/google/guava/wiki)
