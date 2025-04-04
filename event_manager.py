import pygame

class EventManager:
    def __init__(self, main):
        self.main = main

    def poll_events(self, stop):
        global running
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                stop()