import string
import pygame
import chat.commands as commands

class CommandManager:
    def __init__(self, main):
        self.main = main
        self.active = False
        self.command = '/'

    def input(self):
        keys = pygame.key.get_pressed()
        if keys[self.main.game.input_manager.keymap['command']]:
            self.active = True
    
    def poll_events(self, event):
        if self.active and event.type == pygame.KEYDOWN:
            if event.key == pygame.K_ESCAPE:
                self.active = False
                self.command = '/'
            elif event.key == pygame.K_BACKSPACE and len(self.command) > 1:
                self.command = self.command[:-1]
            elif event.key == pygame.K_RETURN:
                commands.execute(self.main, self.command)
                self.command = '/'
                self.active = False
            else:
                if event.unicode in string.ascii_letters + string.digits + ' ':
                    self.command += event.unicode

    def render(self, clock: pygame.time.Clock, screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
        if self.active:
            cursor = (pygame.time.get_ticks() // 400) % 2 == 0
            text = '> ' + self.command + ('_' if cursor else '')
            input_box = font.render(text, True, (255, 255, 255))
            screen.blit(input_box, (10, screen.get_height() - input_box.get_height() - 5))