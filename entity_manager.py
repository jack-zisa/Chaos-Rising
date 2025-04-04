import uuid as u
import pygame

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

    def tick(self):
        [entity.tick_func() for entity in self.get_active_entities().values()]

    def render(self, clock, screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
        [entity.render_func(clock, screen, font, debug) for entity in self.get_active_entities().values()]

    def control(self, dt, input_manager):
        [entity.control_func(dt, input_manager) for entity in self.get_active_entities().values()]

    def collide(self, dt):
        for i, entity_a in enumerate(self.get_active_entities().values()):
            for j, entity_b in enumerate(self.get_active_entities().values()):
                if i == j:
                    continue

                if entity_a.get_collider_rect().colliderect(entity_b.get_collider_rect()):
                    entity_a.collide_func(entity_b)
                    entity_b.collide_func(entity_a)