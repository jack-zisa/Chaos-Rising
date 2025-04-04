import pygame

class Entity:
    def __init__(self, pos: pygame.Vector2, control_func, render_func):
        self.pos = pos
        self.control_func = control_func
        self.render_func = render_func

    def get_center_pos(self) -> pygame.Vector2:
        return self.pos + pygame.Vector2(16, 16)
    def get_name_pos(self) -> pygame.Vector2:
        return self.pos + pygame.Vector2(16, 40)

    def spawn(self, screen: pygame.surface.Surface, pos: pygame.Vector2):
        self.pos = pos
