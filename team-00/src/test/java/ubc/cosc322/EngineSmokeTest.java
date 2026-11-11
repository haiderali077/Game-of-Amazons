package ubc.cosc322;

import java.util.ArrayList;

public final class EngineSmokeTest {
    private EngineSmokeTest() {
    }

    public static void main(String[] args) {
        appliesActionsWithoutMutatingTheInput();
        generatesLegalActionsForTheCurrentPlayer();
        keepsSigmoidResultsInProbabilityRange();
        System.out.println("Engine smoke tests passed");
    }

    private static void appliesActionsWithoutMutatingTheInput() {
        int[][] board = emptyBoard();
        board[6][0] = 1;
        int[][][] state = new int[][][] {board, AmazonsUtility.getMobilityMap(board)};

        AmazonsAction move = new AmazonsAction(0, 6, 0, 5, 0, 4);
        int[][][] updated = AmazonsAction.applyAction(move, state);

        check(state[0][6][0] == 1, "The source state must remain unchanged");
        check(state[0][5][0] == 0, "The source state must not receive the moved queen");
        check(updated[0][6][0] == 0, "The original square must be empty after a move");
        check(updated[0][5][0] == 1, "The queen must occupy its destination");
        check(updated[0][4][0] == 3, "The arrow must block its destination");
    }

    private static void generatesLegalActionsForTheCurrentPlayer() {
        int[][] board = blockedBoard();
        board[0][0] = 1;
        board[0][1] = 0;
        board[0][2] = 0;
        board[1][0] = 0;
        board[1][1] = 0;

        int[][][] state = new int[][][] {board, AmazonsUtility.getMobilityMap(board)};
        ArrayList<AmazonsAction> actions = AmazonsActionFactory.getActions(state, 1);

        check(!actions.isEmpty(), "A mobile queen must have legal actions");
        for (AmazonsAction action : actions) {
            check(action.queenSrcX == 0 && action.queenSrcY == 0,
                    "Every action must move the current player's queen");
            check(board[action.queenDestY][action.queenDestX] == 0,
                    "Every queen destination must be empty");
        }
    }

    private static void keepsSigmoidResultsInProbabilityRange() {
        double negative = AmazonsUtility.sigmoid(-100);
        double neutral = AmazonsUtility.sigmoid(0);
        double positive = AmazonsUtility.sigmoid(100);

        check(negative > 0 && negative < neutral, "Negative scores must map below 0.5");
        check(neutral == 0.5, "A neutral score must map to 0.5");
        check(positive > neutral && positive < 1, "Positive scores must map above 0.5");
    }

    private static int[][] emptyBoard() {
        return new int[10][10];
    }

    private static int[][] blockedBoard() {
        int[][] board = new int[10][10];
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                board[row][column] = 3;
            }
        }
        return board;
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
