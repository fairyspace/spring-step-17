package io.github.fairyspace.test.bean;

public class Wife {
    private Husband husband;

    public String queryHusband() {
        return "Wife.husband、Mother.callMother：";
    }


    public Husband getHusband() {
        return husband;
    }

    public void setHusband(Husband husband) {
        this.husband = husband;
    }



}
