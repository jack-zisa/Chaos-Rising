import pygame
from data.objects import character as ch, enemy as en
from render import hud
from game import Game

def render(game: Game, clock, screen: pygame.surface.Surface, character: ch.Character, font: pygame.font.Font, debug: bool):
    game.entity_manager.render(clock, screen, font, debug)
    game.command_manager.render(clock, screen, font, debug)
    
    hud.render(clock, screen, character, font, debug)