import torch
import torch.nn as nn
from cnn import CNN

class ActorCritic(nn.Module):

    def __init__(self, num_actions):
        super().__init__()

        self.cnn = CNN()

        self.policy = nn.Sequential(
            nn.Linear(self.cnn.output_features,512),
            nn.ReLU(),
            nn.Linear(512,256),
            nn.ReLU(),
            nn.Linear(256,num_actions)
        )

        self.value = nn.Sequential(
            nn.Linear(self.cnn.output_features,512),
            nn.ReLU(),
            nn.Linear(512,256),
            nn.ReLU(),
            nn.Linear(256,1)
        )


    def forward(self,state):

        features = self.cnn(state)

        logits = self.policy(features)
        value = self.value(features)

        return logits,value
