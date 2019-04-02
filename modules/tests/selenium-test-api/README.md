# Liferay Selenium Test API

## Overview

Writing functional selenium tests are a great way to ensure that the end user is
having the intended experience, but it can be difficult to set up and manage
these tests.

This module provides JUnit extensions that integrate with Selenium to make
writing and maintaining functional tests less onerous. To make this possible,
JUnit test runners, test rules, and some special annotations have been created.

## Selenium Properties

To make configuring the test environment easier, a properties file can be used.
This can be specified as a resource file called 'selenium.properties', or
loaded from another location by setting the SystemProperty
"selenium.properties.file.path". see **SeleniumPropertyKeys** for supported
properties

## Test Extensions

### Java Test Case

The simplest way to get a functional test up and running is to extend
**SeleniumJavaTestCase**. This will create a default web driver based on the
selenium properties by using the *setUp* and *tearDown* methods, and make it
available as a protected field. The default browser can be overridden in the
test class by implementing the *getBrowser* method. The same
**WebDriverManager** will be used for all sub-classes, but a new web driver will
 be set before every test.

### JUnit with WebDriverTestRule

JUnit provides the capability to modify tests with test rules. These test rules
can apply to methods (@Rule) or classes (@ClassRule). Test rules may modify the
success/failure of a test, or simply report information about the test
execution.

The **WebDriverTestRule** makes a selenium web driver available to the test
class, as well as executes the test for each browser specified.

### Liferay Selenium Runners

The JUnit Runner extensions **LiferaySeleniumTestRunner** and
**LiferaySeleniumSuiteRunner** extend build in JUnit runners but add in the
ability to specify the browsers to use for the tests.

The **@Browsers** annotation is used to specify which browsers should be used
for the test class, and the **@WebDriverField** annotation marks a field to
 inject the web driver before test execution. The Liferay Selenium runners
 process these annotations, updating the driver and creating test suites for
 each browser.

## Test Rules

- **ScreenShotTestRule**: test watcher that does not modify the execution of the
 test but takes a screen shot on any/all of the following test states: start,
  fail, succeed, finish.
- **SimpleRuleChain**: the JUnit RuleChain uses a builder pattern, and executes
the rules in reverse order. this rule executes other rules iteratively, from
first to last, which is typically easier to understand.
- **WebDriverTestRule**: executes the test statements for each browser and
provides a web driver based on that browser.

## Additional Info

### Thread Locals

two thread locals are set in the Liferay Selenium Runners so that the web driver
and web driver manager can be re-used: **WebDriverThreadLocal** and
**WebDriverManagerThreadLocal**.

### XPath

The **XPathStringBuilder** provides some xpath string methods for convenience.