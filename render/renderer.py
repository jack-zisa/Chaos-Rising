import pygame
from data.objects.entity import character as ch
from render import hud
from game import Game

class Renderer():
    def __init__(self, main):
        self.main = main

    def render(self, game: Game, clock: pygame.time.Clock, screen: pygame.surface.Surface, character: ch.Character, font: pygame.font.Font, debug: bool):        
        game.entity_manager.render(self, clock, screen, font, debug)
        game.command_manager.render(self, clock, screen, font, debug)
        
        hud.render(self, clock, screen, character, font, debug)