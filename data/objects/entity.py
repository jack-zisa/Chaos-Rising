import pygame
import entity_manager
import data.objects.stat as stat

GROUP_PLAYER: str = 'player'
GROUP_ENEMY: str = 'enemy'
GROUP_BULLET: str = 'bullet'
GROUP_OBSTACLE: str = 'obstacle'

class Entity:
    def __init__(self, active: bool, pos: pygame.Vector2, collider: pygame.Vector2, tick_func, control_func, render_func, collide_func, group: str):
        self.active = active
        self.pos = pos
        self.collider = collider
        self.tick_func = tick_func
        self.control_func = control_func
        self.render_func = render_func
        self.collide_func = collide_func
        self.group = group

    def get_center_pos(self) -> pygame.Vector2:
        return self.pos + pygame.Vector2(16, 16)
    def get_name_pos(self) -> pygame.Vector2:
        return self.pos + pygame.Vector2(16, 40)
    def get_collider_rect(self) -> pygame.rect.Rect:
        return pygame.rect.Rect(self.pos.x, self.pos.y, self.collider.x, self.collider.y)

    def spawn(self, screen: pygame.surface.Surface, pos: pygame.Vector2):
        self.pos = pos
        self.active = True
    
    def update_collision(self):
        self.get_collider_rect().topleft = self.pos
    
    def render(self, screen: pygame.surface.Surface, debug: bool):
        if debug:
            pygame.draw.rect(screen, (0, 255, 0), self.collider, 1)
    
    def remove(self):
        entity_manager.remove_entity(self)

class LivingEntity(Entity):
    def __init__(self, active: bool, pos: pygame.Vector2, collider: pygame.Vector2, tick_func, control_func, render_func, collide_func, group: str, stats: stat.Stats):
        Entity.__init__(self, active, pos, collider, tick_func, control_func, render_func, collide_func, group)
        self.stats = stats
    
    def damage(self, amount: int):
        self.stats.health -= amount