# Setup used

- Java 21 - temurin
- Gradle Kotlin

# Instructions to run

- In the project root folder execute the following commands:

````shell
./gradlew clean build bootRun
````

When the server is up and running execute the following in your terminal if you have curl installed.

````shell
curl http://localhost:8080/v1/joke
````

If you don't have `curl` installed navigate to the file `src/test/java/com/example/nsassessment/requests/GetJoke.http`
and press the play button on the left of line 1.

# Considerations for the reviewers

- The api requested to be used has a hardcoded limit of 10 jokes per request even when an amount greater than 10 is set
- If any Joke comes from the external api without any of the following flags: [racist, sexist, explicit]
    - I decided to consider those flags true in order to prevent uncategorized Jokes to be returned
- If two jokes are valid to be returned and both of them have the same length the one that appears first in the list
  will be returned
