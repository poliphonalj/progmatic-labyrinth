package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.exceptions.InvalidMoveException;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;
import com.progmatic.labyrinthproject.interfaces.Player;

import java.util.Random;

public class RandomPlayer implements Player {

    @Override
    public Direction nextMove(Labyrinth l) {
        Random r = new Random();
        int random = r.nextInt(l.possibleMoves().size()); //itt kapok egy listat, mely Directionokat tartalmaz. Ebbol kell random vlasztano
        if (!l.hasPlayerFinished()) {
            return (l.possibleMoves().get(random));
        }
        else{
            return null;        //elerte az end cellat
        }
    }
}
