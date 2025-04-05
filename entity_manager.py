import uuid as u
import pygame
from collections import defaultdict

class EntityManager:
    def __init__(self, main):
        self.main = main
        self.entities: dict = {}

    def add_entity(self, template, pos: pygame.Vector2):
        uuid = u.uuid1()
        entity = template.spawn_func(self.main.game, uuid, pos)
        self.entities[uuid] = entity
        return entity

    def remove_entity(self, entity) -> bool:
        if entity.uuid in self.entities:
            del self.entities[entity.uuid]
            return True
        return False

    def get_active_entities(self) -> dict:
        return {key: entity for key, entity in self.entities.items() if entity is not None and entity.active}

    def tick(self, gametime):
        [entity.tick_func(gametime) for entity in self.get_active_entities().values()]

    def render(self, clock: pygame.time.Clock, screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
        [entity.render_func(clock, screen, font, debug) for entity in self.get_active_entities().values()]

    def control(self, dt, events):
        [entity.control_func(dt, events) for entity in self.get_active_entities().values()]

    def get_cell(self, pos):
        cell_size = 64
        return (int(pos.x // cell_size), int(pos.y // cell_size))

    def collide(self, dt):
        grid = defaultdict(list)
    
        for entity in self.get_active_entities().values():
            cell = self.get_cell(entity.pos)
            grid[cell].append(entity)

        for cell, entities in grid.items():
            neighbor_cells = [
                (cell[0] + dx, cell[1] + dy)
                for dx in [-1, 0, 1]
                for dy in [-1, 0, 1]
            ]
            for c in neighbor_cells:
                for a in entities:
                    for b in grid.get(c, []):
                        if a is b:
                            continue
                        if a.pos.distance_to(b.pos) < 32:
                            if a.get_collider_rect().colliderect(b.get_collider_rect()):
                                a.collide_func(b)
                                b.collide_func(a)