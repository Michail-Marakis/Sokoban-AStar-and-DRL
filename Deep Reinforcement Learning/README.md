# Deep Reinforcement Learning

This directory contains a **Deep Reinforcement Learning (DRL)** implementation for solving Sokoban using the **Proximal Policy Optimization (PPO)** algorithm.

The purpose of this implementation is to experiment with Deep Reinforcement Learning by developing a complete DRL pipeline from scratch. Τhis project was also an opportunity to become more familiar with designing custom Gymnasium environments, implementing reinforcement learning algorithms in PyTorch, and understanding how modern DRL agents learn through interaction with an environment. It serves as a hands-on learning project before starting my undergraduate thesis on Robotics using Deep Reinforcement Learning, Multi - Armed Bandits and Automations.

The project includes:

- PPO agent (Actor-Critic architecture)
- CNN-based state encoder
- Experience memory for rollouts
- PPO training pipeline
- Reward shaping
- Deadlock detection
- Multi-level training with random level selection / curriculum learning support
- Training and evaluation utilities
- Logging and checkpoint management

---

## Symbol Mappings

| Symbol | Meaning |
|:------:|---------|
| `1` | Player |
| `0` | Box |
| `#` | Wall |
| `$` | Target |
| `*` | Box on Target |
| `+` | Player on Target |

---

## Current Status

- PPO implementation completed
- Custom Sokoban Gymnasium environment completed
- Training and evaluation pipeline completed
- Training is currently in progress to obtain a well-performing model across multiple Sokoban levels.
- A graphical user interface (GUI) using **Pygame** will be added for visualizing the trained agent.

---

## Project Structure

```
Deep Reinforcement Learning
├── checkpoints/              # Saved PPO model checkpoints
├── logs/                     # Training and evaluation logs
├── src/
│   ├── agent/
│   │   ├── memory.py         # PPO rollout memory buffer
│   │   ├── ppo_agent.py      # PPO algorithm implementation
│   │   └── trainer.py        # Training loop and evaluation
│   │
│   ├── assets/               # pygame assets
│   │
│   ├── environment/
│   │   ├── actions.py
│   │   ├── board_utils.py
│   │   ├── deadlock.py
│   │   ├── levels.py
│   │   ├── observations.py
│   │   ├── renderer.py
│   │   ├── sokoban_env.py    # Custom Gymnasium environment
│   │   └── tiles.py
│   │
│   ├── levels/
│   │   ├── easy/
│   │   ├── medium/
│   │   ├── hard/
│   │   └── expert/           # Training levels grouped by difficulty
│   │
│   ├── models/
│   │   ├── actor_critic.py   # Actor-Critic neural network
│   │   └── cnn.py            # CNN feature extractor used by both networks
│   │
│   ├── utils/
│   │   ├── config.py         # Hyperparameters and training configuration
│   │   └── logger.py         # Training & evaluation logging
│   │
│   ├── evaluate.py           # Evaluate a trained model
│   └── train.py              # Start PPO training
│
├── README.md
└── requirements.txt
```

---

## How to Run

### 1. Install the required packages

```bash
pip install -r requirements.txt
```

### 2. Configure training

Edit `src/utils/config.py` to adjust:

- PPO hyperparameters
- Reward shaping
- Training episodes
- Checkpoint loading
- Evaluation frequency

The training pipeline supports two different level selection strategies:

- **Random Level Sampling**: every training episode uses a randomly selected level from the available training set. This helps the agent generalize instead of overfitting to a single puzzle.

- **Curriculum Learning**: the agent starts training on easier levels and progressively unlocks more difficult ones as training progresses, making learning more stable.

### 3. Start training

```bash
cd src
python train.py
```

### 4. Evaluate a trained model

Set the desired checkpoint in `config.py`:

```python
LOAD_CHECKPOINT = True
CHECKPOINT_PATH = "checkpoints/model.pt"
```

Then run:

```bash
cd src
python evaluate.py
```

---

## Notes

- Levels are loaded dynamically from the `levels/` directory.
- Checkpoints are saved automatically during training.
- Training and evaluation statistics are stored in the `logs/` directory.