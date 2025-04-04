import pygame

keymap: dict = {
    'up': pygame.K_w,
    'down': pygame.K_s,
    'left': pygame.K_a,
    'right': pygame.K_d,
    'debug': pygame.K_F3,
}

def input(set_debug, get_debug):
    keys = pygame.key.get_pressed()
    if keys[keymap['debug']]:
        set_debug(not get_debug())