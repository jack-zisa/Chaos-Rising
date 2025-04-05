import random

def erratic(enemy, dt, data):
    enemy.pos.x += random.choice([-1, 1]) * enemy.stats.speed * 5 * dt
    enemy.pos.y += random.choice([-1, 1]) * enemy.stats.speed * 5 * dt

def chase(enemy, dt, data):
    enemy.pos = enemy.pos.move_towards(enemy.game.main.character.pos, 1)

behaviors: dict = {
    'random': erratic,
    'chase': chase,
}