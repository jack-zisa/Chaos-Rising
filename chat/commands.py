import random
import pygame

def set_health(main, args):
    if len(args) > 0:
        main.character.stats.health = max(0, int(args[0]))

def spawn_enemy(main, args):
    x = int(args[0]) if len(args) > 0 else random.randint(0, main.screen.get_width())
    y = int(args[1]) if len(args) > 1 else random.randint(0, main.screen.get_height())
    enemy = main.game.data_manager.get_enemy('test')
    main.game.entity_manager.add_entity(enemy, pygame.Vector2(x, y))

commands: dict = {
    '/sethealth': set_health,
    '/spawnenemy': spawn_enemy,
}

def execute(main, command: str):
    elements: list = command.split(' ')
    command = elements[0]

    if command not in commands:
        print(f'Command \'{command}\' not found.')
    else:
        args: list = elements[1:]
        print(f'Execute \'{command}\' with args: {str(args)}')
        commands[command](main, args)