class Stats:
    def __init__(self, health: int = 1, speed: int = 1, attack_speed: int = 1, defense: int = 0, attack: int = 1, vitality: int = 1):
        self.health = health
        self.speed = speed
        self.attack_speed = attack_speed
        self.defense = defense
        self.attack = attack
        self.vitality = vitality
    
    def copy(self) -> 'Stats':
        return Stats(self.health, self.speed, self.attack_speed, self.defense, self.attack, self.vitality)
    
    def from_json(data: dict) -> 'Stats':
        return Stats(data.get('health', 1), data.get('speed', 1), data.get('attack_speed', 1), data.get('defense', 1), data.get('attack', 0), data.get('vitality', 1))