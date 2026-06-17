package com.example.rpgaspects;

public class PlayerData {
    private int level = 1;
    private int exp = 0;
    private AspectType currentAspect = AspectType.NONE;

    public enum AspectType {
        NONE("Brak", "Zwykly smiertelnik bez zadnych modyfikatorow.", 1),
        BERSERKER("Berserker", "Wieksze obrazenia im mniej masz zdrowia. Brak mozliwosci uzywania tarczy.", 5),
        CIEŃ("Cien", "15% szansy na unik w nocy. Pierwszy cios ze skradania zadaje +30% obrazen.", 10),
        PALADYN("Paladyn", "Otrzymujesz 15% mniej obrazen, ale poruszasz sie o 10% wolniej.", 15),
        ŁOWCA("Lowca", "Strzaly z luku/kuszy nakladaja spowolnienie na 2s. Zredukowane obrazenia w zwarciu o 20%.", 20),
        ALCHEMIK("Alchemik", "Wypijane mikstury trwaja 50% dluzej. Negatywne efekty trwaja o polowe krocej.", 25),
        WAMPIR("Wampir", "Leczysz sie o 15% zadanych obrazen, ale ogien zadaje Ci podwojne obrazenia.", 30);

        private final String name;
        private final String description;
        private final int minLevel;

        AspectType(String name, String description, int minLevel) {
            this.name = name;
            this.description = description;
            this.minLevel = minLevel;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getMinLevel() { return minLevel; }
    }

    public void addExp(int amount) {
        this.exp += amount;
        if (this.exp >= getRequiredExp()) {
            this.exp -= getRequiredExp();
            this.level++;
        }
    }

    public int getRequiredExp() {
        return this.level * 100;
    }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public AspectType getCurrentAspect() { return currentAspect; }
    public void setCurrentAspect(AspectType currentAspect) { this.currentAspect = currentAspect; }
}
