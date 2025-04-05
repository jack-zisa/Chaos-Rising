import pygame

class InputManager:
    def __init__(self, main):
        self.main = main
        self.keymap: dict = {
            'up': pygame.K_w,
            'down': pygame.K_s,
            'left': pygame.K_a,
            'right': pygame.K_d,
            'debug': pygame.K_F3,
            'command': pygame.K_SLASH,
        }

    def input(self):
        keys = pygame.key.get_pressed()
        if keys[self.keymap['debug']]:
            self.main.debug = not self.main.debug