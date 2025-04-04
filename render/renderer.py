import pygame
import entity_manager
from data.objects import character as ch, enemy as en
from render import hud

def render(screen: pygame.surface.Surface, character: ch.Character, font: pygame.font.Font, debug: bool):
    entity_manager.render(screen, font, debug)
    
    hud.render(screen, character, font, debug)