package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MCTS {
    private static final int SIMULATION_DEPTH = 5;
    private static final int ITERATIONS = 1000;
    private static final double EXPLORATION_CONSTANT = Math.sqrt(2);
    private Random random = new Random();

    public Move findBestMove(ArrayList<Integer> gameState, int playerColor) {
        Node root = new Node(null, new ArrayList<>(gameState), null, playerColor);
        for (int i = 0; i < ITERATIONS; i++) {
            Node selected = select(root);
            if (!isTerminal(selected)) {
                expand(selected);
            }
            Node toSimulate = selected;
            if (!selected.children.isEmpty()) {
                toSimulate = selected.children.get(random.nextInt(selected.children.size()));
            }
            int simulationResult = simulate(toSimulate, playerColor);
            backpropagate(toSimulate, simulationResult);
        }
        Node bestChild = null;
        int bestVisits = -1;
        for (Node child : root.children) {
            if (child.visits > bestVisits) {
                bestVisits = child.visits;
                bestChild = child;
            }
        }
        return bestChild != null ? bestChild.move : null;
    }

    private class Node {
        Node parent;
        ArrayList<Node> children;
        ArrayList<Integer> gameState;
        Move move;
        int visits;
        double wins;
        int playerColor;
        Node(Node parent, ArrayList<Integer> gameState, Move move, int playerColor) {
            this.parent = parent;
            this.gameState = gameState;
            this.move = move;
            this.playerColor = playerColor;
            this.visits = 0;
            this.wins = 0;
            this.children = new ArrayList<>();
        }
        boolean isFullyExpanded() {
            List<Move> allMoves = generateAllValidMoves(gameState, playerColor);
            return children.size() == allMoves.size();
        }
    }

    private Node select(Node node) {
        while (!node.children.isEmpty()) {
            if (!node.isFullyExpanded()) {
                return node;
            } else {
                node = bestUCT(node);
            }
        }
        return node;
    }

    private Node bestUCT(Node node) {
        Node best = null;
        double bestValue = Double.NEGATIVE_INFINITY;
        for (Node child : node.children) {
            double winRate = child.wins / (child.visits + 1e-6);
            double uctValue = winRate + EXPLORATION_CONSTANT * Math.sqrt(Math.log(node.visits + 1) / (child.visits + 1e-6));
            if (uctValue > bestValue) {
                bestValue = uctValue;
                best = child;
            }
        }
        return best;
    }

    private void expand(Node node) {
        List<Move> moves = generateAllValidMoves(node.gameState, node.playerColor);
        for (Move move : moves) {
            boolean alreadyExpanded = false;
            for (Node child : node.children) {
                if (child.move.getQueenStart().equals(move.getQueenStart()) &&
                        child.move.getQueenEnd().equals(move.getQueenEnd()) &&
                        child.move.getArrow().equals(move.getArrow())) {
                    alreadyExpanded = true;
                    break;
                }
            }
            if (!alreadyExpanded) {
                ArrayList<Integer> newGameState = new ArrayList<>(node.gameState);
                move.applyMove(newGameState, "Simulation");
                Node child = new Node(node, newGameState, move, node.playerColor);
                node.children.add(child);
                break;
            }
        }
    }

    private int simulate(Node node, int playerColor) {
        ArrayList<Integer> simulationState = new ArrayList<>(node.gameState);
        int simulationDepth = SIMULATION_DEPTH;
        int currentPlayer = playerColor;
        while (simulationDepth-- > 0 && !isTerminalState(simulationState)) {
            List<Move> moves = generateAllValidMoves(simulationState, currentPlayer);
            if (moves.isEmpty()) break;
            Move randomMove = moves.get(random.nextInt(moves.size()));
            randomMove.applyMove(simulationState, "Simulation");
            currentPlayer = (currentPlayer == 1) ? 2 : 1;
        }
        return new Minimax().evaluateBoard(simulationState, playerColor);
    }

    private void backpropagate(Node node, int simulationResult) {
        while (node != null) {
            node.visits++;
            node.wins += simulationResult;
            node = node.parent;
        }
    }

    private boolean isTerminal(Node node) {
        return isTerminalState(node.gameState);
    }

    private boolean isTerminalState(ArrayList<Integer> gameState) {
        return generateAllValidMoves(gameState, 1).isEmpty() ||
                generateAllValidMoves(gameState, 2).isEmpty();
    }

    private ArrayList<Move> generateAllValidMoves(ArrayList<Integer> gameState, int playerColor) {
        ArrayList<Move> validMoves = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (gameState.get(i * 11 + j) == playerColor) {
                    validMoves.addAll(findValidMovesFrom(i, j, gameState));
                }
            }
        }
        return validMoves;
    }

    private ArrayList<Move> findValidMovesFrom(int x, int y, ArrayList<Integer> gameState) {
        ArrayList<Move> validMoves = new ArrayList<>();
        int[] directions = {-1, 0, 1};
        for (int dx : directions) {
            for (int dy : directions) {
                if (dx == 0 && dy == 0) continue;
                int newX = x + dx, newY = y + dy;
                while (isInsideBoard(newX, newY) && gameState.get(newX * 11 + newY) == 0) {
                    int arrowX = newX, arrowY = newY;
                    while (isInsideBoard(arrowX, arrowY) && gameState.get(arrowX * 11 + arrowY) == 0) {
                        ArrayList<Integer> start = new ArrayList<>(List.of(x, y));
                        ArrayList<Integer> end = new ArrayList<>(List.of(newX, newY));
                        ArrayList<Integer> arrow = new ArrayList<>(List.of(arrowX, arrowY));
                        validMoves.add(new Move(start, end, arrow, gameState));
                        arrowX += dx;
                        arrowY += dy;
                    }
                    newX += dx;
                    newY += dy;
                }
            }
        }
        return validMoves;
    }

    private boolean isInsideBoard(int x, int y) {
        return x >= 1 && x <= 10 && y >= 1 && y <= 10;
    }
}
