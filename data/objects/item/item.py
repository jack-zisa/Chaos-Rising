from data.objects.entity import stat

class Item:
    def __init__(self, id: str, bullet_id: str, damage: int, stats: stat.Stats):
        self.id = id
        self.bullet_id = bullet_id
        self.damage = damage
        self.stats = stats

    def from_json(data: dict) -> 'Item':
        return Item(data.get('id', ''), data.get('bullet_id', ''), data.get('damage', 0), stat.Stats.from_json(data.get('stats', {})))
