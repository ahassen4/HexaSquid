import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains static helper functions to help validate or generate
 * moves.
 */
public class Logic {
    /**
     * This function checks to see if the proposed move is valid in
     * the ruleset of hexasquid. Namely, it checks if the {@param from} board space
     * is white squid then it can only move by -1 in the y direction. If this
     * is straight ahead then no squid con be on the {@param to} board space.
     * If {@param to} is diagonal to {@param from} then a squid must be there and must
     * be of the opposite color. The same rules apply for black except that it must move
     * by +1 in the y direction.
     *
     * @param from Board space to move from
     * @param to   Board space to move to
     * @return true if this is a valid move given the above description, false otherwise
     */
    // TODO: Fill in this function using the above logic
    public static boolean validMove(BoardSpace from, BoardSpace to) {
        //is there a squid to move
        //what team
        //is this a diagonal move
        //is there an enemy on too?
        //is move valid
        //is this straight forward

        if (from.containsSquid()) {

            //Assignment of
            Squid fromSquid = from.getSquid();

            int fromRowY = from.getRow();
            int toRowY = to.getRow();
            int fromColX = from.getCol();
            int toColX = to.getCol();

            //Start for conditions for white squid
            if (fromSquid.getTeam().getColor().equals(Color.WHITE)) {
                if (!to.containsSquid())
                    if (fromRowY - toRowY == -1 && fromColX - toColX == 0) {

                        return true;
                    }
                if (fromRowY - toRowY == 1 && fromColX - toColX == 0) {
                    //row difference
                    if (!to.containsSquid()) {
                        return true;
                    } else {
                        return false;
                    }
                }
                //Start for conditions for black squid
            } else if (fromSquid.getTeam().getColor().equals(Color.BLACK)) {
                if ((fromRowY - toRowY == -1 && fromColX - toColX == 0) && !to.containsSquid()) {

                    return true;
                    //row difference

                }
                return fromRowY - toRowY == -1 && fromColX - toColX == 1 || fromRowY - toRowY == -1 &&
                        fromColX - toColX == -1 && to.containsSquid() &&
                        !fromSquid.getTeam().getColor().equals(Color.BLACK);


            }
        }


        return false;
    }


    /**
     * This function utilizes {@link #validMove(BoardSpace, BoardSpace)} to generate all valid moves for the given
     * {@param board} and {@param boardSpace}. It does this by first checking if the squid at the given {@param boardSpace}
     * can move forward. Then it checks if it can move in either diagonal direction. It then wraps up all the valid moves
     * into a list and returns them.
     *
     * @param board      Current state of the board
     * @param boardSpace Board space to make a move from
     * @return List of valid moves from the given board space given the current state of the board
     */
    // TODO: Fill in this function using the above logic
    public static List<Pair<BoardSpace, BoardSpace>> getValidMoves(List<List<BoardSpace>> board, BoardSpace boardSpace) {
        //Create a list of pairs of boardspaces to store my valid moves
        List<Pair<BoardSpace, BoardSpace>> storeValidMoves = new ArrayList<>();  //store into this list


        // Loop through the entire board except FOR from;
        //For every candidate space - leverage validmove

        for (List<BoardSpace> bList : board) {
            for (BoardSpace bSpace : bList) {
                if (validMove(boardSpace, bSpace)) {
                    Pair<BoardSpace, BoardSpace> lastStore = new Pair<>(boardSpace, bSpace);
                    storeValidMoves.add(lastStore);
                }
            }
        }


        return storeValidMoves;
    }
}
