package prismarine.permission;

public final class PlayerData extends GenericData {

    private String group = "";

    public PlayerData() {
    }

    @Override
    public String getParentName() {
        return group;
    }

    @Override
    public void setParentName(String parentName) {
        group = parentName;
    }

    @Override
    public void initialize(String worldName) {
        super.initialize(worldName);
    }

    public void release() {
        super.release();
    }
}
