import os
import time


class Renderer:

    #Clear terminal
    @staticmethod
    def clear():
        os.system("cls" if os.name == "nt" else "clear")


    #Print the current Sokoban board.
    @staticmethod
    def render(board):

        for row in board:
            print("".join(row))

        print()


    #Print info
    @staticmethod
    def render_info(step=None,
                    reward=None,
                    total_reward=None,
                    episode=None):

        if episode is not None:
            print(f"Episode: {episode}")

        if step is not None:
            print(f"Step: {step}")

        if reward is not None:
            print(f"Reward: {reward:.2f}")

        if total_reward is not None:
            print(f"Total Reward: {total_reward:.2f}")

        if any(v is not None for v in [episode, step, reward, total_reward]):
            print()


    #print one frame of the game.
    @staticmethod
    def render_frame(board,
                     step=None,
                     reward=None,
                     total_reward=None,
                     episode=None,
                     clear_screen=True):

        if clear_screen:
            Renderer.clear()

        Renderer.render_info(
            step=step,
            reward=reward,
            total_reward=total_reward,
            episode=episode
        )

        Renderer.render(board)


    #Animate a list of board states.
    @staticmethod
    def animate(path,
                delay=0.15):

        total_steps = len(path)

        for step, board in enumerate(path):

            Renderer.render_frame(
                board=board,
                step=step,
                clear_screen=True
            )

            time.sleep(delay)

        print(f"Finished ({total_steps} steps)")


    #Pause execution until ENTER is pressed.
    @staticmethod
    def wait():
        input("Press ENTER to continue...")