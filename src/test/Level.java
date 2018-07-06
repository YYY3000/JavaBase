package test;

public enum Level {

    High(1),
    Mid(2),
    Low(4);

    private Integer level;

    Level(Integer level) {
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

}
