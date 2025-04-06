import pygame
from chat.command_manager import CommandManager
from data_manager import DataManager
from entity_manager import EntityManager
from event_manager import EventManager
from input_manager import InputManager

class Game:
    def __init__(self, main):
        self.main = main
        self.command_manager = CommandManager(main)
        self.data_manager = DataManager(main)
        self.entity_manager = EntityManager(main)
        self.event_manager = EventManager(main)
        self.input_manager = InputManager(main)
    
    def run(self, gametime):
        events = pygame.event.get()
        self.event_manager.poll_events(events)

        self.command_manager.input(events)
        if not self.command_manager.active:
            self.input_manager.input(events)

        self.entity_manager.tick(gametime)
        self.entity_manager.control(self.main.dt, events)
        self.entity_manager.collide(self.main.dt)
