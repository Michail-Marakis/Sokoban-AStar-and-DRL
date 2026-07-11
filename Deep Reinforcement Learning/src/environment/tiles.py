from enum import StrEnum


class Tile(StrEnum):
    EMPTY = ' '
    PLAYER = '1'
    BOX = '0'
    WALL = '#'
    TARGET = '$'
    BOX_ON_TARGET = '*'
    PLAYER_ON_TARGET = '+'