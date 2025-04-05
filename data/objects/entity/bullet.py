import math
import pygame
from data.objects.entity import entity
from util import constants

class BulletController:
    def __init__(self, bullet: 'Bullet'):
        self.bullet = bullet
    
    def control(self, dt: float, events):
        forward = self.bullet.direction * self.bullet.speed * 5 * dt

        offset = self.bullet.perpendicular.copy() * math.cos((self.bullet.game.main.gametime - self.bullet.spawntime) * self.bullet.frequency) * self.bullet.amplitude

        self.bullet.pos += forward + offset
        
        x, y = self.bullet.direction
        angle = math.atan2(y, x) + self.bullet.arc_speed
        self.bullet.direction = pygame.Vector2(math.cos(angle), math.sin(angle))

        self.bullet.update_collision()
    
    def collide(self, other: entity.Entity):
        if other.group != constants.ENTITY_GROUP_BULLET and other.group != self.bullet.parent.group and isinstance(other, entity.LivingEntity):
            other.damage(self.bullet.damage)

            if not self.bullet.piercing:
                self.bullet.remove()

class Bullet(entity.Entity):
    def __init__(self, id: str, collider: pygame.Vector2, lifetime: int, speed: float, acceleration: float, frequency: float, amplitude: float, arc_speed: float, sprite_path: str = "", sprite = None, piercing = False, scale: float = 1):
        entity.Entity.__init__(self, collider, self.spawn, self.tick, self.render, constants.ENTITY_GROUP_BULLET, scale)
        self.id = id
        self.lifetime = lifetime
        self.speed = speed
        self.acceleration = acceleration
        self.frequency = frequency
        self.amplitude = amplitude
        self.arc_speed = arc_speed

        if sprite_path:
            self.sprite = pygame.transform.scale(pygame.image.load(f'resources/assets/bullets/{sprite_path}.png'), (32 * scale, 32 * scale))
        elif sprite:
            self.sprite = sprite
        else:
            raise ValueError("Either sprite or sprite_path must be provided.")

        self.piercing = piercing
        self.target = None
        self.parent = None
        self.damage = 0
        self.direction = None
    
    def tick(self, gametime):
        entity.Entity.tick(self, gametime)
        self.lifetime -= 1
        self.speed += self.acceleration

        if self.lifetime <= 0:
            self.remove()
    
    def render(self, clock, screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
        screen.blit(self.sprite, self.pos)

        entity.Entity.render(self, clock, screen, debug)
    
    def create(self, target: pygame.Vector2, parent: entity.Entity) -> 'Bullet':
        bullet = Bullet(self.id, self.collider, self.lifetime, self.speed, self.acceleration, self.frequency, self.amplitude, self.arc_speed, sprite=self.sprite, scale=self.scale)
        bullet.target = target
        bullet.parent = parent
        bullet.damage = parent.current_item.damage
        return bullet
    
    def spawn(self, game, uuid, pos: pygame.Vector2) -> 'Bullet':
        self.controller = BulletController(self)
        self.control_func = self.controller.control
        self.collide_func = self.controller.collide

        vector = self.target - pos
        if vector.length_squared() != 0:
            self.direction = vector.normalize()
        else:
            self.direction = pygame.Vector2()
        self.perpendicular = pygame.Vector2(-self.direction.y, self.direction.x).normalize()

        self.spawntime = game.main.gametime
        
        return entity.Entity.spawn(self, game, uuid, pos)

    def from_json(data: dict) -> 'Bullet':
        return Bullet(data.get('id', ''), pygame.Vector2(32, 32), data.get('lifetime', 0), data.get('speed', 0), data.get('acceleration', 0), data.get('frequency', 0), data.get('amplitude', 0), data.get('arc_speed', 0), data.get('sprite_path', ''), scale=data.get('scale', 1))