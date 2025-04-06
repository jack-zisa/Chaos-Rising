import pygame
from objects.entity import entity as en

class Attack:
    def __init__(self, bullet_count: int, arc_gap: float, bullet_id: str):
        self.bullet_count = bullet_count
        self.arc_gap = arc_gap
        self.bullet_id = bullet_id
    
    def attack(self, entity: en.Entity, controller):
        if pygame.mouse.get_pressed()[0] and controller.attack_cooldown <= 0:            
            base_direction = (pygame.Vector2(pygame.mouse.get_pos()) - pygame.Vector2(entity.pos)).normalize()
            base_angle = -self.arc_gap * (self.bullet_count - 1) / 2
            
            for i in range(self.bullet_count):
                angle = base_angle + i * self.arc_gap
                bullet = entity.game.data_manager.get_bullet(self.bullet_id).create(entity, direction=base_direction.rotate(angle))
                entity.game.entity_manager.add_entity(bullet, entity.pos.copy())
            
            controller.attack_cooldown = max(1, 150 / max(1, entity.stats.attack_speed)) # 150 is the fastest
        else:
            controller.attack_cooldown -= 1
    
    def from_json(data: dict) -> 'Attack':
        return Attack(data.get('bullet_count', 1), data.get('arc_gap', 0), data.get('bullet_id', ''))
