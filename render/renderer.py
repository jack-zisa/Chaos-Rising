import typing
import pygame
from data.objects.entity import character as ch
from render import hud
from game import Game

class Renderer():
    def __init__(self, main):
        self.main = main
    
    def blit(self, screen: pygame.Surface, source: pygame.Surface, dest: typing.Union[pygame.Vector2, pygame.Rect], area: typing.Optional[pygame.Rect] = None, special_flags: int = 0) -> pygame.Rect:
        if isinstance(dest, pygame.Rect):
            dest = dest.move(-self.main.camera.pos.x, -self.main.camera.pos.y)
        else:
            dest -= self.main.camera.pos

        return screen.blit(source, dest, area, special_flags)

    def render(self, game: Game, clock: pygame.time.Clock, screen: pygame.surface.Surface, character: ch.Character, font: pygame.font.Font, debug: bool):
        self.main.camera.pos.x = character.pos.x - self.main.camera.size.x / 2
        self.main.camera.pos.y = character.pos.y - self.main.camera.size.y / 2
        
        game.entity_manager.render(self, clock, screen, font, debug)
        game.command_manager.render(self, clock, screen, font, debug)
        
        hud.render(self, clock, screen, character, font, debug)