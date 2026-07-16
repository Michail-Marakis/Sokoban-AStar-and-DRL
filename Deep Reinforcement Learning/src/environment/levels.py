from pathlib import Path
import random


class GameLevels:

    LEVEL_FOLDER = Path(__file__).parent.parent / "levels"

    DIFFICULTIES = [
        "easy",
        "medium",
        "hard",
        "expert"
    ]


    @staticmethod
    def load(path):

        with open(path, "r") as file:

            board = [
                list(line.rstrip("\n"))
                for line in file
                if line.strip()
            ]

        return board


    @staticmethod
    def get_levels(difficulty):

        folder = GameLevels.LEVEL_FOLDER / difficulty

        return sorted(folder.glob("*.txt"))


    @staticmethod
    def random_level(difficulties):

        available = []

        for difficulty in difficulties:

            available.extend(
                GameLevels.get_levels(difficulty)
            )

        return random.choice(available)