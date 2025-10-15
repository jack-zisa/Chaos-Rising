package dev.creoii.chaos.item.tooltip;

import com.mojang.serialization.Codec;

import java.util.*;
import java.util.stream.Collectors;

public record Tooltip(Map<Section, List<String>> sections) {
    public static final Tooltip EMPTY = new Tooltip();
    public static final Codec<Tooltip> CODEC = Codec.unboundedMap(Section.CODEC, Codec.STRING.listOf()).xmap(map -> {
        return map.isEmpty() ? EMPTY : new Tooltip(map);
    }, Tooltip::sections);

    public Tooltip() {
        this(new HashMap<>());
    }

    public Tooltip order() {
        return new Tooltip(sections.entrySet().stream().sorted(Comparator.comparingInt(entry -> entry.getKey().ordinal())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, _) -> a, LinkedHashMap::new)));
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
