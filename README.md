# Game of Amazons

A Java game-playing agent for the Game of Amazons. The agent generates legal moves, evaluates board positions, and uses Monte Carlo Tree Search to select a move within a fixed time limit.

The project can run entirely offline in self-play mode. The original course server is not required.

## Tech stack

- **Java** — game engine and agent
- **Monte Carlo Tree Search** — move selection
- **Maven** — full project build and external client dependency
- **GitHub Actions** — automated core-engine checks

## Features

| Feature | Description |
| --- | --- |
| Legal move generation | Finds valid queen moves and arrow placements |
| Computer player | Searches possible moves and selects the strongest result found |
| Board evaluation | Scores territory and movement opportunities |
| Offline self-play | Runs two agents against each other without a server |
| Server adapter | Contains the integration code used by the original course server |
| Automated checks | Tests board updates, move generation, and score handling |

## Quick start

### Prerequisites

- JDK 8 or newer
- A terminal that supports `find` and `xargs` (macOS or Linux)

### Run without the server

From the repository root:

```bash
cd amazons-agent
mkdir -p target/classes
find src/main/java/ubc/cosc322 -name '*.java' ! -name 'COSC322Test.java' -print0 \
  | xargs -0 javac -d target/classes
java -cp target/classes ubc.cosc322.MonteCarlo
```

This command leaves out `COSC322Test.java`, which is the only part that needs the external game-server client. The rest of the engine compiles and runs locally.

The program will print:

- The current board
- Which player moves next
- The board evaluation
- The number of search iterations completed
- The updated board after each move

A complete self-play match can take several minutes. Press `Ctrl+C` to stop it early.

## Full Maven build

Maven can build the server adapter together with the offline engine:

```bash
cd amazons-agent
mvn clean compile
```

This build downloads the SmartFox course client from the package repository listed in `pom.xml`. If that package is unavailable, use the offline quick-start command instead.

## Test the engine

From the repository root:

```bash
mkdir -p build/test-classes
find amazons-agent/src/main/java/ubc/cosc322 -name '*.java' ! -name 'COSC322Test.java' -print0 \
  | xargs -0 javac -d build/test-classes
javac -cp build/test-classes -d build/test-classes \
  amazons-agent/src/test/java/ubc/cosc322/EngineSmokeTest.java
java -cp build/test-classes ubc.cosc322.EngineSmokeTest
```

Expected result:

```text
Engine smoke tests passed
```

## How it works

For every turn, the agent generates the available moves and searches through possible future board states. It uses repeated simulations and a board-scoring function to decide which move to play. The same process is repeated for both players during self-play.

## Main components

| File | Purpose |
| --- | --- |
| `MonteCarlo.java` | Runs the computer player and offline self-play |
| `TreeNode.java` | Stores positions explored during move selection |
| `AmazonsActionFactory.java` | Generates legal moves |
| `AmazonsAction.java` | Applies a move to the board |
| `HeuristicEvaluator.java` | Scores board positions |
| `AmazonsUtility.java` | Provides board and coordinate helpers |
| `COSC322Test.java` | Connects to the original course game server |
| `EngineSmokeTest.java` | Checks the main offline engine behavior |

## Project structure

```text
amazons-agent/
├── pom.xml
└── src/
    ├── main/java/ubc/cosc322/   Game engine and agent
    └── test/java/ubc/cosc322/   Engine checks
```
