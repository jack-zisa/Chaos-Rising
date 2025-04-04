import random
import pygame
from data.objects import stat, entity
from util import constants

class EnemyController:
    def __init__(self, enemy: 'Enemy', game):
        self.enemy = enemy
        self.game = game
    
    def control(self, dt: float, input_manager):
        self.enemy.pos.x += random.choice([-1, 1]) * self.enemy.stats.speed * 5 * dt
        self.enemy.pos.y += random.choice([-1, 1]) * self.enemy.stats.speed * 5 * dt

        self.enemy.update_collision()
    
    def collide(self, other: entity.Entity):
        pass

class Enemy(entity.LivingEntity):
    def __init__(self, id: str, collider: pygame.Vector2, sprite_path: str, stats: stat.Stats):
        self.id = id
        self.sprite = pygame.transform.scale(pygame.image.load(f'resources/assets/enemies/{sprite_path}.png'), (32, 32))
        entity.LivingEntity.__init__(self, collider, self.spawn, self.tick, self.render, constants.ENTITY_GROUP_ENEMY, stats)
    
    def spawn(self, game, uuid, pos: pygame.Vector2) -> 'Enemy':
        self.controller = EnemyController(self, game)
        self.control_func = self.controller.control
        self.collide_func = self.controller.collide
        return entity.Entity.spawn(self, game, uuid, pos)

    def tick(self):
        if self.stats.health <= 0:
            self.remove()

    def render(self, clock, screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
        screen.blit(self.sprite, self.pos)

        if debug:
            health_text = font.render(f'Enemy Health: {self.stats.health} / 1000', True, (255, 255, 255))
            screen.blit(health_text, health_text.get_rect(center = (screen.get_width() - (health_text.get_width() / 2), 80)))

        entity.Entity.render(self, clock, screen, debug)

    def from_json(data: dict) -> 'Enemy':
        return Enemy(data.get('id', ''), pygame.Vector2(32, 32), data.get('sprite_path', ''), stat.Stats.from_json(data.get('stats', {})))
