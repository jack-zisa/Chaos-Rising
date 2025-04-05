import pygame
from chat.command_manager import CommandManager
from data.data_manager import DataManager
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
    
    def run(self):
        self.event_manager.poll_events(pygame.event.get())

        self.command_manager.input()
        if not self.command_manager.active:
            self.input_manager.input()

        self.entity_manager.tick()
        self.entity_manager.control(self.main.dt)
        self.entity_manager.collide(self.main.dt)
