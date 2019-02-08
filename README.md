This is a CLI application that calls Github's API.

The code requires JDK 11 and Gradle in order to build it and run it.

You will need a 5.x version of Gradle. 4.x have issues with JDK 11. I use homebrew to install Gradle.

First run the tests with:

```
gradle test
```

The application supports the following arguments
```
-weeks <number> -sort <asc|desc> <repository name>
```

Only the repository name is required. 52 weeks (one year) and descending sort are the default values for the optional arguments.

To run the application you can try:
```
gradle run --args='-weeks 2 -sort asc kubernetes/kubernetes'
```

NOTE: Github has a rate limiting, if you increase the number of weeks too high,
you will go over the limit of 50 requests per hour. The application will print a message
if you need to wait.

The main classes for this application are:

com.github.codechapin.App <br />
com.github.codechapin.CommitsService <br />
com.github.codechapin.CommitsParser <br />
com.github.codechapin.utils.DaysCounter <br />

DaysCounter is in utils package because it can be used in any application.




