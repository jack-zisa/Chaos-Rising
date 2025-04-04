import random
import pygame
from data.objects import stat, entity

class EnemyController:
    def __init__(self, enemy: 'Enemy'):
        self.enemy = enemy
    
    def control(self, dt: float, input_manager):
        self.enemy.pos.x += random.choice([-1, 1]) * self.enemy.stats.speed * 5 * dt
        self.enemy.pos.y += random.choice([-1, 1]) * self.enemy.stats.speed * 5 * dt

        self.enemy.update_collision()
    
    def collide(self, other: entity.Entity):
        pass

class Enemy(entity.LivingEntity):
    def __init__(self, name: str, pos: pygame.Vector2, collider: pygame.Vector2, sprite_path: str, stats: stat.Stats):
        self.name = name
        self.sprite = pygame.transform.scale(pygame.image.load(f'resources/assets/enemies/{sprite_path}.png'), (32, 32))
        self.controller = EnemyController(self)
        entity.LivingEntity.__init__(self, pos, collider, self.tick, self.controller.control, self.render, self.controller.collide, entity.GROUP_ENEMY, stats)
    
    def tick(self):
        if self.stats.health <= 0:
            self.remove()

    def render(self, screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
        screen.blit(self.sprite, self.pos)

        if debug:
            health_text = font.render(f'Enemy Health: {self.stats.health} / 1000', True, (255, 255, 255))
            screen.blit(health_text, health_text.get_rect(center = (screen.get_width() - (health_text.get_width() / 2), 80)))

        entity.Entity.render(self, screen, debug)

    def from_json(data: dict) -> 'Enemy':
        return Enemy(data.get('name', ''), pygame.Vector2(), pygame.Vector2(32, 32), data.get('sprite_path', ''), stat.Stats.from_json(data.get('stats', {})))
