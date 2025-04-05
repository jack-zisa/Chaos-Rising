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
    status_effect = StatusEffect(id, amplifier, duration, status_effects[id].starter, status_effects[id].applier, status_effects[id].remover)
    status_effect.starter(entity.game.main, entity, status_effect.amplifier, status_effect.duration, status_effect.data)
    entity.status_effects.append(status_effect)

def poison_start(main, entity, amplifier, duration, data):
    pass
def poison(main, entity, amplifier, duration, data):
    entity.stats.health = max(0, entity.stats.health - amplifier)
def poison_end(main, entity, amplifier, duration, data):
    pass

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

def dazed_start(main, entity, amplifier, duration, data):
    data['prev_attack_speed'] = entity.stats.attack_speed
def dazed(main, entity, amplifier, duration, data):
    entity.stats.attack_speed = min(entity.max_stats.attack_speed, entity.stats.attack_speed + amplifier)
def dazed_end(main, entity, amplifier, duration, data):
    entity.stats.attack_speed = data['prev_attack_speed']

def berserk_start(main, entity, amplifier, duration, data):
    data['prev_attack_speed'] = entity.stats.attack_speed
def berserk(main, entity, amplifier, duration, data):
    entity.stats.attack_speed = min(entity.max_stats.attack_speed, entity.stats.attack_speed + amplifier)
def berserk_end(main, entity, amplifier, duration, data):
    entity.stats.attack_speed = data['prev_attack_speed']

status_effects: dict = {
    'poison': StatusEffect('', 0, 0, poison_start, poison, poison_end),
    'regeneration': StatusEffect('', 0, 0, regeneration_start, regeneration, regeneration_end),
    'slowness': StatusEffect('', 0, 0, slowness_start, slowness, slowness_end),
    'speed': StatusEffect('', 0, 0, speed_start, speed_start, speed_end),
    'dazed': StatusEffect('', 0, 0, dazed_start, dazed, dazed_end),
    'berserk': StatusEffect('', 0, 0, berserk_start, berserk, berserk_end),
}