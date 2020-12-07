/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.progmatic.labyrinthproject.Coordinate;
import com.progmatic.labyrinthproject.LabyrinthImpl;
import com.progmatic.labyrinthproject.RandomPlayer;
import com.progmatic.labyrinthproject.WallFollowerPlayer;
import com.progmatic.labyrinthproject.enums.CellType;
import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.exceptions.CellException;
import com.progmatic.labyrinthproject.exceptions.InvalidMoveException;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;
import com.progmatic.labyrinthproject.interfaces.Player;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author pappgergely
 */
public class LabyrinthTest {

    public LabyrinthTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSetSizeAndGetWidthHeight() {
        Labyrinth l = getLabyrinthImpl();
        assertEquals(-1, l.getWidth());
        assertEquals(-1, l.getHeight());
        l.setSize(5, 5);
        assertEquals(5, l.getWidth());
        assertEquals(5, l.getHeight());
        l.setSize(10, 10);
        assertEquals(10, l.getWidth());
        assertEquals(10, l.getHeight());
        l.setSize(20, 30);
        assertEquals(20, l.getWidth());
        assertEquals(30, l.getHeight());
    }

    @Test
    public void testGetCellType() throws Exception {
        String[][] lArr = {
                {"#", "#", "#"},
                {"S", " ", "E"},
                {"#", "#", "#"}};
        Labyrinth l = fromString(lArr);
        assertEquals(CellType.WALL, l.getCellType(new Coordinate(0, 0)));
        assertEquals(CellType.WALL, l.getCellType(new Coordinate(1, 0)));
        assertEquals(CellType.WALL, l.getCellType(new Coordinate(2, 0)));
        assertEquals(CellType.START, l.getCellType(new Coordinate(0, 1)));
        assertEquals(CellType.EMPTY, l.getCellType(new Coordinate(1, 1)));
        assertEquals(CellType.END, l.getCellType(new Coordinate(2, 1)));
        assertEquals(CellType.WALL, l.getCellType(new Coordinate(0, 2)));
        assertEquals(CellType.WALL, l.getCellType(new Coordinate(1, 2)));
        assertEquals(CellType.WALL, l.getCellType(new Coordinate(2, 2)));
    }

    @Test(expected = CellException.class)
    public void testGetCellTypeException1() throws Exception {
        String[][] lArr = {
                {"#", "#", "#"},
                {"S", " ", "E"},
                {"#", "#", "#"}};
        Labyrinth l = fromString(lArr);
        l.getCellType(new Coordinate(-1, 1));
    }

    @Test(expected = CellException.class)
    public void testGetCellTypeException2() throws Exception {
        String[][] lArr = {
                {"#", "#", "#"},
                {"S", " ", "E"},
                {"#", "#", "#"}};
        Labyrinth l = fromString(lArr);
        l.getCellType(new Coordinate(1, -1));
    }

    @Test(expected = CellException.class)
    public void testGetCellTypeException3() throws Exception {
        String[][] lArr = {
                {"#", "#", "#"},
                {"S", " ", "E"},
                {"#", "#", "#"}};
        Labyrinth l = fromString(lArr);
        l.getCellType(new Coordinate(3, 3));
    }

    @Test
    public void testHasPlayerFinished() throws Exception {
        String[][] lArr = {
                {"#", "#", "#"},
                {"S", " ", "#"},
                {"#", "#", "#"}};
        Labyrinth l = fromString(lArr);
        assertEquals(false, l.hasPlayerFinished());
        l.setCellType(new Coordinate(0, 1), CellType.END);
        assertEquals(true, l.hasPlayerFinished());
    }

    @Test
    public void testPlayerPositionAndMovePlayer() throws Exception {
        String[][] lArr = {
                {"#", "#", "#", "#", "#"},
                {"S", " ", "#", " ", "E"},
                {"#", " ", "#", " ", "#"},
                {"#", " ", " ", " ", "#"},
                {"#", "#", "#", "#", "#"}};
        Labyrinth l = fromString(lArr);
        assertEquals(new Coordinate(0, 1), l.getPlayerPosition());
        l.movePlayer(Direction.EAST);
        assertEquals(new Coordinate(1, 1), l.getPlayerPosition());
        l.movePlayer(Direction.SOUTH);
        assertEquals(new Coordinate(1, 2), l.getPlayerPosition());
        l.movePlayer(Direction.SOUTH);
        assertEquals(new Coordinate(1, 3), l.getPlayerPosition());
        l.movePlayer(Direction.EAST);
        assertEquals(new Coordinate(2, 3), l.getPlayerPosition());
        l.movePlayer(Direction.EAST);
        assertEquals(new Coordinate(3, 3), l.getPlayerPosition());
        l.movePlayer(Direction.WEST);
        assertEquals(new Coordinate(2, 3), l.getPlayerPosition());
        l.movePlayer(Direction.EAST);
        assertEquals(new Coordinate(3, 3), l.getPlayerPosition());
        l.movePlayer(Direction.NORTH);
        assertEquals(new Coordinate(3, 2), l.getPlayerPosition());
        l.movePlayer(Direction.NORTH);
        assertEquals(new Coordinate(3, 1), l.getPlayerPosition());
    }

    @Test
    public void testPossibleMoves() throws Exception {
        String[][] lArr = {
                {"#", "#", "#", "#", "#"},
                {"#", " ", " ", " ", "E"},
                {"#", " ", "S", " ", "#"},
                {"#", " ", " ", " ", "#"},
                {"#", "#", "#", "#", "#"}};
        Labyrinth l = fromString(lArr);
        assertEquals(4, l.possibleMoves().size());

        String[][] lArr2 = {
                {"#", "#", "#", "#", "#"},
                {"#", " ", "#", " ", "E"},
                {"#", "#", "S", "#", "#"},
                {"#", " ", "#", " ", "#"},
                {"#", "#", "#", "#", "#"}};
        l = fromString(lArr2);
        assertEquals(0, l.possibleMoves().size());

        String[][] lArr3 = {
                {"#", "#", "#", "#", "#"},
                {"#", " ", "#", " ", "E"},
                {"#", " ", "S", "#", "#"},
                {"#", " ", "#", " ", "#"},
                {"#", "#", "#", "#", "#"}};
        l = fromString(lArr3);
        assertEquals(1, l.possibleMoves().size());
        assertEquals(Direction.WEST, l.possibleMoves().get(0));

        String[][] lArr4 = {
                {"#", "#", "#", "#", "#"},
                {"#", " ", " ", " ", "E"},
                {"#", " ", "S", "#", "#"},
                {"#", " ", "#", " ", "#"},
                {"#", "#", "#", "#", "#"}};
        l = fromString(lArr4);
        assertEquals(2, l.possibleMoves().size());

        String[][] lArr5 = {
                {"#", "#", "#", "#", "#"},
                {"#", " ", "#", " ", "E"},
                {"#", " ", "S", "#", "#"},
                {"#", " ", " ", " ", "#"},
                {"#", "#", "#", "#", "#"}};
        l = fromString(lArr5);
        assertEquals(2, l.possibleMoves().size());

        String[][] lArr6 = {
                {"#", "#", "#", "#", "#"},
                {"#", " ", "#", " ", "E"},
                {"#", "#", "S", " ", "#"},
                {"#", " ", "#", " ", "#"},
                {"#", "#", "#", "#", "#"}};
        l = fromString(lArr6);
        assertEquals(1, l.possibleMoves().size());
        assertEquals(Direction.EAST, l.possibleMoves().get(0));

        String[][] lArr7 = {
                {"#", "#", "#", "#", "#"},
                {"#", " ", " ", " ", "E"},
                {"#", "#", "S", "#", "#"},
                {"#", " ", "#", " ", "#"},
                {"#", "#", "#", "#", "#"}};
        l = fromString(lArr7);
        assertEquals(1, l.possibleMoves().size());
        assertEquals(Direction.NORTH, l.possibleMoves().get(0));
    }

    @Test
    public void testLoadFile() throws Exception {
        String[][] lArr = {
                {"#", "#", "#", "#", "#"},
                {"S", " ", "#", " ", "E"},
                {"#", " ", "#", " ", "#"},
                {"#", " ", " ", " ", "#"},
                {"#", "#", "#", "#", "#"}};
        Labyrinth lSample = fromString(lArr);
        Labyrinth l = getLabyrinthImpl();
        l.loadLabyrinthFile("labyrinth1.txt");
        assertSameLabyrinth(l, lSample);
        assertEquals(new Coordinate(0, 1), l.getPlayerPosition());

        l = getLabyrinthImpl();
        l.loadLabyrinthFile("labyrinth2.txt");
        assertEquals(new Coordinate(0, 1), l.getPlayerPosition());
    }

    @Test
    public void testRandomPlayer() throws Exception {
        String[][] lArr = {
                {"#", "#", "#", "#", "#"},
                {"S", " ", "#", " ", "E"},
                {"#", " ", "#", " ", "#"},
                {"#", " ", " ", " ", "#"},
                {"#", "#", "#", "#", "#"}};
        Labyrinth l = fromString(lArr);
        Player rp = getRandomPlayerImpl();
        while (!l.hasPlayerFinished()) {
            Direction d = rp.nextMove(l);
            l.movePlayer(d);
        }
        assertEquals(true, l.hasPlayerFinished());
        assertEquals(l.getCellType(l.getPlayerPosition()), CellType.END);

        Labyrinth l2 = getLabyrinthImpl();
        l2.loadLabyrinthFile("labyrinth2.txt");
        while (!l2.hasPlayerFinished()) {
            Direction d = rp.nextMove(l2);
            l2.movePlayer(d);
        }
        assertEquals(true, l2.hasPlayerFinished());
        assertEquals(l2.getCellType(l2.getPlayerPosition()), CellType.END);
    }

    @Test
    public void testWallFollowerPlayer() throws Exception {
        String[][] lArr = {
                {"#", "#", "#", "#", "#"},
                {"S", " ", "#", " ", "E"},
                {"#", " ", "#", " ", "#"},
                {"#", " ", " ", " ", "#"},
                {"#", "#", "#", "#", "#"}};
        Labyrinth l = fromString(lArr);
        Player wp = getWallFollowerPlayerImpl();
        while (!l.hasPlayerFinished()) {
            Direction d = wp.nextMove(l);
            l.movePlayer(d);
        }
        assertEquals(true, l.hasPlayerFinished());
        assertEquals(l.getCellType(l.getPlayerPosition()), CellType.END);

        String[] files = {"labyrinth2.txt", "labyrinth3.txt"};
        for (String file : files) {
            l = getLabyrinthImpl();
            l.loadLabyrinthFile(file);
            while (!l.hasPlayerFinished()) {
                Direction d = wp.nextMove(l);
                l.movePlayer(d);
            }
            assertEquals(true, l.hasPlayerFinished());
            assertEquals(l.getCellType(l.getPlayerPosition()), CellType.END);
        }
    }

    @Test
    public void testConsciousPlayer() throws Exception {
        Player cp = getConsciousPlayerImpl();

        String[] files = {"labyrinth1.txt", "labyrinth2.txt", "labyrinth3.txt"};
        for (String file : files) {
            Labyrinth l = getLabyrinthImpl();
            l.loadLabyrinthFile(file);
            while (!l.hasPlayerFinished()) {
                Direction d = cp.nextMove(l);
                l.movePlayer(d);
            }
            assertEquals(true, l.hasPlayerFinished());
            assertEquals(l.getCellType(l.getPlayerPosition()), CellType.END);
        }
    }

    @Test(expected = InvalidMoveException.class)
    public void testInvalidMove1() throws Exception {
        String[][] lArr = {
                {"#", "#", "#"},
                {"S", " ", "E"},
                {"#", "#", "#"}};
        Labyrinth l = fromString(lArr);
        l.movePlayer(Direction.NORTH);
    }

    @Test(expected = InvalidMoveException.class)
    public void testInvalidMove2() throws Exception {
        String[][] lArr = {
                {"#", "#", "#"},
                {"S", " ", "E"},
                {"#", "#", "#"}};
        Labyrinth l = fromString(lArr);
        l.movePlayer(Direction.EAST);
        l.movePlayer(Direction.SOUTH);
    }

    @Test(expected = InvalidMoveException.class)
    public void testInvalidMove3() throws Exception {
        String[][] lArr = {
                {"#", "#", "#"},
                {"S", " ", "E"},
                {"#", "#", "#"}};
        Labyrinth l = fromString(lArr);
        l.movePlayer(Direction.WEST);
    }

    private void assertSameLabyrinth(Labyrinth l1, Labyrinth l2) throws CellException {
        assertEquals(l1.getHeight(), l2.getHeight());
        assertEquals(l1.getWidth(), l2.getWidth());
        for (int c = 0; c < l1.getWidth(); c++) {
            for (int r = 0; r < l1.getHeight(); r++) {
                Coordinate coord = new Coordinate(c, r);
                assertEquals(l1.getCellType(coord), l2.getCellType(coord));
            }
        }
    }

    private Labyrinth fromString(String[][] strArr) throws CellException {
        Labyrinth l = getLabyrinthImpl();
        l.setSize(strArr[0].length, strArr.length);
        for (int i = 0; i < strArr.length; i++) {
            for (int j = 0; j < strArr[i].length; j++) {
                CellType type;
                String str = strArr[i][j];
                switch (str) {
                    case "#":
                        type = CellType.WALL;
                        break;
                    case "S":
                        type = CellType.START;
                        break;
                    case "E":
                        type = CellType.END;
                        break;
                    default:
                        type = CellType.EMPTY;
                        break;
                }
                l.setCellType(new Coordinate(j, i), type);
            }
        }
        return l;
    }


    private Labyrinth getLabyrinthImpl() {
        Labyrinth lb = new LabyrinthImpl();
        lb.loadLabyrinthFile("C:\\progmatic-labyrinth\\labyrinth1.txt");
        try {
            lb.possibleMoves();
            lb.movePlayer(Direction.EAST);
            return lb;
        } catch (InvalidMoveException e) {
            e.getMessage();
        }
        return null;
    }


    public Player getRandomPlayerImpl() {
        return new RandomPlayer();
    }


    private Player getWallFollowerPlayerImpl() {
        return new WallFollowerPlayer();
    }

    // TODO
    private Player getConsciousPlayerImpl() {
        return null;
    }
}
