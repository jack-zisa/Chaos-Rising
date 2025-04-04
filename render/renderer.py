import pygame
from data.objects import character as ch, enemy as en
from render import hud
from game import Game

def render(game: Game, screen: pygame.surface.Surface, character: ch.Character, font: pygame.font.Font, debug: bool):
    game.entity_manager.render(screen, font, debug)
    
    hud.render(screen, character, font, debug)