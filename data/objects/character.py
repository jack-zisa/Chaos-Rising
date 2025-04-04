import pygame
import input_manager
from data.objects import stat, entity

class CharacterClass:
    def __init__(self, name: str, sprite_path: str, base_stats: stat.Stats, max_stats: stat.Stats):
        self.name = name
        self.sprite = pygame.transform.scale(pygame.image.load(f'resources/assets/classes/{sprite_path}.png'), (32, 32))
        self.base_stats = base_stats
        self.max_stats = max_stats
    
    def from_json(data: dict) -> 'CharacterClass':
        return CharacterClass(data.get('name', ''), data.get('sprite_path', ''), stat.Stats.from_json(data.get('base_stats', {})), stat.Stats.from_json(data.get('max_stats', {})))

class CharacterController:
    def __init__(self, character: 'Character'):
        self.character = character

    def move(self, dt: float, input_manager):
        keys = pygame.key.get_pressed()
        if keys[input_manager.keymap['up']]:
            self.character.pos.y -= self.character.stats.speed * 5 * dt
        if keys[input_manager.keymap['down']]:
            self.character.pos.y += self.character.stats.speed * 5 * dt
        if keys[input_manager.keymap['left']]:
            self.character.pos.x -= self.character.stats.speed * 5 * dt
        if keys[input_manager.keymap['right']]:
            self.character.pos.x += self.character.stats.speed * 5 * dt
    
    def attack(self, dt: float, input_manager):
        if pygame.mouse.get_pressed()[0]:
            self.character.attacking = True
        else:
            self.character.attacking = False
    
    def control(self, dt: float, input_manager):
        self.move(dt, input_manager)
        self.attack(dt, input_manager)

        keys = pygame.key.get_pressed()
        if keys[pygame.K_SPACE]:
            self.character.stats.health -= 1
        if keys[pygame.K_LSHIFT]:
            self.character.stats.speed += 1            

class Character(entity.Entity):
    def __init__(self, pos: pygame.Vector2, clazz: CharacterClass):
        self.controller = CharacterController(self)
        entity.Entity.__init__(self, pos, self.controller.control, self.render)
        self.clazz = clazz
        self.stats = clazz.base_stats.copy()
        self.max_stats = clazz.base_stats.copy()
        self.attacking = False
    
    def render(self, screen: pygame.surface.Surface):
        screen.blit(self.clazz.sprite, self.pos)

        if self.attacking:
            pygame.draw.line(screen, (255, 255, 255), self.get_center_pos(), pygame.mouse.get_pos(), 2)