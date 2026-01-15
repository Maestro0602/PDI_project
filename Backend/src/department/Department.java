package Backend.src.department;

public enum Department {
    GIC("GIC"),
    GIM("GIM"),
    GEE("GEE");

    private String displayName;

    Department(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
