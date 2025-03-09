package ubc.cosc322;

public class TransitionFunction {
    private static final double THRESHOLD = 0.6;

    public boolean useMinimax(GameState gameState) {
        return Math.random() > THRESHOLD;
    }
}
