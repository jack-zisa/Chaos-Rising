import os
import json
from data.objects import character as ch, enemy as en, bullet

data_schema: dict = {
    'class': ch.CharacterClass.from_json,
    'enemy': en.Enemy.from_json,
    'bullet': bullet.Bullet.from_json,
}
data: dict = {key: {} for key in data_schema.keys()}

def get_character_class(id: str) -> ch.CharacterClass:
    return data['class'][id]

def get_enemy(id: str) -> en.Enemy:
    return data['enemy'][id]

def get_bullet(id: str) -> bullet.Bullet:
    return data['bullet'][id]

def load():
    global data_schema, data
    
    base = 'resources/data/'

    if not os.path.exists(base):
        print(f"Directory '{base}' does not exist.")
        return

    for folder, parser in data_schema.items():
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
                        data[folder][obj.id] = obj
                except json.JSONDecodeError as e:
                    print(f"Error parsing {filename} in {folder}: {e}")
