# classtype-parser

Parse ClassTypes (e.g. types, generics, etc) from any given Object into a node/tree data structure.

## Status

| Name | Questions | CI | Codecov | Docs | Release |
| :--- | :---: | :---: | :---: | :---: | :---: |
| [classtype-parser](https://github.com/project-aries/classtype-parser) | [![Stack Overflow](https://img.shields.io/badge/stack-overflow-4183C4.svg)](https://stackoverflow.com/questions/tagged/classtype-parser) | [![Build Status](https://travis-ci.org/project-aries/classtype-parser.svg?branch=master)](https://travis-ci.org/project-aries/classtype-parser) | [![codecov](https://codecov.io/gh/project-aries/classtype-parser/branch/master/graph/badge.svg)](https://codecov.io/gh/project-aries/classtype-parser) | [![Docs](https://img.shields.io/badge/docs-latest-blue.svg)](http://htmlpreview.github.io/?https://github.com/project-aries/classtype-parser/blob/classtype-parser-gh-pages/docs/index.html) | [![Bintray](https://api.bintray.com/packages/project-aries/libs-release-local/classtype-parser/images/download.svg) ](https://bintray.com/project-aries/libs-release-local/classtype-parser/_latestVersion) |

## Latest release

Can be sourced from jcenter like so:

    <dependency>
      <groupId>com.aries</groupId>
      <artifactId>classtype-parser</artifactId>
      <version>0.0.1</version>
      <classifier>sources|tests|javadoc|all</classifier> (Optional)
    </dependency>
    
## Motivation

In designing the [api-processor](https://github.com/project-aries/api-processor) I needed a way to look at a given Object and resolve all of it's potential types and then compare those types to their runtime implementations passed in by the end user. While libraries like [guava](https://github.com/google/guava) and [typetools](https://github.com/jhalterman/typetools) do type-related work I required a different approach that had built in support for comparing potentially N number of types to some other class with N number of types in a node/tree structure.

Futhermore I needed a way to work with primitives in a type-esque related way and to be able to convert said primitives to their boxed types and then back again with ease.

## Setup and How to use

Consider the following class:

    abstract class HelloWorld implements Function<Integer, Boolean>, Comparable<String> {
        @Override
        public Boolean apply(final Integer instance) {
            return null;
        }
    }

It implements Function with types `Integer` and `Boolean` on top of implementing Comparable with type `String`. To convert this into a `ClassType` one can either use the class definition itself:

    ClassType helloWorldType = ClassType.parse(HelloWorld.class);

or use an instance of HelloWorld:

    ClassType helloWorldType = ClassType.parse(new SubClassOfHelloWorld());

The `helloWorldType` ClassType is a typical node/tree data structure which logically looks like:

    ClassType:some.path.to.HelloWorld -->
        ClassType:java.util.function.Function -->
	        ClassType:java.lang.Integer
	        ClassType:java.lang.Boolean
	    ClassType:java.lang.Comparable -->
	        ClassType:java.lang.String
		
## Examples

Parsing a regular class with a generic type?

    package my.hello.world
    class HelloWord <SomeUnknownType> { }

    ClassType classType = ClassType.parse(HelloWorld.class);
    assertThat(classType.toString()).equals("my.hello.world.HelloWorld<java.lang.Object>");
    
Parsing a regular class which extends a generic class?

    package my.hello.world
    class HelloWordParent <SomeUnknownType> { }
    
    package my.hello.world
    class HelloWord extends HelloWordParent<Integer> { }

    ClassType classType = ClassType.parse(HelloWorld.class);
    assertThat(classType.toString()).equals("my.hello.world.HelloWorld<my.hello.world.HelloWorldParent<java.lang.Integer>>");

Parsing a regular class with typed interface (supports multiple interfaces as well)?

    package my.hello.world
    class HelloWord implements Comparable<String> { }

    ClassType classType = ClassType.parse(HelloWorld.class);
    assertThat(classType.toString()).equals("my.hello.world.HelloWorld<java.lang.Comparable<java.lang.String>>");
    
Parsing a primitive?

    ClassType classType = ClassType.parse(123);
    assertThat(classType.toString()).equals("java.lang.Integer");
	  
## On Comparing ClassType's
Because `ClassType` implements comparable you can compare any node to any other and the compare process will iterate between all possible nodes checking for consistency. The possible values returned from said comparison are as follows:

    -1 : mismatch between any 2 nodes (i.e. java.lang.Integer does not match java.lang.Boolean) or wrong number of child nodes
     0 : all nodes match
     1 : when source has an unknown type (e.g. java.lang.Object) when comparing to target
     2 : when target has an unknown type (e.g. java.lang.Object) when comparing to source
     3 : when source or target both have unknown types when comparing to their counterparts
    
## Examples

The [various tests](https://github.com/project-aries/classtype-parser/tree/master/src/test/java/com/aries/classtype/parser) provide many examples that you can use in your own code.
    
## Testing

Running tests can be done like so:

    ./gradlew clean build
	
# Additional Resources

* Project was built using the model provided by [gradle-multi-project-example](https://github.com/project-aries/gradle-multi-project-example)
* [Guava](https://github.com/google/guava/wiki)
* [typetools](https://github.com/jhalterman/typetools)

