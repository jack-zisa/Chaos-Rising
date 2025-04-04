import pygame

class EntityManager:
    def __init__(self, main):
        self.main = main
        self.entities: list = []

    def add_entity(self, entity, pos: pygame.Vector2):
        self.entities.append(entity.spawn_func(self.main.game, pos))

    def remove_entity(self, entity):
        self.entities.remove(entity)

    def get_active_entities(self) -> list:
        return list(filter(lambda e: e.active, self.entities))

    def tick(self):
        [entity.tick_func() for entity in self.get_active_entities()]

    def render(self, screen: pygame.surface.Surface, font: pygame.font.Font, debug: bool):
        [entity.render_func(screen, font, debug) for entity in self.get_active_entities()]

    def control(self, dt, input_manager):
        [entity.control_func(dt, input_manager) for entity in self.get_active_entities()]

    def collide(self, dt):
        for i, entity_a in enumerate(self.get_active_entities()):
            for j, entity_b in enumerate(self.get_active_entities()):
                if i == j:
                    continue

                if entity_a.get_collider_rect().colliderect(entity_b.get_collider_rect()):
                    entity_a.collide_func(entity_b)
                    entity_b.collide_func(entity_a)