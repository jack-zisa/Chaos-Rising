import pygame

class EventManager:
    def __init__(self, main):
        self.main = main

    def poll_events(self, events):
        for event in events:
            if event.type == pygame.QUIT:
                self.main.running = False
            
            self.main.game.command_manager.poll_events(event)