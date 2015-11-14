package prismarine.permission;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public abstract class GenericData {

    private String prefix = "";
    private String suffix = "";
    private final Multimap<String, String> permissions = HashMultimap.create();
    protected GenericData parent = null;

    protected GenericData() {
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void initialize(String worldName) {
    }

    public void release() {
        parent = null;
    }

    public abstract String getParentName();

    public abstract void setParentName(String parentName);
}
