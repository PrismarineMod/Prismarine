package prismarine.permission;

public final class GroupData extends GenericData {

    private String inheritance = "";

    public GroupData() {
    }

    @Override
    public String getParentName() {
        return inheritance;
    }

    @Override
    public void setParentName(String parentName) {
        inheritance = parentName;
    }
}
