import pygame
from objects.entity import stat, entity
from objects.entity.ai import behavior
from util import constants

class EnemyController:
    def __init__(self, enemy: 'Enemy', game):
        self.enemy = enemy
        self.game = game
    
    def control(self, dt: float, events):
        if self.enemy.behavior:
            behavior.behaviors.get(self.enemy.behavior.get('id', ''), '')(self.enemy, dt, self.enemy.behavior) # empty dict for future custom data per-behavior
        if self.enemy.moving:
            self.enemy.update_collision()
    
    def collide(self, other: entity.Entity):
        pass

class Enemy(entity.LivingEntity):
    def __init__(self, id: str, collider: pygame.Vector2, stats: stat.Stats, behavior: dict = {}, sprite_path: str = "", sprite = None, scale: float = 1):
        entity.LivingEntity.__init__(self, collider, self.spawn, self.tick, self.render, constants.ENTITY_GROUP_ENEMY, scale, stats, stats.copy())
        self.id = id
        self.behavior = behavior

        if sprite_path:
            self.sprite = pygame.transform.scale(pygame.image.load(f'resources/assets/enemies/{sprite_path}.png'), (32 * scale, 32 * scale))
        elif sprite:
            self.sprite = sprite
        else:
            raise ValueError("Either sprite or sprite_path must be provided.")

        self.controller = None
        self.control_func = None
        self.collide_func = None
    
    def spawn(self, game, uuid, pos: pygame.Vector2) -> 'Enemy':
        enemy = Enemy(self.id, self.collider, self.stats.copy(), self.behavior, sprite=self.sprite, scale=self.scale)
        enemy.controller = EnemyController(enemy, game)
        enemy.control_func = enemy.controller.control
        enemy.collide_func = enemy.controller.collide
        return entity.Entity.spawn(enemy, game, uuid, pos)

    def tick(self, gametime):
        entity.LivingEntity.tick(self, gametime)
        if self.stats.health <= 0:
            self.remove()

    def render(self, renderer, clock, screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
        screen.blit(self.sprite, self.pos)

        if debug:
            collider_rect = self.get_collider_rect()
            pygame.draw.rect(screen, (0, 255, 0), collider_rect, 1)

        if debug:
            health_text = font.render(f'{self.stats.health}/{self.max_stats.health}', True, (255, 255, 255))
            screen.blit(health_text, health_text.get_rect(center = (self.pos.x + 8, self.pos.y - 8)))

        entity.Entity.render(self, renderer, clock, screen, debug)

    def from_json(data: dict) -> 'Enemy':
        return Enemy(data.get('id', ''), pygame.Vector2(32, 32), stat.Stats.from_json(data.get('stats', {})), data.get('behavior', {}), data.get('sprite_path', ''), scale=data.get('scale', 1))
