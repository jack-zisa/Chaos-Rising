import pygame
import objects.entity.stat as stat
import objects.item.item as item
import render.tooltip as tooltip
import util.constants as constants
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
        self.moving = False
    
    def get_center_pos(self) -> pygame.Vector2:
        if not self.active:
            return None
        return self.pos + pygame.Vector2(16 * self.scale, 16 * self.scale)

    def get_collider_rect(self) -> pygame.rect.Rect:
        if not self.active:
            return None
        return pygame.rect.Rect(self.pos.x, self.pos.y, self.collider.x * self.scale, self.collider.y * self.scale)

    def spawn(self, game: Game, uuid, pos: pygame.Vector2):
        self.game = game
        self.spawntime = game.main.gametime
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
            
                if status_effect.duration <= 0:
                    if status_effect.remover is not None:
                        status_effect.remover(self.game.main, self, status_effect.amplifier, status_effect.duration, status_effect.data)
                    self.status_effects.remove(status_effect)
                status_effect.duration -= 1

    def render(self, renderer, clock, screen: pygame.surface.Surface, debug: bool):
        if len(self.status_effects) > 0:
            pygame.draw.rect(screen, self.status_effects[0].color, pygame.rect.Rect(self.pos.x - 8, self.pos.y - 8, 8, 8))

        if debug:
            pygame.draw.rect(screen, (0, 255, 0), self.get_collider_rect(), 1)
    
    def remove(self):
        self.game.entity_manager.remove_entity(self)

    def has_effect(self, id) -> bool:
        return any(e.id == id for e in self.status_effects)

class LivingEntity(Entity):
    def __init__(self, collider: pygame.Vector2, spawn_func, tick_func, render_func, group: str, scale: float, stats: stat.Stats, max_stats: stat.Stats):
        Entity.__init__(self, collider, spawn_func, tick_func, render_func, group, scale)
        self.stats = stats
        self.max_stats = max_stats
    
    def damage(self, amount: int):
        if self.stats.health <= 0 or self.has_effect(constants.STATUS_EFFECT_INVINCIBLE):
            return
        defense = self.stats.defense + (20 if self.has_effect(constants.STATUS_EFFECT_ARMORED) else 0)
        amount = amount - (0 if self.has_effect(constants.STATUS_EFFECT_ARMOR_BROKEN) else defense)
        self.stats.health = max(0, self.stats.health - amount)
    
    def heal(self, amount: int):
        if self.stats.health <= 0 or self.has_effect(constants.STATUS_EFFECT_SICKENED):
            return
        if self.has_effect(constants.STATUS_EFFECT_VIVACIOUS):
            amount += 5
        self.stats.health = min(self.max_stats.health, self.stats.health + amount)
    
    def tick(self, gametime: int):
        Entity.tick(self, gametime)
        if self.stats.health != self.max_stats.health and gametime % 20 == 0:
            self.heal(int(0.25 * self.stats.vitality))
    
class ItemEntity(Entity):
    def __init__(self, scale, item: item.Item):
        Entity.__init__(self, None, self.spawn, self.tick, self.render, constants.ENTITY_GROUP_ITEM, scale)
        self.collider = pygame.Vector2(0, 0)
        self.item = item
    
    def tick(self, gametime: int):
        pass
    
    def render(self, renderer, clock, screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
        rect = self.item.sprite.get_rect(topleft=self.pos)
        screen.blit(self.item.sprite, rect.topleft)

        tooltip.render_tooltip(screen, rect, font, [
            f'{self.item.id}', f'Damage: {self.item.damage}'
            ])
    
    def control():
        pass
