# Game of Amazons: Monte Carlo Agent

A Java game-playing agent for the Game of Amazons, a two-player strategy game played on a 10 × 10 board. The agent combines Monte Carlo Tree Search (MCTS) with a territory-and-mobility heuristic to choose moves within a fixed time budget.

The repository includes a complete move generator, immutable state transitions, heuristic board evaluation, a reusable search tree, an offline self-play runner, and an adapter for the original course game server.

## Technical highlights

- Generates every legal queen move and arrow placement from a board state.
- Uses Upper Confidence Bound (UCB) scoring to balance exploration and exploitation.
- Replaces expensive full-game simulations with a heuristic rollout for faster search.
- Evaluates queen reachability, king-distance territory, local mobility, and turn advantage.
- Reuses the selected subtree after each move instead of rebuilding the search from scratch.
- Supports both offline self-play and server-driven matches.

## Search pipeline

Each turn runs a time-boxed MCTS loop:

1. **Selection** follows the child with the highest UCB score until the search reaches a node with unexplored actions.
2. **Expansion** applies one legal action and creates a child state.
3. **Evaluation** scores the resulting position and maps the heuristic value to an estimated win probability with a sigmoid function.
4. **Backpropagation** updates visit and value totals while alternating perspective at each tree level.
5. **Decision** selects the root child with the strongest observed win rate.

This approach avoids a full random playout on every iteration, allowing more candidate positions to be evaluated during the move timer.

## Architecture

| Component | Responsibility |
| --- | --- |
| `MonteCarlo` | Runs MCTS, heuristic rollouts, backpropagation, and self-play |
| `TreeNode` | Stores search statistics, parent-child relationships, and unexplored actions |
| `AmazonsActionFactory` | Enumerates legal queen moves and arrow shots |
| `AmazonsAction` | Represents and applies a move without mutating the input state |
| `HeuristicEvaluator` | Estimates territory control and positional advantage |
| `AmazonsUtility` | Builds mobility maps, validates coordinates, and prints boards |
| `COSC322Test` | Connects the agent to the original SmartFox game server |

The board uses a compact integer representation: `0` for an empty square, `1` and `2` for the two players, and `3` for a blocked square.

## Repository layout

```text
.
├── README.md
└── team-00
    ├── pom.xml
    └── src/main/java/ubc/cosc322
        ├── AmazonsAction.java
        ├── AmazonsActionFactory.java
        ├── AmazonsUtility.java
        ├── COSC322Test.java
        ├── HeuristicEvaluator.java
        ├── MonteCarlo.java
        └── TreeNode.java
```

## Requirements

- JDK 8 or newer
- Maven 3.6 or newer for the full project build

## Build and run

From the repository root:

```bash
cd team-00
mvn clean compile
java -cp target/classes ubc.cosc322.MonteCarlo
```

The self-play runner prints each board, its heuristic value, the search depth, and the number of MCTS iterations completed for each move. A full match can take several minutes because both players receive a search budget on every turn.

### Run the offline engine without the server dependency

The server adapter depends on a course-hosted SmartFox client. If that package is unavailable, the standalone engine can still be compiled and run:

```bash
cd team-00
mkdir -p target/classes
find src/main/java/ubc/cosc322 -name '*.java' ! -name 'COSC322Test.java' -print0 \
  | xargs -0 javac -d target/classes
java -cp target/classes ubc.cosc322.MonteCarlo
```

## Verification

`EngineSmokeTest` checks state immutability, action generation, and heuristic score mapping without requiring the retired game server. Run the same checks used by the continuous-integration workflow with:

```bash
mkdir -p build/test-classes
find team-00/src/main/java/ubc/cosc322 -name '*.java' ! -name 'COSC322Test.java' -print0 \
  | xargs -0 javac -d build/test-classes
javac -cp build/test-classes -d build/test-classes \
  team-00/src/test/java/ubc/cosc322/EngineSmokeTest.java
java -cp build/test-classes ubc.cosc322.EngineSmokeTest
```

## Design notes

The heuristic changes emphasis as the board closes. Early positions rely more heavily on queen-distance territory, while later positions incorporate king-distance control and mobility. The search stores both the board and a mobility map so these evaluations do not need to reconstruct all local information from scratch.

Search values are always interpreted from the player-to-move perspective. During backpropagation, `1 - result` converts the value to the opposing player's perspective at the parent node.

## Current limitations

- The original course game server is no longer expected to be available, so `COSC322Test` is retained as an integration example.
- The self-play search budget is currently configured in source rather than through command-line options.
- The implementation favors readability and direct board arrays over compact bitboard optimizations.
