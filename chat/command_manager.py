import string
import pygame
import chat.commands as commands

class CommandManager:
    def __init__(self, main):
        self.main = main
        self.active = False
        self.command = '/'

    def input(self, events):
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
                if event.unicode in string.ascii_letters + string.digits + ' ' + '_' + '.':
                    self.command += event.unicode

    def render(self, renderer, clock: pygame.time.Clock, screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
        if self.active:
            cmd_text = '> ' + self.command
            cursor = (pygame.time.get_ticks() // 400) % 2 == 0
            text = cmd_text + ('_' if cursor else '')

            x = 10
            y = screen.get_height() - font.get_height() - 5

            for i in range(len(text)):
                char = text[i]
                char_surface = font.render(char, True, (255, 255, 255))
                screen.blit(char_surface, (int(x), y))

                if (char == '_' and i + 1 < len(text) and text[i + 1] == '_' and i < len(cmd_text) - 1):
                    x += char_surface.get_width() + 1
                else:
                    x += char_surface.get_width()