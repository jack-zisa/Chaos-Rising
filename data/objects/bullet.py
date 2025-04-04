import pygame
from data.objects import entity

class BulletController:
    def __init__(self, bullet: 'Bullet'):
        self.bullet = bullet
    
    def control(self, dt: float, input_manager):
        self.bullet.pos = self.bullet.pos.move_towards(self.bullet.target, 10)

        self.bullet.update_collision()
    
    def collide(self, other: entity.Entity):
        if other.group != entity.GROUP_BULLET:
            if other.group != self.bullet.parent.group and isinstance(other, entity.LivingEntity):
                other.damage(1)

class Bullet(entity.Entity):
    def __init__(self, pos: pygame.Vector2, target: pygame.Vector2, collider: pygame.Vector2, parent: entity.Entity, sprite_path: str, lifetime: int):
        self.target = target
        self.parent = parent
        self.sprite = pygame.transform.scale(pygame.image.load(f'resources/assets/bullets/{sprite_path}.png'), (32, 32))
        self.lifetime = lifetime
        self.controller = BulletController(self)
        entity.Entity.__init__(self, pos, collider, self.tick, self.controller.control, self.render, self.controller.collide, entity.GROUP_BULLET)
    
    def tick(self):
        self.lifetime -= 1

        if self.lifetime <= 0:
            self.remove()
    
    def render(self, screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
        screen.blit(self.sprite, self.pos)

        entity.Entity.render(self, screen, debug)