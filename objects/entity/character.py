import pygame
from util import constants
from objects.entity import entity, stat

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

        if not keys[self.game.input_manager.keymap['up']] and not keys[self.game.input_manager.keymap['down']] and not keys[self.game.input_manager.keymap['left']] and not keys[self.game.input_manager.keymap['right']]:
            self.character.moving = False
        else:
            if keys[self.game.input_manager.keymap['up']]:
                self.character.pos.y -= self.character.stats.speed * dt
                self.character.moving = True
            if keys[self.game.input_manager.keymap['down']]:
                self.character.pos.y += self.character.stats.speed * dt
                self.character.moving = True
            if keys[self.game.input_manager.keymap['left']]:
                self.character.pos.x -= self.character.stats.speed * dt
                self.character.moving = True
            if keys[self.game.input_manager.keymap['right']]:
                self.character.pos.x += self.character.stats.speed * dt
                self.character.moving = True

            self.character.update_collision() # self.character.moving is always True here
    
    def attack(self, dt: float, events):
        if self.character.current_item is None:
            return
        
        self.character.current_item.item.attack.attack(self.character, self)
    
    def control(self, dt: float, events):
        if self.game.command_manager.active:
            return

        if not self.character.has_effect('paralyzed') or not self.character.has_effect('frozen') or not self.character.has_effect('petrified'):
            self.move(dt, events)
        if not self.character.has_effect('stunned') or not self.character.has_effect('petrified'):
            self.attack(dt, events)

    def collide(self, other: entity.Entity):
        if other.group == constants.ENTITY_GROUP_ENEMY:
            self.character.damage(1)

class Character(entity.LivingEntity):
    def __init__(self, collider: pygame.Vector2, clazz: CharacterClass, scale: float = 1):
        entity.LivingEntity.__init__(self, collider, self.spawn, self.tick, self.render, constants.ENTITY_GROUP_PLAYER, scale, clazz.base_stats.copy(), clazz.base_stats.copy())
        self.clazz = clazz
        self.clazz.sprite = pygame.transform.scale(self.clazz.sprite, (32 * self.scale, 32 * self.scale))
        self.attacking = False
        self.current_item = None
    
    def spawn(self, game, uuid, pos: pygame.Vector2):
        self.controller = CharacterController(self, game)
        self.control_func = self.controller.control
        self.collide_func = self.controller.collide
        return entity.Entity.spawn(self, game, uuid, pos)
    
    def tick(self, gametime):
        entity.LivingEntity.tick(self, gametime)
    
    def render(self, renderer, clock, screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
        screen.blit(self.clazz.sprite, self.pos)

        if self.current_item is not None:
            self.current_item.render(renderer, clock, screen, font, debug)
        
        if debug:
            stats_text = font.render(f'H: {self.stats.health}/{self.max_stats.health},S: {self.stats.speed}/{self.max_stats.speed},AS: {self.stats.attack_speed}/{self.max_stats.attack_speed},D: {self.stats.defense}/{self.max_stats.defense},A: {self.stats.attack}/{self.max_stats.attack},V: {self.stats.vitality}/{self.max_stats.vitality}', True, (255, 255, 255))
            screen.blit(stats_text, stats_text.get_rect(center = (screen.get_width() - (stats_text.get_width() / 2), 10)))

        entity.Entity.render(self, renderer, clock, screen, debug)