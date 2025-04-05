class StatusEffect():
    def __init__(self, id: str, amplifier: int, duration: int, applier):
        self.id = id
        self.amplifier = amplifier
        self.duration = duration
        self.applier = applier

def apply(id: str, amplifier: int, duration: int, entity):
    if id not in status_effects or not entity.active:
        return
    entity.status_effects.append(StatusEffect(id, amplifier, duration, status_effects[id]))

def regeneration(main, entity, amplifier, duration):
    entity.stats.health = min(entity.max_stats.health, entity.stats.health + amplifier)

status_effects: dict = {
    'regeneration': regeneration,
}