import pygame
import render.renderer as renderer
from game import Game
from data.objects import character as ch

class Main:
    def __init__(self):
        pygame.init()
        pygame.display.set_caption("Chaos Rising")

        self.game = Game(self)
        self.game.data_manager.load()

        self.screen: pygame.surface.Surface = pygame.display.set_mode((640, 360))
        self.clock = pygame.time.Clock()
        self.running = True
        self.debug = False
        self.dt = 0
        self.font = pygame.font.SysFont('Calibri', 24)

        self.character = ch.Character(pygame.Vector2(32, 32), self.game.data_manager.get_character_class('wizard'))
        self.game.entity_manager.add_entity(self.character, pygame.Vector2(self.screen.get_width() / 2, self.screen.get_height() / 2))

    def start(self):
        while self.running:
            self.screen.fill('black')

            self.game.run()

            renderer.render(self.game, self.clock, self.screen, self.character, self.font, self.debug)

            pygame.display.flip()
            self.dt = self.clock.tick(60) / 1000

        pygame.quit()

if __name__ == "__main__":
    Main().start()