import javafx.animation.AnimationTimer;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This is the meat of this codebase.
 * This class represents the game board.
 * This means it keeps track of the visual JavaFX objects
 * as well as the logical objects used to represent the game.
 * It allows the user to reset the state of the board as well
 * as set it to run in "auto" mode where it keeps playing games
 * automatically forever. If the user click the auto button again
 * it stops running automatically and allows the user to pick up
 * where it left off.
 */
public class Board {
    // Many variables to keep track of
    // Gridpane to draw in
    private final GridPane gridPane;
    // Size of the squares in the grid
    private final double spaceSize;
    // The hexasquid education robot
    private final HER her;
    // Companion to the gridpane used to lookup specific boardspaces
    private List<List<BoardSpace>> board;
    // Keeps track of where the user wants to move from
    private BoardSpace from;
    // Keeps track of the last state of the board which is used to punish the computer
    private List<List<BoardSpace>> lastBoard;
    // Keeps track of the last move of the computer which is used to punish the computer
    private Pair<BoardSpace, BoardSpace> lastMove;
    // Is the board in a winning state?
    private boolean hasWinner = false;
    // Is the board in auto mode?
    private boolean autoMode = false;
    // Timer used to animate auto mode
    private AnimationTimer timer;

    public Board(GridPane gridPane, double spaceSize) {
        this.gridPane = gridPane;
        this.spaceSize = spaceSize;
        her = new HER();
        board = constructBoard();
    }

    public List<List<BoardSpace>> getBoard() {
        return board;
    }

    // START STUDENT SECTION

    /**
     * This function tests one of the win conditions namely did a
     * squid make it all the way across the board.
     * Basically it checks if a white squid is in the 0th row or
     * if a black squid is in the second row. If either one is
     * true then this function returns true.
     *
     * @return true if a squid made it across the board, false otherwise
     */
    // TODO: Fill in this function using the above logic
    private boolean squidMadeItAcross() {

        // Loop through the board: use a for foreach loop
        // search row 0, search row 2



        for (int i = 0; i < 3; i += 2) {
            List<BoardSpace> interRow = board.get(i);
            for (BoardSpace crossSpace : interRow) {
                if (crossSpace.containsSquid() && i == 0 && crossSpace.getSquid().getTeam() == Squid.Team.WHITE) {
                    return true;
                }
                if (crossSpace.containsSquid() && i == 2 && crossSpace.getSquid().getTeam() == Squid.Team.BLACK) {
                    return true;
                }
            }
        }




        return false;
    }

    /**
     * This function tests another one of the win conditions namely did
     * one of the teams get eliminated. It accomplishes this tasks
     * by checking to see if either team has no squids left.
     *
     * @return true either team has no squids left, false otherwise
     */
    // TODO: Fill in this function using the above logic
    private boolean teamEliminated() {
        //loop through board
        // if one contain squid return false
        //if boardspace.getsquid == to team the continue
        // not equal logic.getvalidmoves.empty\
        int whiteIndex = 0;
        int blackIndex = 0;
        for (int i = 0; i < 3; i += 2) {
            List<BoardSpace> myboardRow = board.get(i);
            for (BoardSpace theSpace : myboardRow) {
                if (theSpace.containsSquid() && theSpace.getSquid().getTeam() == Squid.Team.WHITE) {
                    whiteIndex++;
                }
                if (theSpace.containsSquid() && theSpace.getSquid().getTeam() == Squid.Team.BLACK) {
                    blackIndex++;
                }
            }
        }
        if (blackIndex == 0) {
            return true;
        }
        return whiteIndex == 0;




    }

    /**
     * This function tests the third and final win condition namely
     * is the given team out of moves. It does this by looking at each
     * squid of the given team on the board and using {@link Logic#getValidMoves(List, BoardSpace)}.
     *
     * @param team Team to check valid moves for
     * @return true if no squid on the given team has any valid moves, false otherwise
     */
    // TODO: Fill in this function using the above logic
    private boolean noMoves(Squid.Team team) {

        //loop through the board
        //finding boardspaces with squids of the target team
        //feed the boardspace into logic getValidMoves()

//        List<Pair<BoardSpace, BoardSpace>> groupValidMoves = new ArrayList<>();

        for (List<BoardSpace> bSpaceX : board){
            for (BoardSpace theSpace : bSpaceX) {
                if (theSpace.containsSquid() && !Logic.getValidMoves(board, theSpace).isEmpty()) {
                    return false;
                }
            }
        }


        return true; //In case I cannot find any moves
    }

    // END STUDENT SECTION

    /**
     * Utilizes the three above methods to check whether the board is in a winning state
     *
     * @return true if any win condition is met, false otherwise
     */
    private boolean checkWin() {
        return squidMadeItAcross() || teamEliminated() || noMoves(Squid.Team.WHITE) || noMoves(Squid.Team.BLACK);
    }

    /**
     * Bread and butter function to move a squid from a board space
     * to another board space.
     *
     * @param from Board space to move from
     * @param to   Board space to move to
     */
    private void makeMove(BoardSpace from, BoardSpace to) {
        BoardSpace realFrom;
        BoardSpace realTo;
        if (from.getPicture() == null && to.getPicture() == null) {
            realFrom = board.get(from.getRow()).get(from.getCol());
            realTo = board.get(to.getRow()).get(to.getCol());
        } else {
            realFrom = from;
            realTo = to;
        }
        Squid fromSquid = realFrom.getSquid();
        realFrom.setSquid(null);
        realTo.setSquid(fromSquid);
    }

    /**
     * Creates a semi shallow copy of the board which is used as a key in HER's map.
     *
     * @return Semi shallow copy of the board
     */
    private List<List<BoardSpace>> semiShallowCopyBoard() {
        List<List<BoardSpace>> newBoard = new ArrayList<>();
        for (List<BoardSpace> row : board) {
            List<BoardSpace> newRow = new ArrayList<>();
            for (BoardSpace space : row) {
                newRow.add(new BoardSpace(space.getSquid(), space));
            }
            newBoard.add(newRow);
        }
        return newBoard;
    }

    /**
     * This function utilizes HER to make a move for the computer
     */
    private void makeComputerMove() {
        lastBoard = semiShallowCopyBoard();
        Pair<BoardSpace, BoardSpace> computerMove = her.generateMove(lastBoard);
        makeMove(computerMove.getKey(), computerMove.getValue());
        lastMove = computerMove;

        if (checkWin()) {
            System.out.println("Black Wins!");
            hasWinner = true;
            if (autoMode) {
                reset();
            }
        }
    }

    /**
     * Constructs the inital state of the board.
     * This includes creating the onclick event for each board space.
     * This is created in here in order to be closed over the board space
     * which need to be used in the on click event.
     *
     * @return List companion representation to the JavaFX grid
     */
    private List<List<BoardSpace>> constructBoard() {
        List<List<BoardSpace>> board = new ArrayList<>();
        // Used to alternate colors on the board
        int count = 0;

        for (int i = 0; i < 3; i++) {
            List<BoardSpace> row = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                Color spaceColor = count % 2 == 0 ? Color.web("#DE5221") : Color.web("#5221DE");
                Rectangle rect = new Rectangle(spaceSize, spaceSize, spaceColor);
                BoardSpace boardSpace;

                if (i != 1) {
                    Squid.Team team = i == 0 ? Squid.Team.BLACK : Squid.Team.WHITE;
                    Color pieceColor = team.getColor();
                    Squid squid;

                    if (j == 0) {
                        Circle circle = new Circle(spaceSize / 4, pieceColor);
                        squid = new Squid(team, Squid.ShapeType.CIRCLE, circle);
                    } else if (j == 1) {
                        double halfSpaceSize = spaceSize / 2;
                        Polygon triangle = Triangle.equilateralTriangleCenteredOn(halfSpaceSize, halfSpaceSize, spaceSize * 0.75);
                        triangle.setFill(pieceColor);
                        squid = new Squid(team, Squid.ShapeType.TRIANGLE, triangle);
                    } else {
                        double halfSpaceSize = spaceSize / 2;
                        Rectangle rectanglePiece = new Rectangle(halfSpaceSize, halfSpaceSize, pieceColor);
                        squid = new Squid(team, Squid.ShapeType.SQUARE, rectanglePiece);
                    }

                    boardSpace = new BoardSpace(rect, i, j, squid);
                } else {
                    boardSpace = new BoardSpace(rect, i, j);
                }

                // Logic behind the interaction with the user
                boardSpace.getPicture().setOnMouseClicked(event -> {
                    // Don't let the user click if the board is in a winning state
                    if (!hasWinner) {
                        // Don't let the user click if the board is in auto mode
                        if (!autoMode) {
                            // If from is null that means this is the first square they are clicking
                            if (from == null && boardSpace.containsSquid()) {
                                // Thus save this space
                                from = boardSpace;
                                // And highlight it
                                from.toggleHighlight();
                            }
                            // Otherwise, this is the second space they are clicking
                            else if (from != null) {
                                // Thus check if this is a valid move
                                if (Logic.validMove(from, boardSpace)) {
                                    // If so make the move
                                    makeMove(from, boardSpace);
                                    // Check if the user made a winning move
                                    if (checkWin()) {
                                        // If so let em know!
                                        System.out.println("White Wins!");
                                        // Set the board in a winning state
                                        hasWinner = true;
                                        // Punish the computer!
                                        her.removeBadMove(lastBoard, lastMove);
                                    }
                                    // If the user hasn't won yet let the computer move
                                    else {
                                        makeComputerMove();
                                    }
                                }
                                // Now that the user has completed their move remove the highlighting
                                from.toggleHighlight();
                                // And make from null again so the cycle can continue
                                from = null;
                            }
                        }
                    }
                });
                // Finish up making the board list
                row.add(boardSpace);
                // And adding the space to JavaFX
                gridPane.add(boardSpace.getPicture(), j, i);
                count++;
            }
            // Add the completed row to the board list
            board.add(row);
        }

        return board;
    }

    /**
     * Runs the board in auto mode. This entails picking random moves
     * for the white player and calculated moves for black. This speeds
     * up the learning process tremendously.
     */
    public void runAuto() {
        if (!autoMode) {
            reset();
            autoMode = true;
            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    List<BoardSpace> whiteSpaces = new ArrayList<>();
                    for (List<BoardSpace> boardRow : board) {
                        for (BoardSpace space : boardRow) {
                            if (space.containsSquid() && space.getSquid().getTeam() == Squid.Team.WHITE) {
                                whiteSpaces.add(space);
                            }
                        }
                    }

                    Collections.shuffle(whiteSpaces);
                    for (BoardSpace whiteSpace : whiteSpaces) {
                        List<Pair<BoardSpace, BoardSpace>> randomMoves = Logic.getValidMoves(board, whiteSpace);
                        if (randomMoves.size() != 0) {
                            Pair<BoardSpace, BoardSpace> randomMove =
                                    randomMoves.get(ThreadLocalRandom.current().nextInt(randomMoves.size()));
                            makeMove(randomMove.getKey(), randomMove.getValue());
                            break;
                        }
                    }

                    if (checkWin()) {
                        System.out.println("White Wins!");
                        hasWinner = true;
                        her.removeBadMove(lastBoard, lastMove);
                        reset();
                    } else {
                        makeComputerMove();
                    }
                }
            };
            timer.start();
        } else {
            timer.stop();
            autoMode = false;
        }
    }

    /**
     * Resets the board to its initial state.
     */
    public void reset() {
        gridPane.getChildren().clear();
        board = constructBoard();
        from = null;
        lastBoard = null;
        lastMove = null;
        hasWinner = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board1 = (Board) o;
        return board.equals(board1.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board);
    }

    @Override
    public String toString() {
        return "Board{" +
                "gridPane=" + gridPane +
                ", spaceSize=" + spaceSize +
                ", board=" + board +
                '}';
    }
}

