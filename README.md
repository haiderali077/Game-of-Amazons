# Game of Amazons

A Java agent that plays the Game of Amazons using Monte Carlo Tree Search. It evaluates possible moves, chooses a strong response within a set time limit, and can play complete games against itself.

## Features

- Generates legal queen moves and arrow placements
- Uses Monte Carlo Tree Search to choose moves
- Scores board positions using territory and mobility
- Includes an offline self-play mode
- Includes smoke tests and automated GitHub checks
- Can connect to the original course game server through a separate adapter

## Project structure

```text
amazons-agent/
├── pom.xml
└── src/
    ├── main/java/ubc/cosc322/   Game engine and agent
    └── test/java/ubc/cosc322/   Smoke tests
```

## Requirements

- JDK 8 or newer
- Maven 3.6 or newer for the full build

## Run the offline agent

From the repository root:

```bash
cd amazons-agent
mkdir -p target/classes
find src/main/java/ubc/cosc322 -name '*.java' ! -name 'COSC322Test.java' -print0 \
  | xargs -0 javac -d target/classes
java -cp target/classes ubc.cosc322.MonteCarlo
```

The program prints each board state as the two agents play against each other.

## Maven build

```bash
cd amazons-agent
mvn clean compile
```

The full Maven build includes the course game-server adapter and its external dependency. The offline agent does not need that dependency.

## Run the tests

```bash
mkdir -p build/test-classes
find amazons-agent/src/main/java/ubc/cosc322 -name '*.java' ! -name 'COSC322Test.java' -print0 \
  | xargs -0 javac -d build/test-classes
javac -cp build/test-classes -d build/test-classes \
  amazons-agent/src/test/java/ubc/cosc322/EngineSmokeTest.java
java -cp build/test-classes ubc.cosc322.EngineSmokeTest
```
