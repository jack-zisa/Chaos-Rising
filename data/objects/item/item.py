import pygame
from data.objects.entity import stat

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
    
    def render(self, clock, screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
        sprite_rect = self.sprite.get_rect(topleft=(0, 32))
        screen.blit(self.sprite, sprite_rect.topleft)

        mouse_pos = pygame.mouse.get_pos()
        if sprite_rect.collidepoint(mouse_pos):
            lines = [
                f'{self.id}',
                f'Damage: {self.damage}'
            ]

            tooltip_surfaces = [font.render(line, True, (255, 255, 255)) for line in lines]
            max_width = max(surf.get_width() for surf in tooltip_surfaces)
            total_height = sum(surf.get_height() for surf in tooltip_surfaces)

            tooltip_bg = pygame.Surface((max_width + 10, total_height + 10))
            tooltip_bg.fill((100, 100, 100))
            tooltip_bg.set_alpha(200)

            tooltip_pos = (mouse_pos[0] + 12, mouse_pos[1] + 12)
            screen.blit(tooltip_bg, tooltip_pos)

            y_offset = tooltip_pos[1] + 5
            for surf in tooltip_surfaces:
                screen.blit(surf, (tooltip_pos[0] + 5, y_offset))
                y_offset += surf.get_height()

    def from_json(data: dict) -> 'Item':
        import data.objects.attack.attack as attack
        return Item(data.get('id', ''), data.get('damage', 0), attack.Attack.from_json(data.get('attack', {})), stat.Stats.from_json(data.get('stats', {})), data.get('sprite_path', ''))
