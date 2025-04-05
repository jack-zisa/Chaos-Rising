import pygame

class Camera:
    def __init__(self, width: int, height: int):
        self.pos = pygame.Vector2(0, 0)
        self.size = pygame.Vector2(width, height)
