import random
import pygame

def set_class(main, args):
    if len(args) > 0:
        clazz = main.game.data_manager.get_character_class(args[0])
        if clazz is not None:
            main.character.clazz = clazz
            main.character.max_stats = clazz.base_stats

def set_stat(main, args):
    if len(args) > 1:
        stat = args[0]
        value = int(args[1])

        if stat == 'health':
            main.character.stats.health = max(0, value)
        elif stat == 'speed':
            main.character.stats.speed = max(0, value)

def spawn_enemy(main, args):
    x = int(args[0]) if len(args) > 0 else random.randint(0, main.screen.get_width())
    y = int(args[1]) if len(args) > 1 else random.randint(0, main.screen.get_height())
    enemy = main.game.data_manager.get_enemy('test')
    main.game.entity_manager.add_entity(enemy, pygame.Vector2(x, y))

commands: dict = {
    '/setstat': set_stat,
    '/spawnenemy': spawn_enemy,
    '/setclass': set_class,
}

def execute(main, command_str: str):
    elements: list = command_str.split(' ')
    command = elements[0]

    if command not in commands:
        print(f'Command \'{command}\' not found.')
    else:
        args: list = elements[1:]
        print(f'Execute \'{command}\' with args: \'{','.join(args)}\'')
        commands[command](main, args)