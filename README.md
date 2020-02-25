# Sparse Fetch Demo
What's the fastest, least memory-consuming, easy to code/maintain approach to querying large database tables?

I list the approaches I took, run test through a REST API after Spring Boot starts and quick warmup is executed to prime 
the JVM, and list the average results.

TL;DR:  JPA query -> `List<Map<String, Object>>` -> New sparse object instances from a Byte Buddy generated class.

## Problem
At work, I have to work with a few database tables that are massive, containing hundreds of fields.  I'd love
to break them up into multiple tables, but can't because of our other internal applications and custom reports that 
depend on these tables.  

I WILL NEVER CONSIDER ENABLING SECOND LEVEL HIBERNATE CACHING.  MYSQL DATABASE IS ALREADY FAST ENOUGH.  I'LL CHOOSE 
WHEN THE TIME COMES TO DO CACHING IF I ABSOLUTELY MUST.

I have to query these tables a lot while writing code.  If I go with the 
[Projections](https://www.baeldung.com/spring-data-jpa-projections) approach, then I'm going to have at least 20 class 
files for one table I would be maintaining, which I don't want to do.  

I'd really like to use JPQL and fetch the full object, but is the memory consumed for all of those fields I don't need t
hat bad?  Is it much slower than fetching a few fields?  

I guess I could live with fetching a `List<Map<String, Object>>`, but it's going to be rough working with
a map vs. POJO.  I'd have to keep typecasting when fetching the key's value.  Also, I'd need to store the results into
my own `Map` implementation that throws an exception on a key not found, because this means I didn't fetch enough 
field(s) in my query.

While I'm asking these questions, why not find out which is quicker and uses less memory:  JPA or Native Query?

## Environment
VirtualBox: Ubuntu 18, MySQL 8

Spring Boot:  Spring Data and Spring REST

Java 11.

## Data
I can't use work data and needed large sample data (large record and field count).  Found a good one from E for Excel.  
Generated 5,000 record Human Resources CSV file with 37 fields.  See File Setup for more info.

## Code
To run tests, I use a REST client to invoke the tests.  For all tests, I'm fetching all 5,000 records.  For sparse 
fetches, I'm fetching 5 fields:  2 Integers, 2 Strings, and a Date (LocalDate).  I use Spring's builtin StopWatch class 
to report time and Runtime's totalMemory() - freeMemory() to get memory used. 

## JPA vs Native Query
I'm using Spring Data so I have the option to use either JPA or Native Query.  I like both, but usually prefer Native 
Query.  I guess Native Query will be faster.

### Results
Endpoint | Average Time | Average Memory Used
-------- | ------------ | -----------
/findJpaMap | 40 ms | 6 MB
/findNativeMap | 50 ms | 10 MB

Average times were very good, but I was surprised by the memory consumption for the Native Query.  JPA all the way.

## Return type List<Map<String, Object>> or Object[][] ?
Curious if using a more basic multidimensional array would cut down on memory usage

### Results
Endpoint | Average Time | Average Memory Used
-------- | ------------ | -----------
/findJpaMap | 40 ms | 6 MB
/findJpaMultiArray | 65 ms | 11 MB

I'm guessing by default `List<Map<String, Object>>` is returned and then converted to `Object[][]`.  Sticking with
`List<Map<String, Object>>`

## Can We Get Away With Full Object Fetch?
Now that we have some good base numbers, will doing a full object fetch be so bad?

### Results
Endpoint | Average Time | Average Memory Used
-------- | ------------ | -----------
/findJpaMap | 40 ms | 6 MB
/findAll | 220 ms | 34 MB

Ouch.  Just before I throw out full object fetching, I need to make sure getting values from the Map won't be so bad. 
I'm going to now loop 5 times through each record, get the values from the Map, and write values in a StringBuilder.  
Also, this will use the SparseMap, which checks if the key exists otherwise throws an exception that we didn't fetch
that field from the database.

### Results
Endpoint | Average Time | Average Memory Used
-------- | ------------ | -----------
/findAndAccessSparseJpa | 65 ms | 14.2 MB
/findAndAccessAll | 230 ms | 36.6 MB

Full object fetching is too expensive.

## Anything Else I Can Do?
I would be fine settling with sparse JPA fetches that return as a List of Maps.  The Map guards me from accidentally 
getting a field that wasn't fetched from the database.  While debugging, only the key/values that were fetched are 
visible, which is so much better than seeing a bunch nulls in a sparsely fetched POJO. The only real drawback is I have 
to work with a Map. I have to know ahead of time the type of the field, so my code is going to be filled with typecasting.

I really would like to use Projections (multiple POJOs composed of a subset of fields), but I don't want to have 20+ 
class files for the same table.  I don't care if I have 20+ classes loaded in the JVM because there are already tens of
thousands of classes in our runtime, I don't want all of those class files in my source code.

It's like I want to use something like Mockito in production on an interface that will return the field value on a 
getter call and throw an exception on a getter call if that field was not fetched.

## Byte Buddy
From [Byte Buddy](https://bytebuddy.net):
> Byte Buddy is a code generation and manipulation library for creating and modifying Java classes during the runtime of
> a Java application and without the help of a compiler. Other than the code generation utilities that ship with the 
> Java Class Library, Byte Buddy allows the creation of arbitrary classes and is not limited to implementing interfaces 
> for the creation of runtime proxies. Furthermore, Byte Buddy offers a convenient API for changing classes either 
> manually, using a Java agent or during a build.

Mockito uses Byte Buddy, so I can pull off what I just talked about.  It's already available as a library since
Hibernate uses it.

`org.mattrr78.sparsefetchdemo.EmployeeService.initializeSparseClass()` calls 
`org.mattrr78.sparsefetchdemo.SparseInstanceRepository.addConstructor()` to create a sparse class.  A constructor of 
this sparse class is then stored into a Map in `SparseInstanceRepository`.  Sparse instances can then be generated from 
`org.mattrr78.sparsefetchdemo.SparseInstanceRepository.createInstance()`.

### Results
Endpoint | Average Time | Average Memory Used
-------- | ------------ | -----------
/findAndAccessSparseJpa | 65 ms | 14.2 MB
**/findAndAccessSparseInstances** | **50 ms** | **9.5 MB**

## Conclusion
Using a class generated by ByteBuddy to take Map result of a JPA query and store into new sparse instances seems like
the best solution.  It's fast, low memory overhead, does not have to rely on working with maps after fetch (no typecasting required),
debugging this sparse instance only shows the fields that were fetched from the database, and I get an exception thrown
if I attempt to call a getter on a sparse instance that was not fetched.

## Misc
### File Setup

HR Data generated from http://eforexcel.com/wp/downloads-16-sample-csv-files-data-sets-for-testing/

After downloading the CSV file, I needed to tidy up the data a bit to get it to import into MySQL.  So I wrote 
`org.mattrr78.sparsefetchdemo.EmployeeFileConverter`.  You don't have to run this, because the resulting file is found in 
converted_5000_HR_Records.zip

### Database Setup

MySQL 8 Commands:

```
CREATE DATABASE hrdb;

CREATE USER hrdb IDENTIFIED BY 'hrdb';

GRANT ALL ON hrdb.* TO hrdb;

CREATE TABLE Employee
(
	id int not null,
	namePrefix varchar(6) null,
	firstName varchar(60) null,
	middleInitial char null,
	lastName varchar(60) null,
	gender char null,
	email varchar(100) null,
	fatherName varchar(120) null,
	motherFullName varchar(120) null,
	motherMaidenName varchar(60) null,
	dateOfBirth date null,
	timeOfBirth time null,
	age int null,
	weight int null,
	joinDate date null,
	joinQuarter char(2) null,
	joinHalf char(2) null,
	joinYear int null,
	joinMonth int null,
	joinMonthName varchar(12) null,
	joinMonthNameShort char(3) null,
	joinDay int null,
	joinWeek varchar(12) null,
	joinWeekShort char(3) null,
	companyYears int null,
	salary int null,
	percentHike int null,
	socialSecurityNumber varchar(12) null,
	phone varchar(20) null,
	locationName varchar(30) null,
	locationCounty varchar(30) null,
	locationCity varchar(30) null,
	locationState char(2) null,
	locationZip varchar(10) null,
	region varchar(10) null,
	username varchar(20) null,
	password varchar(30) null,
	constraint Employee_pk
		primary key (id)
);
```

Then load converted_5000_HR_Records.csv file contents into Employee table.