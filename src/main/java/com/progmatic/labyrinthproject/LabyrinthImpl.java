package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.CellType;
import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.exceptions.CellException;
import com.progmatic.labyrinthproject.exceptions.InvalidMoveException;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author pappgergely
 */
public class LabyrinthImpl implements Labyrinth {
    char[][] labirintus;
    ArrayList<Coordinate> koordinatak = new ArrayList<>();        //azert tarolom a koordinatakat, hogy szukseg eseten kesobb hasznalhassam az elozo lepesekez is
    ArrayList<Direction> lehetsegesUtak;
    Coordinate aktualisPoz;

    public LabyrinthImpl() {

    }


    //ha a labirintus ketdim. char tomb nem null, akkor visszaadom  sorok majd az oszlopok hosszat, egyebkent -1

    @Override
    public int getWidth() {
        if (labirintus != null) {
            return labirintus[0].length - 1;
        } else {
            return -1;
        }
    }

    @Override
    public int getHeight() {
        if (labirintus != null) {
            return labirintus.length - 1;
        } else {
            return -1;
        }
    }


    //a fromString() bol kiindulva kell dolgozni
    //players position-t a startra allitani
    //egy labirintust egy ketdimenzios Stringben tarolunk

    @Override
    public void loadLabyrinthFile(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            int width = Integer.parseInt(sc.nextLine());
            int height = Integer.parseInt(sc.nextLine());
            labirintus = new char[height][width];
            for (int hh = 0; hh < height; hh++) {
                String line = sc.nextLine();
                for (int ww = 0; ww < width; ww++) {
                    switch (line.charAt(ww)) {
                        case 'W':
                            labirintus[hh][ww] = '#';
                            break;
                        case 'E':
                            labirintus[hh][ww] = 'E';
                            break;
                        case 'S':                           //ide kell allitani a player positionjat starthoz
                            labirintus[hh][ww] = 'S';
                            setCellType(new Coordinate(hh, ww), CellType.START);       //cellException
                            break;
                        default:
                            labirintus[hh][ww] = ' ';     //kell egy default ertek a szokozoknek is, ahol lepkedhetunk
                    }
                }
            }
        } catch (FileNotFoundException | NumberFormatException | CellException ex) {    //hozza kell irni a cellExceptiont
            System.out.println(ex.toString());
        }
    }

    @Override
    public CellType getCellType(Coordinate c) throws CellException {  //Celltype egy enum!!!
        int col = c.getCol();  //aktualis oszlop
        int row = c.getRow();  //aktualis sor

        switch (labirintus[row][col]) {
            case '#':
                return CellType.WALL;               // a break itt felesleges a return miatt

            case 'E':
                return CellType.END;

            case 'S':
                return CellType.START;

            default:
                return CellType.EMPTY;
        }
    }

    @Override
    public void setSize(int width, int height) {
        try {
            labirintus = null;
            labirintus = new char[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    setCellType(new Coordinate(i, j), CellType.EMPTY);
                }
            }
        } catch (CellException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void setCellType(Coordinate c, CellType type) throws CellException {
        int col = c.getCol();  //aktualis oszlop
        int row = c.getRow();  //aktualis sor

        switch (type) {
            case EMPTY:
                labirintus[row][col] = ' ';
                break;
            case WALL:
                labirintus[row][col] = '#';
                break;
            case END:
                labirintus[row][col] = 'E';
                break;
            default:
                labirintus[row][col] = 'S';             //beallitani a players start pos.
                setCellType(new Coordinate(col, row), CellType.START);

        }
    }

    @Override
    public Coordinate getPlayerPosition() {
        return koordinatak.get(koordinatak.size() - 1);
    }

    @Override
    public boolean hasPlayerFinished() {
        try {
            if (getCellType(getPlayerPosition()) == CellType.END) {
                return true;
            } else {
                return false;
            }
        } catch (CellException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Direction> possibleMoves() {    //egy listat visszaadni a poziciotol fuggoen. Merre mehet hogy ne utkozzon falba....
        lehetsegesUtak = new ArrayList<>();
        aktualisPoz = getPlayerPosition();          //a koordinatak utolso eleme az aktualis pozicio

        int x = aktualisPoz.getRow();
        int y = aktualisPoz.getCol();           //vizsgalni: nem utkozik falba es nem lep ki a labirintusbol

        //jobbra mozgas(EAST vizsgalat)
        for (int i = 0; i <= getHeight(); i++) {
            if (x >= 0 && x <= getWidth() - 1 && labirintus[i][x + 1] != '#') {       //a getwidth() es a getHeight() mar a 0 tol szamitott erteket adja vissza
                lehetsegesUtak.add(Direction.EAST);
            }
        }

        //balre mozgas(WEST vizsgalat)
        for (int i = 0; i <= getHeight(); i++) {
            if (x >= 1 && x <= getWidth() && labirintus[i][x - 1] != '#') {
                lehetsegesUtak.add(Direction.WEST);
            }
        }

        //fel mozgas(North vizsgalat)
        for (int i = 1; i <= getWidth(); i++) {
            if (y >= 1 && y <= getHeight() && labirintus[y - 1][i] != '#') {
                lehetsegesUtak.add(Direction.NORTH);
            }
        }

        //le mozgas(SOUTH vizsgalat)
        for (int i = 1; i <= getWidth(); i++) {
            if (y >= 0 && y <= getHeight() - 1 && labirintus[y + 1][i] != '#') {
                lehetsegesUtak.add(Direction.SOUTH);
            }
        }
        return lehetsegesUtak;
    }

    @Override
    public void movePlayer(Direction direction) throws InvalidMoveException {
        //meg kell hatarozni a jatekos akt poziciojat
        aktualisPoz = getPlayerPosition();
        //meg kell nezni hogy errol a poziciorol merre mehet
        possibleMoves();

        switch (direction) {
            case NORTH:
                if (lehetsegesUtak.contains(Direction.NORTH)) {
                    aktualisPoz = new Coordinate(aktualisPoz.getRow(), aktualisPoz.getCol() - 1);
                } else {
                    throw new InvalidMoveException();
                }
                break;

            case SOUTH:
                if (lehetsegesUtak.contains(Direction.SOUTH)) {
                    aktualisPoz = new Coordinate(aktualisPoz.getRow(), aktualisPoz.getCol() + 1);
                } else {
                    throw new InvalidMoveException();
                }
                break;

            case EAST:
                if (lehetsegesUtak.contains(Direction.EAST)) {
                    aktualisPoz = new Coordinate(aktualisPoz.getRow() + 1, aktualisPoz.getCol());
                } else {
                    throw new InvalidMoveException();
                }
                break;

            case WEST:
                if (lehetsegesUtak.contains(Direction.WEST)) {
                    aktualisPoz = new Coordinate(aktualisPoz.getRow() - 1, aktualisPoz.getCol());
                } else {
                    throw new InvalidMoveException();
                }
                break;
        }
    }
}
