import random
import util.constants as constants

def erratic(enemy, dt, data):
    if not enemy.has_effect(constants.STATUS_EFFECT_PARALYZED) or not enemy.has_effect(constants.STATUS_EFFECT_FROZEN) or not enemy.has_effect(constants.STATUS_EFFECT_PETRIFIED):
        enemy.pos.x += random.choice([-1, 1]) * enemy.stats.speed * data.get('speed', 1) * dt
        enemy.pos.y += random.choice([-1, 1]) * enemy.stats.speed * data.get('speed', 1) * dt

def chase(enemy, dt, data):
    if not enemy.has_effect(constants.STATUS_EFFECT_PARALYZED) or not enemy.has_effect(constants.STATUS_EFFECT_FROZEN) or not enemy.has_effect(constants.STATUS_EFFECT_PETRIFIED):
        distance = data.get('distance', 0)
        if enemy.get_center_pos().distance_to(enemy.game.main.character.get_center_pos()) >= distance:
            enemy.pos = enemy.pos.move_towards(enemy.game.main.character.get_center_pos(), data.get('speed', 1))

behaviors: dict = {
    'random': erratic,
    'chase': chase,
}