import os
import json
from data.objects import character as ch, enemy as en
import data.objects.character as ch

data_schema: dict = {
    'classes': ch.CharacterClass.from_json,
    'enemies': en.Enemy.from_json
}
data: dict = {key: {} for key in data_schema.keys()}

def get_character_class(name: str) -> ch.CharacterClass:
    return data['classes'][name]

def get_enemy(name: str) -> en.Enemy:
    return data['enemies'][name]

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
                        data[folder][obj.name] = obj
                except json.JSONDecodeError as e:
                    print(f"Error parsing {filename} in {folder}: {e}")
