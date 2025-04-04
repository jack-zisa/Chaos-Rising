import pygame
import entity_manager
from data.objects import character as ch, enemy as en
from render import hud, debug

DEBUG = True

def render(screen: pygame.surface.Surface, character: ch.Character, enemy: en.Enemy, font: pygame.font.Font):
    entity_manager.render(screen)
    
    hud.render(screen, character, font)
    
    if DEBUG:
        debug.render(screen, character, font)
