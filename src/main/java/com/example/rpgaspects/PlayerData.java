package com.example.rpgaspects;

public class PlayerData {
    private AspectType currentAspect = AspectType.NONE;

    public enum AspectType {
        NONE("Brak", "Zwykly smiertelnik bez zadnych modyfikatorow."),
        BERSERKER("Berserker", "Wieksze obrazenia im mniej masz zdrowia. Brak tarczy."),
        CIEŃ("Cien", "15% szansy na unik w nocy. Pierwszy cios ze skradania zadaje +30% obrazen."),
        PALADYN("Paladyn", "Otrzymujesz 15% mniej obrazen, ale poruszasz sie o 10% wolniej."),
        ŁOWCA("Lowca", "Strzaly z luku/kuszy nakladaja spowolnienie. -20% obrazen w zwarciu."),
        ALCHEMIK("Alchemik", "Wypijane mikstury trwaja dluzej. Negatywne efekty trwaja krocej."),
        WAMPIR("Wampir", "Leczysz sie o 15% zadanych obrazen. Ogien zadaje podwojne obrazenia.");

        private final String name;
        private final String description;

        AspectType(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
    }

    public AspectType getCurrentAspect() { return currentAspect; }
    public void setCurrentAspect(AspectType currentAspect) { this.currentAspect = currentAspect; }
}
