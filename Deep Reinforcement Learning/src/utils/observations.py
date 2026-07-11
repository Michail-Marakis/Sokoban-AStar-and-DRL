import numpy as np
from gymnasium import spaces


class ObservationEncoder:

    SYMBOL_TO_CHANNEL = {
        '1': 0,   #Player
        '0': 1,   #Box
        '#': 2,   #Wall
        '$': 3,   #Target
        '*': 4,   #Box on Target
        '+': 5    #Player on Target
    }

    @staticmethod
    def encode(board):

        height = len(board)
        width = len(board[0])

        observation = np.zeros((6, height, width), dtype=np.float32)

        for row in range(height):
            for col in range(width):
                
                symbol = board[row][col]

                if symbol in ObservationEncoder.SYMBOL_TO_CHANNEL:
                    channel = ObservationEncoder.SYMBOL_TO_CHANNEL[symbol]
                    observation[channel, row, col] = 1.0

        return observation


    @staticmethod
    def observation_space(height, width):

        return spaces.Box(
            low=0.0,
            high=1.0,
            shape=(6, height, width),
            dtype=np.float32
        )