import random
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
            'spawn': pygame.K_SPACE,
        }

    def input(self):
        keys = pygame.key.get_pressed()
        if keys[self.keymap['debug']]:
            self.main.debug = not self.main.debug

        if keys[self.keymap['spawn']]:
            enemy = self.main.game.data_manager.get_enemy('test')
            self.main.game.entity_manager.add_entity(enemy, pygame.Vector2(random.randint(0, self.main.screen.get_width()), random.randint(0, self.main.screen.get_height())))
