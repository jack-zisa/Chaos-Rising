import pygame
from data.objects.entity import entity
from util import constants

class BulletController:
    def __init__(self, bullet: 'Bullet'):
        self.bullet = bullet
    
    def control(self, dt: float, events):
        self.bullet.pos = self.bullet.pos.move_towards(self.bullet.target, 10)
        
        self.bullet.update_collision()
    
    def collide(self, other: entity.Entity):
        if other.group != constants.ENTITY_GROUP_BULLET and other.group != self.bullet.parent.group and isinstance(other, entity.LivingEntity):
            other.damage(self.bullet.damage)

            if not self.bullet.piercing:
                self.bullet.remove()

class Bullet(entity.Entity):
    def __init__(self, id: str, collider: pygame.Vector2, lifetime: int, sprite_path: str = "", sprite = None, piercing = False):
        self.id = id
        self.lifetime = lifetime

        if sprite_path:
            self.sprite = pygame.transform.scale(pygame.image.load(f'resources/assets/bullets/{sprite_path}.png'), (32, 32))
        elif sprite:
            self.sprite = sprite
        else:
            raise ValueError("Either sprite or sprite_path must be provided.")

        self.piercing = piercing
        self.target = None
        self.parent = None
        self.damage = 0
        entity.Entity.__init__(self, collider, self.spawn, self.tick, self.render, constants.ENTITY_GROUP_BULLET)
    
    def tick(self, gametime):
        entity.Entity.tick(self, gametime)
        self.lifetime -= 1

        if self.lifetime <= 0:
            self.remove()
    
    def render(self, clock, screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
        screen.blit(self.sprite, self.pos)

        entity.Entity.render(self, clock, screen, debug)
    
    def create(self, target: pygame.Vector2, parent: entity.Entity) -> 'Bullet':
        bullet = Bullet(self.id, self.collider, self.lifetime, sprite=self.sprite)
        bullet.target = target
        bullet.parent = parent
        bullet.damage = parent.current_item.damage
        return bullet
    
    def spawn(self, game, uuid, pos: pygame.Vector2) -> 'Bullet':
        self.controller = BulletController(self)
        self.control_func = self.controller.control
        self.collide_func = self.controller.collide
        return entity.Entity.spawn(self, game, uuid, pos)

    def from_json(data: dict) -> 'Bullet':
        return Bullet(data.get('id', ''), pygame.Vector2(32, 32), data.get('lifetime', 0), data.get('sprite_path', ''))