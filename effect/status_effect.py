class StatusEffect():
    def __init__(self, id: str, amplifier: int, duration: int, starter, applier, remover):
        self.id = id
        self.amplifier = amplifier
        self.duration = duration
        self.starter = starter
        self.applier = applier
        self.remover = remover
        self.data = {}

def apply(id: str, amplifier: int, duration: int, entity):
    if id not in status_effects or not entity.active:
        return
    status_effect = StatusEffect(id, amplifier, duration, status_effect_starts[id], status_effects[id], status_effect_ends[id])
    status_effect.starter(entity.game.main, entity, status_effect.amplifier, status_effect.duration, status_effect.data)
    entity.status_effects.append(status_effect)

def regeneration_start(main, entity, amplifier, duration, data):
    pass
def regeneration(main, entity, amplifier, duration, data):
    entity.stats.health = min(entity.max_stats.health, entity.stats.health + amplifier)
def regeneration_end(main, entity, amplifier, duration, data):
    pass

def slowness_start(main, entity, amplifier, duration, data):
    data['prev_speed'] = entity.stats.speed
def slowness(main, entity, amplifier, duration, data):
    entity.stats.speed = max(0, entity.stats.speed - amplifier)
def slowness_end(main, entity, amplifier, duration, data):
    entity.stats.speed = data['prev_speed']

def speed_start(main, entity, amplifier, duration, data):
    data['prev_speed'] = entity.stats.speed
def speed(main, entity, amplifier, duration, data):
    entity.stats.speed = min(entity.max_stats.speed, entity.stats.speed + amplifier)
def speed_end(main, entity, amplifier, duration, data):
    entity.stats.speed = data['prev_speed']

status_effect_starts: dict = {
    'regeneration': regeneration_start,
    'slowness': slowness_start,
    'speed': speed_start,
}
status_effects: dict = {
    'regeneration': regeneration,
    'slowness': slowness,
    'speed': speed,
}
status_effect_ends: dict = {
    'regeneration': regeneration_end,
    'slowness': slowness_end,
    'speed': speed_end,
}