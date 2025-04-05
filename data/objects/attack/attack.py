import math
import pygame
from data.objects.entity import entity as en

class Attack:
    def __init__(self, bullet_count: int, arc_gap: float, bullet_id: str):
        self.bullet_count = bullet_count
        self.arc_gap = arc_gap
        self.bullet_id = bullet_id
    
    def attack(self, entity: en.Entity, controller):
        cooldown = max(1, 150 / max(1, entity.stats.attack_speed))

        if pygame.mouse.get_pressed()[0] and controller.attack_cooldown <= 0:
            origin = entity.pos.copy()
            mouse_pos = pygame.mouse.get_pos()
            base_direction = (pygame.Vector2(mouse_pos) - origin).normalize()

            start_angle = -self.arc_gap * (self.bullet_count - 1) / 2

            for i in range(self.bullet_count):
                angle = start_angle + i * self.arc_gap
                rotated_dir = base_direction.rotate(angle)
                bullet = entity.game.data_manager.get_bullet(self.bullet_id).create(origin + rotated_dir * 10, entity, direction = rotated_dir)
                bullet.index = i
                entity.game.entity_manager.add_entity(bullet, origin)

            controller.attack_cooldown = cooldown
        else:
            controller.attack_cooldown -= 1
    
    def from_json(data: dict) -> 'Attack':
        return Attack(data.get('bullet_count', 1), data.get('arc_gap', 0), data.get('bullet_id', ''))
