package fr.lastril.uhchost.modes.naruto.v2.izanami;

public enum IzanamiGoalAuthor {

    DAMAGEPLAYER("§eRecevoir un coup de la part du joueur ciblé", false),
    DAMAGETARGET("§eInfliger un total de 15 coups au joueur ciblé", false),
    //GIVETARGET("§eDonner une pomme dorée au joueur ciblé", false),
    //GIVEJOUEUR("§eRecevoir une pomme dorée de la part du joueur ciblé.", false),
    PROXIMITYTARGET("§eRester à 20 blocs du joueur ciblé pendant§c 5 minutes", false),
    PLACELAVA("§ePoser un§6 seau de lave§e sous les pieds du joueur ciblé", false);

    private final String description;
    private boolean done;

    IzanamiGoalAuthor(String description, boolean done) {
        this.description = description;
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }
}
