import pygame
import data.objects.entity.stat as stat
from game import Game

class Entity:
    def __init__(self, collider: pygame.Vector2, spawn_func, tick_func, render_func, group: str, scale: float):
        self.collider = collider
        self.spawn_func = spawn_func
        self.tick_func = tick_func
        self.render_func = render_func
        self.group = group
        self.scale = scale
        self.active = False

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
        return pygame.rect.Rect(self.pos.x, self.pos.y, self.collider.x * self.scale, self.collider.y * self.scale)

    def spawn(self, game: Game, uuid, pos: pygame.Vector2):
        self.game = game
        self.uuid = uuid
        self.pos = pos
        self.active = True
        self.status_effects: list = []
        return self
    
    def update_collision(self):
        self.get_collider_rect().topleft = self.pos
    
    def tick(self, gametime):
        for status_effect in self.status_effects:            
            if gametime % 20 == 0:
                if status_effect.applier is not None:
                    status_effect.applier(self.game.main, self, status_effect.amplifier, status_effect.duration, status_effect.data)
                status_effect.duration -= 1
            
                if status_effect.duration <= 0:
                    if status_effect.remover is not None:
                        status_effect.remover(self.game.main, self, status_effect.amplifier, status_effect.duration, status_effect.data)
                    self.status_effects.remove(status_effect)
    
    def render(self, clock, screen: pygame.surface.Surface, debug: bool):
        if debug:
            pygame.draw.rect(screen, (0, 255, 0), self.get_collider_rect(), 1)
    
    def remove(self):
        self.game.entity_manager.remove_entity(self)

class LivingEntity(Entity):
    def __init__(self, collider: pygame.Vector2, spawn_func, tick_func, render_func, group: str, scale: float, stats: stat.Stats, max_stats: stat.Stats):
        Entity.__init__(self, collider, spawn_func, tick_func, render_func, group, scale)
        self.stats = stats
        self.max_stats = max_stats
    
    def damage(self, amount: int):
        if self.stats.health <= 0 or any(e.id == 'invincible' for e in self.status_effects):
            return
        amount = amount - self.stats.defense
        self.stats.health = max(0, self.stats.health - amount)
    
    def heal(self, amount: int):
        if self.stats.health <= 0 or any(e.id == 'sickened' for e in self.status_effects):
            return
        self.stats.health = min(self.max_stats.health, self.stats.health + amount)
    
    def tick(self, gametime):
        if self.stats.health != self.max_stats.health and gametime % 20 == 0:
            self.heal(int(0.25 * self.stats.vitality))


