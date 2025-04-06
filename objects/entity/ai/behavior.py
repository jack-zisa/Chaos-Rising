import random

def erratic(enemy, dt, data):
    enemy.pos.x += random.choice([-1, 1]) * enemy.stats.speed * data.get('speed', 1) * dt
    enemy.pos.y += random.choice([-1, 1]) * enemy.stats.speed * data.get('speed', 1) * dt

def chase(enemy, dt, data):
    distance = data.get('distance', 0)
    if enemy.get_center_pos().distance_to(enemy.game.main.character.get_center_pos()) >= distance:
        enemy.pos = enemy.pos.move_towards(enemy.game.main.character.get_center_pos(), data.get('speed', 1))

behaviors: dict = {
    'random': erratic,
    'chase': chase,
}