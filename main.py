import pygame
import render.renderer as renderer
from game import Game
from objects.entity import character as ch
from render import camera

class Main:
    def __init__(self):
        pygame.init()
        pygame.display.set_caption('Chaos Rising')

        self.game = Game(self)
        self.game.data_manager.load()

        self.screen: pygame.surface.Surface = pygame.display.set_mode((640, 360))
        self.clock = pygame.time.Clock()
        self.running = True
        self.debug = False
        self.dt = 0
        self.font = pygame.font.SysFont('Calibri', 20)
        self.gametime = 0

        self.renderer = renderer.Renderer(self)
        self.camera = camera.Camera(self.screen.get_width(), self.screen.get_height())

        self.character = ch.Character(pygame.Vector2(32, 32), self.game.data_manager.get_character_class('wizard'))
        self.game.entity_manager.add_entity(self.character, pygame.Vector2(0, 0))

    def start(self):
        while self.running:
            self.screen.fill('black')

            self.gametime += 1
            self.game.run(self.gametime)

            self.renderer.render(self.game, self.clock, self.screen, self.character, self.font, self.debug)

            pygame.display.flip()
            self.dt = self.clock.tick(60) / 1000

        pygame.quit()

if __name__ == '__main__':
    Main().start()