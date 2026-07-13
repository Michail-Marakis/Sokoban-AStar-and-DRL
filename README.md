# SOKOBAN AI SOLVER

e project features a traditional solver implemented using the **A\*** search algorithm, alongside a **Deep Reinforcement Learning (DRL)** agent trained with the PPO algorithm in a custom Gymnasium (formerly OpenAI Gym) environment. The next milestone is to build an interactive frontend for both algorithms using **Java Swing Framework** for the A* and **pygame** for DRL.

---

## Game Rules & Execution

At the start of the program, the user is prompted to select the desired **Sokoban level difficulty**.

- If a solution exists, the solver prints **each player move** sequentially until the final state is reached.
- If no solution can be found, the program terminates with an appropriate notification message.

### Goal State
The objective is to guide the player to push **every box onto a designated target tile**.

- Each target tile can hold **exactly one box**.
- **Multiple boxes on the same target are not allowed.**
- There is a **one-to-one (1–1)** correspondence between boxes and targets to complete the level.
  
For a visual demonstration and tutorial on how the game is played, check out this video [here](https://youtu.be/M_cXO0-2sSE?si=3mNLrBsCis5hRT3T).

---

For the **A\*** implementation click [here](https://github.com/Michail-Marakis/Sokoban-solver-using-A-star/tree/main/Astar)      
For the **DRL** implementation click [here](https://github.com/Michail-Marakis/Sokoban-solver-using-A-star/tree/main/Deep%20Reinforcement%20Learning)
