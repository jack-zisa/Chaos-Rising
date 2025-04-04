import pygame
from util import constants
from data.objects import entity, stat

class CharacterClass:
    def __init__(self, id: str, sprite_path: str, base_stats: stat.Stats, max_stats: stat.Stats):
        self.id = id
        self.sprite = pygame.transform.scale(pygame.image.load(f'resources/assets/classes/{sprite_path}.png'), (32, 32))
        self.base_stats = base_stats
        self.max_stats = max_stats
    
    def from_json(data: dict) -> 'CharacterClass':
        return CharacterClass(data.get('id', ''), data.get('sprite_path', ''), stat.Stats.from_json(data.get('base_stats', {})), stat.Stats.from_json(data.get('max_stats', {})))

class CharacterController:
    def __init__(self, character: 'Character', game):
        self.character = character
        self.game = game

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
        
        self.character.update_collision()
    
    def attack(self, dt: float, input_manager):
        if pygame.mouse.get_pressed()[0]:
            self.character.attacking = True
            bullet = self.game.data_manager.get_bullet('test').create(pygame.mouse.get_pos(), self.character)
            self.game.entity_manager.add_entity(bullet, self.character.pos)
        else:
            self.character.attacking = False
    
    def control(self, dt: float, input_manager):
        self.move(dt, input_manager)
        self.attack(dt, input_manager)

        keys = pygame.key.get_pressed()
        if keys[pygame.K_LSHIFT]:
            self.character.stats.speed += 1 

    def collide(self, other: entity.Entity):
        if other.group == constants.ENTITY_GROUP_ENEMY:
            self.character.damage(1)

class Character(entity.LivingEntity):
    def __init__(self, collider: pygame.Vector2, clazz: CharacterClass):
        entity.LivingEntity.__init__(self, collider, self.spawn, self.tick, self.render, constants.ENTITY_GROUP_PLAYER, clazz.base_stats.copy())
        self.clazz = clazz
        self.max_stats = clazz.base_stats.copy()
        self.attacking = False
    
    def spawn(self, game, pos: pygame.Vector2):
        self.controller = CharacterController(self, game)
        self.control_func = self.controller.control
        self.collide_func = self.controller.collide
        return entity.Entity.spawn(self, game, pos)
    
    def tick(self):
        pass
    
    def render(self, screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
        screen.blit(self.clazz.sprite, self.pos)

        if self.attacking:
            pygame.draw.line(screen, (255, 255, 255), self.get_center_pos(), pygame.mouse.get_pos(), 2)
        
        if debug:
            health_text = font.render(f'Health: {self.stats.health} / {self.max_stats.health}', True, (255, 255, 255))
            speed_text = font.render(f'Speed: {self.stats.speed} / {self.max_stats.speed}', True, (255, 255, 255))
            screen.blit(health_text, health_text.get_rect(center = (screen.get_width() - (health_text.get_width() / 2), 16)))
            screen.blit(speed_text, speed_text.get_rect(center = (screen.get_width() - (speed_text.get_width() / 2), 48)))

        entity.Entity.render(self, screen, debug)