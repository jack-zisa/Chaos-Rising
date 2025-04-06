import math
import pygame
from data.objects.entity import entity
from util import constants

BASE_SIZE: int = 32
BASE_OFFSET_VEC: pygame.Vector2 = pygame.Vector2(BASE_SIZE / 2, BASE_SIZE / 2)

class BulletController:
    def __init__(self, bullet: 'Bullet'):
        self.bullet = bullet
    
    def control(self, dt: float, events):
        forward = self.bullet.direction * self.bullet.speed * dt

        offset = self.bullet.perpendicular.copy() * math.cos((self.bullet.game.main.gametime - self.bullet.spawntime) * self.bullet.frequency) * self.bullet.amplitude * (-1 if self.bullet.index % 2 else 1)

        self.bullet.pos += forward + offset
        
        x, y = self.bullet.direction
        angle = math.atan2(y, x) + self.bullet.arc_speed
        self.bullet.direction = pygame.Vector2(math.cos(angle), math.sin(angle))

        if self.bullet.moving:
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
            self.sprite = pygame.transform.scale(pygame.image.load(f'resources/assets/bullets/{sprite_path}.png'), (BASE_SIZE * scale, BASE_SIZE * scale))
        elif sprite:
            self.sprite = sprite
        else:
            raise ValueError("Either sprite or sprite_path must be provided.")

        self.piercing = piercing
        self.target = None
        self.parent = None
        self.damage = 0
        self.direction = None
        self.index = -1
    
    def tick(self, gametime):
        entity.Entity.tick(self, gametime)
        self.lifetime -= 1
        self.speed += self.acceleration

        if self.lifetime <= 0:
            self.remove()
    
    def render(self, renderer, clock, screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
        screen.blit(self.sprite, self.sprite.get_rect(center=self.pos + BASE_OFFSET_VEC))
        entity.Entity.render(self, renderer, clock, screen, debug)
    
    def create(self, parent: entity.Entity, direction: pygame.Vector2 = None) -> 'Bullet':
        bullet = Bullet(self.id, self.collider, self.lifetime, self.speed, self.acceleration, self.frequency, self.amplitude, self.arc_speed, sprite=self.sprite, scale=self.scale)
        bullet.parent = parent
        bullet.damage = parent.current_item.damage

        if direction is not None and direction.length_squared() != 0:
            bullet.direction = direction.normalize()
            # Rotate sprite to face the direction, accounting for 45° rotation
            bullet.sprite = pygame.transform.rotate(self.sprite, -pygame.Vector2(direction.x, -direction.y).angle_to(pygame.Vector2(1, 0)) - 45)
        else:
            bullet.direction = pygame.Vector2()  # Just a default fallback

        return bullet
    
    def spawn(self, game, uuid, pos: pygame.Vector2) -> 'Bullet':
        self.controller = BulletController(self)
        self.control_func = self.controller.control
        self.collide_func = self.controller.collide

        self.perpendicular = pygame.Vector2(-self.direction.y, self.direction.x).normalize()

        self.spawntime = game.main.gametime
        
        return entity.Entity.spawn(self, game, uuid, pos)

    def from_json(data: dict) -> 'Bullet':
        return Bullet(data.get('id', ''), pygame.Vector2(32, 32), data.get('lifetime', 0), data.get('speed', 0), data.get('acceleration', 0), data.get('frequency', 0), data.get('amplitude', 0), data.get('arc_speed', 0), data.get('sprite_path', ''), scale=data.get('scale', 1))