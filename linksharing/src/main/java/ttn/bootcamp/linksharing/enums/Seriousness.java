package ttn.bootcamp.linksharing.enums;

public enum Seriousness {

    CASUAL(0), SERIOUS(1), VERY_SERIOUS(2);

    private int seriousness;

    Seriousness(int seriousness) {
        this.seriousness = seriousness;
    }

    int getSeriousness() {
        return this.seriousness;
    }

}