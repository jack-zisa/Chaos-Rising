import pygame
import data.objects.entity.character as ch

def render(clock: pygame.time.Clock, screen: pygame.surface.Surface, character: ch.Character, font: pygame.font.Font, debug: bool):    
    screen.blit(character.clazz.sprite, (0, 0))

    text = font.render(character.clazz.id, True, (255, 255, 255))
    screen.blit(text, text.get_rect(center = (56,16)))

    width = get_health_bar_width(screen, character)
    pygame.draw.rect(screen, get_health_bar_color(character), ((screen.get_width() / 2) - (width / 2), 10, width, 15))

    if debug:
        fps_text = font.render(f'{int(clock.get_fps())} FPS', True, (255, 255, 255))
        screen.blit(fps_text, fps_text.get_rect(center = (screen.get_width() - (fps_text.get_width() / 2), 30)))

def get_health_bar_width(screen: pygame.surface.Surface, character: ch.Character) -> int:
    width = int(screen.get_width() * 0.625)
    return min(width, int(width * (character.stats.health / character.max_stats.health)))

def get_health_bar_color(character: ch.Character) -> pygame.Vector3:
    ratio = character.stats.health / character.max_stats.health
    
    if ratio > 0.75:
        return (0, 255, 0)
    elif ratio > 0.25:
        return (255, 255, 0)
    else:
        return (255, 0, 0)