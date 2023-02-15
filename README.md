# DBTester

A JUnit-Extension to automatically set up databases that can be used for during the test. 

## Features
- Start and stop an H2-Database automatically
- Compare the content of the database with a desired dataset
- Clean up of the database after each test

## How to use
- To include DBTester your test need to extend the `DBTestCase` Class.  
```java
public class ExampleTest extends DBTestCase {
````
- Tables, Sequences, etc. can be initialised with the `DBTestCase.setPathToDDLs` method.  
  This method requires a `java.nio.file.Paths` to the location of the file with the DDL statements. 
```java
@BeforeAll
static void setUp() throws IOException {
    DBTestCase.setPathToDDLs(Paths.get("./src/test/ddl.sql"));
}
```
- If you want to populate the database with some initial data, the method `setInitialDataset` can be used.
```java
this.setInitialDataset(Paths.get("./src/test/dataset1.md"));
```
- To compare the content of the database with the desired output `assertEqualDataset` can be used.
```java
this.assertEqualDataset(Paths.get("./src/test/dataset2.md"));
```
- The datasets can be written as markdown tables. For this, the name of the table must be written as a heading followed by the table itself.

## Planned functions
- [ ] Configurable clean-up methods.   
  It should be possible to set whether the entire content, only part of it or nothing at all should be deleted after each test.
- [ ] Support of other dataset formats  
  For example, XML to support datasets from tools like dbunit.
- [ ] Support of different internal databases.  
  It should be possible to combine this library with a tool like [Testcontainers](https://github.com/testcontainers/testcontainers-java) to set up databases other than an H2 Database