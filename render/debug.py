import pygame
import data.objects.character as ch

def render(screen: pygame.surface.Surface, character: ch.Character, font: pygame.font.Font):
    health_text = font.render(f'Health: {character.stats.health} / {character.max_stats.health}', True, (255, 255, 255))
    speed_text = font.render(f'Speed: {character.stats.speed} / {character.max_stats.speed}', True, (255, 255, 255))

    screen.blit(health_text, health_text.get_rect(center = (screen.get_width() - (health_text.get_width() / 2), 16)))
    screen.blit(speed_text, speed_text.get_rect(center = (screen.get_width() - (speed_text.get_width() / 2), 48)))
