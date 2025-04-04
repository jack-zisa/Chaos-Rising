import pygame
import entity_manager
from data.objects import character as ch, enemy as en
from render import hud, debug as dbg

def render(screen: pygame.surface.Surface, character: ch.Character, enemy: en.Enemy, font: pygame.font.Font, debug: bool):
    entity_manager.render(screen)
    
    hud.render(screen, character, font)
    
    if debug:
        dbg.render(screen, character, font)
