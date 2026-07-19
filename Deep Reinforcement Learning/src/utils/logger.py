import csv
import os


class Logger:

    def __init__(self, log_dir="logs"):

        self.log_dir = log_dir
        os.makedirs(self.log_dir, exist_ok=True)

    
        #Training log
    
        self.training_file = os.path.join(
            self.log_dir,
            "training_log.csv"
        )

        if not os.path.exists(self.training_file):

            with open(self.training_file, "w", newline="") as file:

                writer = csv.writer(file)

                writer.writerow([
                    "Episode",
                    "Reward",
                    "Steps",
                    "Completed",
                    "Deadlock"
                ])

      
        #Evaluation log
        

        self.evaluation_file = os.path.join(
            self.log_dir,
            "evaluation_log.csv"
        )

        if not os.path.exists(self.evaluation_file):

            with open(self.evaluation_file, "w", newline="") as file:

                writer = csv.writer(file)

                writer.writerow([
                    "Episode",
                    "Level",
                    "SuccessRate",
                    "AverageReward",
                    "AverageSteps",
                    "Deadlocks"
                ])

    #Training
    def log_episode(
        self,
        episode,
        reward,
        steps,
        completed,
        deadlock,
        level
    ):

        print(
        f"Episode {episode:5d} | "
        f"Level {level.stem:15s} | "
        f"Reward: {reward:8.2f} | "
        f"Steps: {steps:4d} | "
        f"Completed: {completed} | "
        f"Deadlock: {deadlock}"
        )

        with open(self.training_file, "a", newline="") as file:

            writer = csv.writer(file)

            writer.writerow([
                episode,
                level.stem,
                reward,
                steps,
                completed,
                deadlock
            ])


    #Evaluation
    def log_evaluation(
        self,
        episode,
        level,
        success_rate,
        average_reward,
        average_steps,
        deadlocks
    ):

        print(
            f"Level {level} | "
            f"Success: {success_rate:.1f}% | "
            f"Avg Reward: {average_reward:.2f} | "
            f"Avg Steps: {average_steps:.1f} | "
            f"Deadlocks: {deadlocks}"
        )

        with open(self.evaluation_file, "a", newline="") as file:

            writer = csv.writer(file)

            writer.writerow([
                "Episode",
                "Level",
                "Reward",
                "Steps",
                "Completed",
                "Deadlock"
            ])


    #Checkpoints

    def log_model(self, path):

        print(f"Model saved to {path}")