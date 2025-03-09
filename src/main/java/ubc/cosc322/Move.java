package ubc.cosc322;
import ubc.cosc322.Move;
import java.util.ArrayList;

public class Move {
    public ArrayList<Integer> queenStart;
    public ArrayList<Integer> queenEnd;
    public ArrayList<Integer> arrow;

    public Move(ArrayList<Integer> queenStart, ArrayList<Integer> queenEnd, ArrayList<Integer> arrow) {
        this.queenStart = queenStart;
        this.queenEnd = queenEnd;
        this.arrow = arrow;
    }
}

