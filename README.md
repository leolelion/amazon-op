# Game of Amazons – AI Report and Implementation  

This report outlines the techniques used in the Game of the Amazons AI and highlights key software engineering aspects of its design. The AI follows a hybrid move-selection strategy and is built in a modular fashion to keep game logic separate from UI and networking.  

---

## Techniques Employed  

### **Hybrid Move Selection Strategy**  

- **Monte Carlo Tree Search (MCTS):**  
  - **When It’s Used:**  
    Applied during the early-to-mid game when the board is still open (more than 50 empty cells).  
  - **How It Works:**  
    MCTS builds a search tree by running multiple random simulations from the current state. It uses the Upper Confidence Bounds for Trees (UCT) formula to balance **exploration** (trying new moves) and **exploitation** (focusing on the best-known moves).  
  - **Why It’s Effective:**  
    Works well when there are many possible moves, as it doesn’t require an exhaustive search and can function without a complex heuristic.  

- **Minimax with Alpha-Beta Pruning:**  
  - **When It’s Used:**  
    Activated during the late game when the board has fewer than 50 empty cells.  
  - **How It Works:**  
    The algorithm searches through possible moves up to a fixed depth, evaluating board positions using a heuristic function. Alpha-beta pruning is applied to cut off unpromising branches, improving efficiency.  
  - **Why It’s Effective:**  
    Provides precise, deterministic decisions in deep tactical situations, which become more common in the late game.  

- **Decision-Making Mechanism:**  
  - The **Brain** class determines which algorithm to use based on the current board state (i.e., the number of empty cells). This adaptive approach allows the AI to use the most suitable strategy for each phase of the game.  

---

## Software Engineering Aspects  

### **Modular Design and Separation of Concerns**  

- **COSC322Test:**  
  - Acts as the main interface between the game server, the GUI, and the AI.  
  - Handles server messages (e.g., game start, board updates, opponent moves) and delegates move generation and game status evaluation to the **Brain** class.  
  - Manages network communication using the GameClient API.  

- **Brain:**  
  - Serves as the AI’s core logic processor.  
  - Handles move generation, algorithm selection (MCTS or Minimax), and win/loss conditions.  
  - Includes helper functions for checking valid moves and board boundaries.  
  - Keeps decision-making isolated, allowing changes to AI logic without affecting the UI or networking.  

- **Move:**  
  - Represents a single move, including the queen’s initial position, new position, and arrow placement.  
  - Provides methods for simulating moves (without altering the actual game state) and applying moves in the live game.  
  - This clear separation simplifies testing and debugging.  

- **MCTS and Minimax:**  
  - Each algorithm is implemented in its own class.  
  - This makes the code easier to manage and allows modifications or replacements of one algorithm without disrupting the entire system.  

### **Maintainability and Extensibility**  

- **Clear Interfaces:**  
  - Each class has a well-defined role. For instance, **COSC322Test** handles communication and GUI updates, while **Brain** focuses on game logic.  

- **Encapsulation:**  
  - Complex logic (win/loss checks, move simulations, and algorithm selection) is confined to the **Brain** class. This minimizes code duplication and improves maintainability.  

- **API Integration:**  
  - The AI interacts with the GameClient API using predefined methods (e.g., `sendMoveMessage`) and constants (e.g., `GAME_STATE_PLAYER_LOST`), ensuring compatibility with the game server.  

- **Testing and Debugging:**  
  - Console logs track algorithm choices, move decisions, and game state updates, aiding in debugging and testing.  

- **Adaptive Strategy:**  
  - The AI dynamically adjusts its approach based on the number of empty cells, ensuring effective gameplay across different phases.  

---

## Conclusion  

The Game of the Amazons AI employs a hybrid approach that combines MCTS and Minimax, dynamically choosing the best algorithm based on the board state. The system is designed with a clear separation between game logic, network communication, and UI management. This modular architecture improves maintainability, simplifies testing, and allows for future enhancements. This report provides an overview of the AI's techniques and highlights the software engineering principles that make the system both robust and easy to understand.
