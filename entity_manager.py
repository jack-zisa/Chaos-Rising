import pygame
from data.objects import entity

entities: list[entity.Entity] = []

def add_entity(entity: entity.Entity):
    entities.append(entity)

def remove_entity(entity: entity.Entity):
    entities.remove(entity)

def get_active_entities() -> list[entity.Entity]:
    return list(filter(lambda e: e.active, entities))

def tick():
    [entity.tick_func() for entity in get_active_entities()]

def render(screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
    [entity.render_func(screen, font, debug) for entity in get_active_entities()]

def control(dt, input_manager):
    [entity.control_func(dt, input_manager) for entity in get_active_entities()]

def collide(dt):
    for i, entity_a in enumerate(get_active_entities()):
        for j, entity_b in enumerate(get_active_entities()):
            if i == j:
                continue

            if entity_a.get_collider_rect().colliderect(entity_b.get_collider_rect()):
                entity_a.collide_func(entity_b)
                entity_b.collide_func(entity_a)