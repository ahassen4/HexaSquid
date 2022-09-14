import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The Hexasquid Educational Robot or HER is trained by
 * the user to play Hexasquid better and better over time.
 * It learns by being punished for its mistakes. This is achieved
 * through the use of a Map which keeps track of HER's available moves
 * for a given board state. If HER makes a bad move then that move is removed
 * from the moves available for that board state. Thus, HER will not make the same
 * mistake twice.
 */
public class HER {
    /**
     * This class represents a list of available moves.
     * These moves are generated using {@link Logic#getValidMoves(List, BoardSpace)}.
     */
    private static class Moves {
        private final List<Pair<BoardSpace, BoardSpace>> moves;

        private Moves(List<List<BoardSpace>> board) {
            moves = new ArrayList<>();

            for (List<BoardSpace> row : board) {
                for (BoardSpace boardSpace : row) {
                    if (boardSpace.containsSquid() && boardSpace.getSquid().getTeam() == Squid.Team.BLACK) {
                        moves.addAll(Logic.getValidMoves(board, boardSpace));
                    }
                }
            }
        }

        private Pair<BoardSpace, BoardSpace> generateMove() {
            return moves.get(ThreadLocalRandom.current().nextInt(moves.size()));
        }

        private void removeMove(Pair<BoardSpace, BoardSpace> move) {
            moves.remove(move);
        }
    }
    // This represents the matchboxes that you would use to train HER in real life
    private final Map<List<List<BoardSpace>>, Moves> matchboxes;

    public HER() {
        matchboxes = new HashMap<>();
    }

    /**
     * This function simply checks to see whether it has seen the given
     * board. If it has then it randomly selects one of those moves.
     * If it hasn't seen the given board then it creates a fresh set of moves
     * for that board and chooses one of them.
     * @param board Board to generate a move for
     * @return The move generated for the given board
     */
    public Pair<BoardSpace, BoardSpace> generateMove(List<List<BoardSpace>> board) {
        if (matchboxes.containsKey(board)) {
            return matchboxes.get(board).generateMove();
        }
        else {
            Moves moves = new Moves(board);
            matchboxes.put(board, moves);
            return moves.generateMove();
        }
    }

    /**
     * This function punishes HER for a bad move
     * @param board Board where HER made a mistake
     * @param move Move that HER made by mistake
     */
    public void removeBadMove(List<List<BoardSpace>> board, Pair<BoardSpace, BoardSpace> move) {
        matchboxes.get(board).removeMove(move);
    }
}
