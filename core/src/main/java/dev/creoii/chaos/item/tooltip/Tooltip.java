package dev.creoii.chaos.item.tooltip;

import com.mojang.serialization.Codec;

import java.util.*;

public record Tooltip(EnumMap<Section, List<String>> sections) {
    public static final Tooltip EMPTY = new Tooltip();
    public static final Codec<Tooltip> CODEC = Codec.unboundedMap(Section.CODEC, Codec.STRING.listOf()).xmap(map -> {
        return map.isEmpty() ? EMPTY : new Tooltip(new EnumMap<>(map));
    }, Tooltip::sections);

    public Tooltip() {
        this(new EnumMap<>(Section.class));
    }

    public Tooltip order() {
        EnumMap<Section, List<String>> ordered = new EnumMap<>(Section.class);
        sections.entrySet().stream()
            .sorted(Comparator.comparingInt(entry -> entry.getKey().ordinal()))
            .forEach(entry -> ordered.put(entry.getKey(), entry.getValue()));
        return new Tooltip(ordered);
    }

    public List<String> getSection(Section section) {
        return sections.get(section);
    }

    public List<String> getOrCreateSection(Section section) {
        return sections.computeIfAbsent(section, _ -> new ArrayList<>());
    }

    public void addSection(Section section) {
        addSection(section, new ArrayList<>());
    }

    public void addSection(Section section, String line) {
        List<String> lines = new ArrayList<>();
        lines.add(line);
        addSection(section, lines);
    }

    public void addSection(Section section, String... lines) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, lines);
        addSection(section, list);
    }

    public void addSection(Section section, List<String> lines) {
        sections.putIfAbsent(section, lines);
    }

    public void addLine(Section section, String line) {
        getOrCreateSection(section).add(line);
    }

    public void addLine(Section section, String... lines) {
        Collections.addAll(getOrCreateSection(section), lines);
    }

    public void addLine(Section section, List<String> lines) {
        getOrCreateSection(section).addAll(lines);
    }

    public void removeSection(Section section) {
        sections.remove(section);
    }

    public enum Section {
        NAME,
        RARITY,
        TYPE,
        DESCRIPTION,
        STATS;

        public static final Codec<Section> CODEC = Codec.STRING.xmap(s -> Section.valueOf(s.toUpperCase()), section -> section.name().toLowerCase());
    }
}
