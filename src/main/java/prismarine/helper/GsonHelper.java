package prismarine.helper;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.io.Files;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import prismarine.Prismarine;

import java.io.File;
import java.io.IOException;

public final class GsonHelper {

    private static final Gson gson = new GsonBuilder()
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
}
