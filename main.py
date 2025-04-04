import random
import pygame
import entity_manager
import render.renderer as renderer
import data.data_loader as data_loader
from data.objects import character as ch, enemy as en, stat

pygame.init()
pygame.display.set_caption("Chaos Rising")

data_loader.load()

screen: pygame.surface.Surface = pygame.display.set_mode((1280, 720))
clock = pygame.time.Clock()
running = True
dt = 0
font = pygame.font.SysFont('Calibri', 24)

character = ch.Character(pygame.Vector2(), data_loader.get_character_class('test'))
character.spawn(screen, pygame.Vector2(screen.get_width() / 2, screen.get_height() / 2))
entity_manager.add_entity(character)

enemy = data_loader.get_enemy('test')
enemy.spawn(screen, pygame.Vector2(random.randint(0, screen.get_width()), random.randint(0, screen.get_height())))
entity_manager.add_entity(enemy)

def poll_events():
    global running
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False

while running:
    poll_events()

    screen.fill("black")

    renderer.render(screen, character, enemy, font)

    entity_manager.control(dt)

    pygame.display.flip()
    dt = clock.tick(60) / 1000

pygame.quit()