import pygame

def render_tooltip(screen: pygame.surface.Surface, rect, font: pygame.font.Font, text: list):
    mouse_pos = pygame.mouse.get_pos()
    if rect.collidepoint(mouse_pos):
        tooltip_surfaces = [font.render(line, True, (255, 255, 255)) for line in text]
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