package prismarine.helper;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import com.google.common.reflect.TypeToken;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import prismarine.Prismarine;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public final class GsonHelper {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Multimap.class, new MultiMapAdapter())
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .create();

    private GsonHelper() {
    }

    public static <T> Optional<T> loadInstance(String filename, Class<T> clazz) {
        File file = Prismarine.getFile(filename);
        if (file.exists()) {
            try {
                String json = Joiner.on('\n').join(Files.readLines(file, Charsets.UTF_8));
                return Optional.of(gson.fromJson(json, clazz));
            } catch (IOException e) {
                Prismarine.getLogger().catching(e);
            }
        }
        return Optional.absent();
    }

    public static void saveInstance(String filename, Object instance) {
        try {
            Files.write(gson.toJson(instance), Prismarine.getFile(filename), Charsets.UTF_8);
        } catch (IOException e) {
            Prismarine.getLogger().catching(e);
        }
    }

    private static final class MultiMapAdapter<K, V> implements JsonSerializer<Multimap<K, V>>, JsonDeserializer<Multimap<K, V>> {

        private final Type mapType;

        private MultiMapAdapter() {
            try {
                this.mapType = Multimap.class.getDeclaredMethod("asMap").getGenericReturnType();
            } catch (NoSuchMethodException e) {
                throw Throwables.propagate(e);
            }
        }

        @Override
        public JsonElement serialize(Multimap<K, V> src, Type type, JsonSerializationContext context) {
            return context.serialize(src.asMap(), getType(type));
        }

        @Override
        public Multimap<K, V> deserialize(JsonElement json, Type type, JsonDeserializationContext context)
                throws JsonParseException {
            Map<K, Collection<V>> map = context.deserialize(json, getType(type));
            Multimap<K, V> multimap = HashMultimap.create();
            for (Map.Entry<K, Collection<V>> entry : map.entrySet()) {
                multimap.putAll(entry.getKey(), entry.getValue());
            }
            return multimap;
        }

        private Type getType(Type type) {
            return TypeToken.of(type).resolveType(mapType).getType();
        }
    }
}
