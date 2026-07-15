# Deep Reinforcement Learning

This directory contains a complete **Deep Reinforcement Learning (DRL)** implementation for solving Sokoban using the **Proximal Policy Optimization (PPO)** algorithm.

The project includes:

* PPO agent (Actor-Critic architecture)
* CNN-based state encoder
* Experience memory for rollouts
* PPO training pipeline
* Training and evaluation utilities
* Reward shaping
* Deadlock detection
* Multi-level training with random level selection / curriculum learning support
* Logging and checkpoint management

---

## Symbol Mappings

| Symbol | Meaning              |
|------:|----------------------|
| `1`   | Player               |
| `0`   | Box                  |
| `#`   | Wall                 |
| `$`   | Target               |
| `*`   | Box on Target        |
| `+`   | Player on Target     |

---

## Current Status

*  PPO implementation completed **(Done)**
*  Sokoban Gymnasium environment completed **(Done)**
*  Training and evaluation pipeline completed **(Done)**
*  Training is in progress to obtain the best-performing model. **(Pending)**
*  A graphical user interface (GUI) using **Pygame** will be added for interactive visualization of the trained agent. **(Pending)**
