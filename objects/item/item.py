import pygame
from objects.entity import stat

class Item:
    def __init__(self, id: str, damage: int, attack, stats: stat.Stats, sprite_path: str = "", sprite = None):
        self.id = id

        if sprite_path:
            self.sprite = pygame.transform.scale(pygame.image.load(f'resources/assets/items/{sprite_path}.png'), (32, 32))
        elif sprite:
            self.sprite = sprite
        else:
            raise ValueError("Either sprite or sprite_path must be provided.")
        
        self.damage = damage
        self.attack = attack
        self.stats = stats
    
    def from_json(data: dict) -> 'Item':
        import objects.attack.attack as attack
        return Item(data.get('id', ''), data.get('damage', 0), attack.Attack.from_json(data.get('attack', {})), stat.Stats.from_json(data.get('stats', {})), data.get('sprite_path', ''))