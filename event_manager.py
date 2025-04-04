import pygame

def poll_events(stop):
    global running
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            stop()