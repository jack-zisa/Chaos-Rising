package dev.creoii.chaos.util;

import com.badlogic.gdx.files.FileHandle;

@FunctionalInterface
public interface Parser {
    Identifiable parse(FileHandle jsonFile);
}
