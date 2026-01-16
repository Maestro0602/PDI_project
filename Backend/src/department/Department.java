package Backend.src.department;

public enum Department {
    GIC("GIC"),
    GIM("GIM"),
    ELECTRIC("Electric");

    private String displayName;

    Department(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
