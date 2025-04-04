import pygame
from data.objects import entity
from util import constants

class BulletController:
    def __init__(self, bullet: 'Bullet'):
        self.bullet = bullet
    
    def control(self, dt: float, input_manager):
        self.bullet.pos = self.bullet.pos.move_towards(self.bullet.target, 10)
        
        self.bullet.update_collision()
    
    def collide(self, other: entity.Entity):
        if other.group != constants.ENTITY_GROUP_BULLET:
            if other.group != self.bullet.parent.group and isinstance(other, entity.LivingEntity):
                other.damage(1)

class Bullet(entity.Entity):
    def __init__(self, id: str, collider: pygame.Vector2, sprite_path: str, lifetime: int):
        self.id = id
        self.sprite = pygame.transform.scale(pygame.image.load(f'resources/assets/bullets/{sprite_path}.png'), (32, 32))
        self.lifetime = lifetime
        entity.Entity.__init__(self, collider, self.spawn, self.tick, self.render, constants.ENTITY_GROUP_BULLET)
    
    def tick(self):
        self.lifetime -= 1

        if self.lifetime <= 0:
            self.remove()
    
    def render(self, clock, screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
        screen.blit(self.sprite, self.pos)

        entity.Entity.render(self, clock, screen, debug)
    
    def create(self, target: pygame.Vector2, parent: entity.Entity) -> 'Bullet':
        self.target = target
        self.parent = parent     
        return self
    
    def spawn(self, game, uuid, pos: pygame.Vector2) -> 'Bullet':
        self.game = game
        self.pos = pos
        self.controller = BulletController(self)
        self.control_func = self.controller.control
        self.collide_func = self.controller.collide
        return entity.Entity.spawn(self, game, uuid, pos)

    def from_json(data: dict) -> 'Bullet':
        return Bullet(data.get('id', ''), pygame.Vector2(32, 32), data.get('sprite_path', ''), data.get('lifetime', 0))