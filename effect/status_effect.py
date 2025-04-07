import pygame
import objects.entity.entity as en
import util.constants as constants

class StatusEffect():
    def __init__(self, color, amplifier: int, duration: int, id: str = '', starter = None, applier = None, remover = None):
        self.color = color
        self.amplifier = amplifier
        self.duration = duration
        self.id = id
        self.starter = starter
        self.applier = applier
        self.remover = remover
        self.data = {}

def apply(id: str, amplifier: int, duration: int, entity):
    if id not in status_effects or not entity.active:
        return
    status_effect = StatusEffect(status_effects[id].color, amplifier, duration, id, status_effects[id].starter, status_effects[id].applier, status_effects[id].remover)
    if status_effect.starter is not None:
        status_effect.starter(entity.game.main, entity, status_effect.amplifier, status_effect.duration, status_effect.data)
    entity.status_effects.append(status_effect)

def poison(main, entity, amplifier, duration, data):
    if isinstance(entity, en.LivingEntity):
        entity.damage(0.25 * amplifier)

def regeneration(main, entity, amplifier, duration, data):
    if isinstance(entity, en.LivingEntity):
        entity.heal(0.25 * amplifier)

def slowness_start(main, entity, amplifier, duration, data):
    data['prev_speed'] = entity.stats.speed
def slowness(main, entity, amplifier, duration, data):
    entity.stats.speed = max(0, entity.stats.speed - amplifier)
def slowness_end(main, entity, amplifier, duration, data):
    pass
    #entity.stats.speed = data['prev_speed']

def speed_start(main, entity, amplifier, duration, data):
    data['prev_speed'] = entity.stats.speed
def speed(main, entity, amplifier, duration, data):
    entity.stats.speed = min(entity.max_stats.speed, entity.stats.speed + amplifier)
def speed_end(main, entity, amplifier, duration, data):
    entity.stats.speed = data['prev_speed']

def dazed_start(main, entity, amplifier, duration, data):
    data['prev_attack_speed'] = entity.stats.attack_speed
def dazed(main, entity, amplifier, duration, data):
    entity.stats.attack_speed = max(0, entity.stats.attack_speed - amplifier)
def dazed_end(main, entity, amplifier, duration, data):
    entity.stats.attack_speed = data['prev_attack_speed']

def berserk_start(main, entity, amplifier, duration, data):
    data['prev_attack_speed'] = entity.stats.attack_speed
def berserk(main, entity, amplifier, duration, data):
    entity.stats.attack_speed = min(entity.max_stats.attack_speed, entity.stats.attack_speed + amplifier)
def berserk_end(main, entity, amplifier, duration, data):
    entity.stats.attack_speed = data['prev_attack_speed']

def health_boost_start(main, entity, amplifier, duration, data):
    data['prev_health'] = entity.stats.health
def health_boost(main, entity, amplifier, duration, data):
    entity.stats.health = min(entity.max_stats.health, entity.stats.health + (amplifier * 10))
def health_boost_end(main, entity, amplifier, duration, data):
    entity.stats.health = data['prev_health']

def cursed_start(main, entity, amplifier, duration, data):
    data['prev_health'] = entity.stats.health
def cursed(main, entity, amplifier, duration, data):
    entity.stats.health = max(0, entity.stats.health - (amplifier * 10))
def cursed_end(main, entity, amplifier, duration, data):
    entity.stats.health = data['prev_health']

def enlarged_start(main, entity, amplifier, duration, data):
    data['prev_scale'] = entity.scale
def enlarged(main, entity, amplifier, duration, data):
    entity.scale = amplifier
def enlarged_end(main, entity, amplifier, duration, data):
    entity.scale = data['prev_scale']

def minified_start(main, entity, amplifier, duration, data):
    data['prev_scale'] = entity.scale
def minified(main, entity, amplifier, duration, data):
    entity.scale = amplifier
def minified_end(main, entity, amplifier, duration, data):
    entity.scale = data['prev_scale']

status_effects: dict = {
    constants.STATUS_EFFECT_POISON: StatusEffect((50, 255, 0), 0, 0, applier=poison),
    constants.STATUS_EFFECT_BURNED: StatusEffect((255, 50, 50), 0, 0, applier=poison),
    constants.STATUS_EFFECT_REGENERATION: StatusEffect((255, 0, 50), 0, 0, applier=regeneration),
    constants.STATUS_EFFECT_SLOWNESS: StatusEffect((100, 0, 0), 0, 0, slowness_start, slowness, slowness_end),
    constants.STATUS_EFFECT_PARALYZED: StatusEffect((255, 255, 0), 0, 0),
    constants.STATUS_EFFECT_FROZEN: StatusEffect((0, 100, 255), 0, 0),
    constants.STATUS_EFFECT_PETRIFIED: StatusEffect((50, 50, 50), 0, 0),
    constants.STATUS_EFFECT_SPEED: StatusEffect((0, 255, 0), 0, 0, speed_start, speed_start, speed_end),
    constants.STATUS_EFFECT_DAZED: StatusEffect((100, 100, 0), 0, 0, dazed_start, dazed, dazed_end),
    constants.STATUS_EFFECT_STUNNED: StatusEffect((100, 100, 100), 0, 0),
    constants.STATUS_EFFECT_BERSERK: StatusEffect((255, 50, 0), 0, 0, berserk_start, berserk, berserk_end),
    constants.STATUS_EFFECT_SICKENED: StatusEffect((50, 255, 0), 0, 0),
    constants.STATUS_EFFECT_INVINCIBLE: StatusEffect((0, 0, 255), 0, 0),
    constants.STATUS_EFFECT_ARMOR_BROKEN: StatusEffect((255, 0, 255), 0, 0),
    constants.STATUS_EFFECT_ARMORED: StatusEffect((100, 100, 100), 0, 0),
    constants.STATUS_EFFECT_VIVACIOUS: StatusEffect((255, 0, 50), 0, 0),
    constants.STATUS_EFFECT_HEALTH_BOOST: StatusEffect((255, 0, 50), 0, 0, health_boost_start, health_boost, health_boost_end),
    constants.STATUS_EFFECT_CURSED: StatusEffect((50, 50, 50), 0, 0, cursed_start, cursed, cursed_end),
    constants.STATUS_EFFECT_ENLARGED: StatusEffect((0, 100, 0), 0, 0, enlarged_start, enlarged, enlarged_end),
    constants.STATUS_EFFECT_MINIFIED: StatusEffect((0, 0, 100), 0, 0, minified_start, minified, minified_end),
}