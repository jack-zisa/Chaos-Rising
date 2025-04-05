import os
import json

class DataManager:
    def __init__(self, main):
        from data.objects import character as ch, enemy as en, bullet

        self.main = main
        self.data_schema: dict = {
            'class': ch.CharacterClass.from_json,
            'enemy': en.Enemy.from_json,
            'bullet': bullet.Bullet.from_json,
        }
        self.data: dict = {key: {} for key in self.data_schema.keys()}

    def get_character_class(self, id: str):
        return self.data['class'].get(id, None)

    def get_enemy(self, id: str):
        return self.data['enemy'].get(id, None)

    def get_bullet(self, id: str):
        return self.data['bullet'].get(id, None)

    def load(self):        
        base = 'resources/data/'

        if not os.path.exists(base):
            print(f"Directory '{base}' does not exist.")
            return

        for folder, parser in self.data_schema.items():
            folder_path = os.path.join(base, folder)

            if not os.path.exists(folder_path):
                print(f"Folder '{folder_path}' does not exist, skipping.")
                continue

            for filename in os.listdir(folder_path):
                if filename.endswith(".json"):
                    file_path = os.path.join(folder_path, filename)
                    try:
                        with open(file_path, "r", encoding="utf-8") as file:
                            obj = parser(json.load(file))
                            self.data[folder][obj.id] = obj
                    except json.JSONDecodeError as e:
                        print(f"Error parsing {filename} in {folder}: {e}")