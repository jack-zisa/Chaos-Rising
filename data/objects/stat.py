class Stats:
    def __init__(self, health: int = 1, speed: int = 1):
        self.health = health
        self.speed = speed
    
    def copy(self) -> 'Stats':
        return Stats(self.health, self.speed)
    
    def from_json(data: dict) -> 'Stats':
        return Stats(data.get('health', 1), data.get('speed', 1))