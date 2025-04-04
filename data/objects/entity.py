import pygame
import data.objects.stat as stat
from game import Game

class Entity:
    def __init__(self, collider: pygame.Vector2, spawn_func, tick_func, render_func, group: str):
        self.collider = collider
        self.spawn_func = spawn_func
        self.tick_func = tick_func
        self.render_func = render_func
        self.group = group

    def get_center_pos(self) -> pygame.Vector2:
        if not self.active:
            return None
        return self.pos + pygame.Vector2(16, 16)
    def get_name_pos(self) -> pygame.Vector2:
        if not self.active:
            return None
        return self.pos + pygame.Vector2(16, 40)
    def get_collider_rect(self) -> pygame.rect.Rect:
        if not self.active:
            return None
        return pygame.rect.Rect(self.pos.x, self.pos.y, self.collider.x, self.collider.y)

    def spawn(self, game: Game, pos: pygame.Vector2):
        self.game = game
        self.pos = pos
        self.active = True
        return self
    
    def update_collision(self):
        self.get_collider_rect().topleft = self.pos
    
    def render(self, screen: pygame.surface.Surface, debug: bool):
        if debug:
            pygame.draw.rect(screen, (0, 255, 0), self.get_collider_rect(), 1)
    
    def remove(self):
        self.game.entity_manager.remove_entity(self)

class LivingEntity(Entity):
    def __init__(self, collider: pygame.Vector2, spawn_func, tick_func, render_func, group: str, stats: stat.Stats):
        Entity.__init__(self, collider, spawn_func, tick_func, render_func, group)
        self.stats = stats
    
    def damage(self, amount: int):
        self.stats.health -= amount