# SOKOBAN AI SOLVER

An AI-based solver for the classic **Sokoban** puzzle, implemented using the **A\*** search algorithm. Our next goal is to implement the frontend and also develop a **Deep Reinforcement Learning (DRL)** using custom Gymnasium (formerly, OpenAI Gym) environement, using PPO algorithm.

---

## Game Rules

At the start of the program, the user is prompted to select the desired **Sokoban level difficulty**.

- If a solution exists, **each player move** is printed until the final state is reached.
- At every step, the **heuristic value (`h`)** and the **total cost (`f`)** are displayed.
- If no solution exists, an appropriate message is printed.

### Goal State
The objective is to place **each box onto a target**.

- Each target can hold **exactly one box**
- **Multiple boxes on the same target are not allowed**
- The correspondence must be **one-to-one (1–1)**
  
For a better and visual understanding of the game checkout this Youtoube tutorial [here](https://youtu.be/M_cXO0-2sSE?si=3mNLrBsCis5hRT3T).

---

For the **A\*** implementation click [here](https://github.com/Michail-Marakis/Sokoban-solver-using-A-star/tree/main/Astar)     
For the **DRL** implementation click [here](https://github.com/Michail-Marakis/Sokoban-solver-using-A-star/tree/main/Deep%20Reinforcement%20Learning)
