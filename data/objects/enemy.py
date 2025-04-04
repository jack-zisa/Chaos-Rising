import random
import pygame
from data.objects import stat, entity

class EnemyController:
    def __init__(self, enemy: 'Enemy'):
        self.enemy = enemy
    
    def control(self, dt: float):        
        self.enemy.pos.x += random.choice([-1, 1]) * self.enemy.stats.speed * 5 * dt
        self.enemy.pos.y += random.choice([-1, 1]) * self.enemy.stats.speed * 5 * dt

class Enemy(entity.Entity):
    def __init__(self, name: str, pos: pygame.Vector2, sprite_path: str, stats: stat.Stats):
        self.name = name
        self.sprite = pygame.transform.scale(pygame.image.load(f'resources/assets/enemies/{sprite_path}.png'), (32, 32))
        self.controller = EnemyController(self)
        entity.Entity.__init__(self, pos, self.controller.control, self.render)
        self.stats = stats
    
    def render(self, screen: pygame.surface.Surface):
        screen.blit(self.sprite, self.pos)

    def from_json(data: dict) -> 'Enemy':
        return Enemy(data.get('name', ''), pygame.Vector2(), data.get('sprite_path', ''), stat.Stats.from_json(data.get('stats', {})))
