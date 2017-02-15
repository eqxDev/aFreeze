package me.eqxdev.afreeze.utils;

/**
 * Created by eqxDev on 03/02/2017.
 */
public enum FreezeType {

    FACTION("Faction"),
    ALL("Server"),
    HACKER("Hacker"),
    NO_GLASS("No Glass"),
    PLAYER("Normal");

    private final String name;

    private FreezeType(String name) {
        this.name = name;
    }

    public String toName() {
        return this.name;
    }

}
