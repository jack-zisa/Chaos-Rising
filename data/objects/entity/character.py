import pygame
from util import constants
from data.objects.entity import entity, stat

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
        self.attack_cooldown = 0

    def move(self, dt: float, events):
        keys = pygame.key.get_pressed()
        if keys[self.game.input_manager.keymap['up']]:
            self.character.pos.y -= self.character.stats.speed * 5 * dt
        if keys[self.game.input_manager.keymap['down']]:
            self.character.pos.y += self.character.stats.speed * 5 * dt
        if keys[self.game.input_manager.keymap['left']]:
            self.character.pos.x -= self.character.stats.speed * 5 * dt
        if keys[self.game.input_manager.keymap['right']]:
            self.character.pos.x += self.character.stats.speed * 5 * dt
        
        self.character.update_collision()
    
    def attack(self, dt: float, events):
        if self.character.current_item is None:
            return
        
        cooldown = max(1, 150 / max(1, self.character.stats.attack_speed)) # 150 is the fastest

        if pygame.mouse.get_pressed()[0] and self.attack_cooldown <= 0:
            self.character.attacking = True
            bullet = self.game.data_manager.get_bullet(self.character.current_item.bullet_id).create(pygame.mouse.get_pos(), self.character)
            self.game.entity_manager.add_entity(bullet, self.character.pos)
            self.attack_cooldown = cooldown
        else:
            self.character.attacking = False
            self.attack_cooldown -= 1
    
    def control(self, dt: float, events):
        if self.game.command_manager.active:
            return

        self.move(dt, events)
        self.attack(dt, events)

    def collide(self, other: entity.Entity):
        if other.group == constants.ENTITY_GROUP_ENEMY:
            self.character.damage(1)

class Character(entity.LivingEntity):
    def __init__(self, collider: pygame.Vector2, clazz: CharacterClass, scale: float = 1):
        entity.LivingEntity.__init__(self, collider, self.spawn, self.tick, self.render, constants.ENTITY_GROUP_PLAYER, scale, clazz.base_stats.copy())
        self.clazz = clazz
        self.clazz.sprite = pygame.transform.scale(self.clazz.sprite, (32 * self.scale, 32 * self.scale))
        self.max_stats = clazz.base_stats.copy()
        self.attacking = False
        self.current_item = None
    
    def spawn(self, game, uuid, pos: pygame.Vector2):
        self.controller = CharacterController(self, game)
        self.control_func = self.controller.control
        self.collide_func = self.controller.collide
        return entity.Entity.spawn(self, game, uuid, pos)
    
    def tick(self, gametime):
        entity.Entity.tick(self, gametime)

        if self.stats.health != self.max_stats.health and gametime % 20 == 0:
            self.stats.health = min(self.max_stats.health, self.stats.health + int(0.24 * self.stats.vitality))
    
    def render(self, clock, screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
        screen.blit(self.clazz.sprite, self.pos)
        
        if debug:
            stats_text = font.render(f'H: {self.stats.health}/{self.max_stats.health},S: {self.stats.speed}/{self.max_stats.speed},AS: {self.stats.attack_speed}/{self.max_stats.attack_speed},D: {self.stats.defense}/{self.max_stats.defense},A: {self.stats.attack}/{self.max_stats.attack},V: {self.stats.vitality}/{self.max_stats.vitality}', True, (255, 255, 255))
            screen.blit(stats_text, stats_text.get_rect(center = (screen.get_width() - (stats_text.get_width() / 2), 10)))

        entity.Entity.render(self, clock, screen, debug)