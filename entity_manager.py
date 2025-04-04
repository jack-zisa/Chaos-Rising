import pygame
import input_manager
from data.objects import entity

entities: list[entity.Entity] = []

def add_entity(entity: entity.Entity):
    entities.append(entity)

def render(screen: pygame.surface.Surface):
    [entity.render_func(screen) for entity in entities]

def control(dt, input_manager):
    [entity.control_func(dt, input_manager) for entity in entities]