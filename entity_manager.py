import pygame
from data.objects import entity

entities: list[entity.Entity] = []

def add_entity(entity: entity.Entity):
    entities.append(entity)

def remove_entity(entity: entity.Entity):
    entities.remove(entity)

def tick():
    [entity.tick_func() for entity in entities]

def render(screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
    [entity.render_func(screen, font, debug) for entity in entities]

def control(dt, input_manager):
    [entity.control_func(dt, input_manager) for entity in entities]

def collide(dt):
    for i, entity_a in enumerate(entities):
        for j, entity_b in enumerate(entities):
            if i == j:
                continue

            if entity_a.collider.colliderect(entity_b.collider):
                entity_a.collide_func(entity_b)
                entity_b.collide_func(entity_a)