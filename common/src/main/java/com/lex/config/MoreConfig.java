package com.lex.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.lex.MoreBrewery;
import dev.architectury.platform.Platform;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class MoreConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final List<Option<?>> OPTIONS = new ArrayList<>(); // precisa vir antes dos campos abaixo

    public static final IntOption LOSS_PER_ALCOHOL_LEVEL = new IntOption("liver", "lossPerAlcoholLevel", 3, 0, 100);
    public static final IntOption FLAT_LOSS_PER_ALCOHOLIC_ITEM = new IntOption("liver", "flatLossPerAlcoholicItem", 0, 0, 100);
    public static final IntOption WATER_RESTORE = new IntOption("liver", "waterRestore", 5, 0, 100);
    public static final IntOption TAG_RESTORE = new IntOption("liver", "teaRestore", 3, 0, 100);
    public static final IntOption NATURAL_REGEN_PER_MINUTE = new IntOption("liver", "naturalRegenPerMinute", 1, 0, 100);

    public static final DoubleOption MAX_DAMAGE_PENALTY = new DoubleOption("penalties", "maxDamagePenalty", 0.50, 0.0, 1.0);
    public static final DoubleOption MAX_SPEED_PENALTY = new DoubleOption("penalties", "maxSpeedPenalty", 0.30, 0.0, 1.0);
    public static final DoubleOption MAX_HEALTH_PENALTY = new DoubleOption("penalties", "maxHealthPenalty", 0.40, 0.0, 0.9);

    private MoreConfig() {}

    public static void load() {
        Path file = Platform.getConfigFolder().resolve(MoreBrewery.MOD_ID + ".json");
        JsonObject root = new JsonObject();

        if (Files.exists(file)) {
            try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                JsonElement parsed = JsonParser.parseReader(reader);
                if (parsed.isJsonObject()) root = parsed.getAsJsonObject();
            } catch (IOException | JsonParseException e) {
                MoreBrewery.LOGGER.warn("Não foi possível ler {}; usando valores padrão", file, e);
            }
        }

        JsonObject out = new JsonObject();
        for (Option<?> option : OPTIONS) {
            JsonElement section = root.get(option.section);
            if (section != null && section.isJsonObject() && section.getAsJsonObject().has(option.key)) {
                try {
                    option.read(section.getAsJsonObject().get(option.key));
                } catch (RuntimeException e) {
                    MoreBrewery.LOGGER.warn("Valor inválido em {}.{}; usando o padrão", option.section, option.key);
                }
            }
            JsonObject target = out.has(option.section) ? out.getAsJsonObject(option.section) : new JsonObject();
            target.add(option.key, option.json());
            out.add(option.section, target);
        }

        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, GSON.toJson(out), StandardCharsets.UTF_8);
        } catch (IOException e) {
            MoreBrewery.LOGGER.warn("Não foi possível salvar {}", file, e);
        }
    }

    public abstract static class Option<T extends Number> {
        final String section;
        final String key;

        Option(String section, String key) {
            this.section = section;
            this.key = key;
            OPTIONS.add(this);
        }

        public abstract T get();

        abstract void read(JsonElement element);

        abstract JsonElement json();
    }

    public static final class IntOption extends Option<Integer> {
        private final int min, max;
        private volatile int value;

        IntOption(String section, String key, int def, int min, int max) {
            super(section, key);
            this.min = min;
            this.max = max;
            this.value = def;
        }

        @Override public Integer get() { return value; }
        @Override void read(JsonElement e) { value = Math.max(min, Math.min(max, e.getAsInt())); }
        @Override JsonElement json() { return new JsonPrimitive(value); }
    }

    public static final class DoubleOption extends Option<Double> {
        private final double min, max;
        private volatile double value;

        DoubleOption(String section, String key, double def, double min, double max) {
            super(section, key);
            this.min = min;
            this.max = max;
            this.value = def;
        }

        @Override public Double get() { return value; }
        @Override void read(JsonElement e) { value = Math.max(min, Math.min(max, e.getAsDouble())); }
        @Override JsonElement json() { return new JsonPrimitive(value); }
        public static void init(){

        }

    }
}
