# Hatchways Assessment
All source code with a full Git commit history can be found at https://github.com/sloanlipman/hatchways-assessment.

## Pre-requites
- Java 8

## Installation
### Windows
<code>./gradlew.bat build</code>

### MacOS/Linux
<code>./gradlew build</code>

Note: This project uses the Spotless plugin to ensure consistent code formatting. If any changes are made, you must run
<code>./gradlew.bat spotlessApply</code> (Windows) or <code>./gradlew spotlessApply</code> (MacOS/Linux) before building.

## Running
<code>./gradlew.bat bootRun</code>

### MacOS/Linux
<code>./gradlew bootRun</code>

The application will then run on port 8080.

Note: You can supply the optional <code>-xtest</code> argument to skip tests.

## Use
This application exposes two ReSTful APIs:
* <code>localhost:8080/api/ping</code>
* <code>localhost:8080/api/posts?tags=tech,science&sortBy=likes&direction=desc</code>
  * <code>tags</code> is a required parameter. It can be a single tag or a comma-separated list of tags.
  * <code>sortBy</code> is an optional parameter. It can take the value of id, reads, likes, or popularity. If unspecified, it defaults to id. Note: these values are case-sensitive.
  * <code>direction</code> is an optional parameter. It can take the value of asc or desc. If unspecified, it takes the default value of asc. Note: these values are case-sensitive.
