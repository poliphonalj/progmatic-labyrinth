package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;
import com.progmatic.labyrinthproject.interfaces.Player;

import java.util.ArrayList;

public class WallFollowerPlayer implements Player {

    @Override
    public Direction nextMove(Labyrinth l) {
        ArrayList possibleMoves = (ArrayList) l.possibleMoves();
        for (int i = 0; i < possibleMoves.size(); i++) {
            try {
                //ha mindig csak jobbra kanyarodik:(EAST)
                if (possibleMoves.size() > 1) {     //egy elagazashoz ertunk iranypreferenciakat szabunk
                    if (possibleMoves.contains(Direction.EAST)) {
                       return Direction.EAST;
                    }
                    if (!(possibleMoves.contains(Direction.EAST)) && possibleMoves.contains(Direction.SOUTH)) {
                        return Direction.SOUTH;
                    }
                    if(!(possibleMoves.contains(Direction.EAST)) &&(!(possibleMoves.contains(Direction.SOUTH)))){
                        return Direction.NORTH;
                    }
                    else{
                        return Direction.WEST;
                    }

                }
            } catch (Exception e) {
                System.out.println(e.getMessage());

                  }

        }
        return null;

    }
}
