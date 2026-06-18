package com.example.rpgaspects;

public class PlayerData {
    private AspectType currentAspect = AspectType.NONE;

    public enum AspectType {
        NONE("Brak", "Zwykly smiertelnik bez zadnych modyfikatorow."),
        BERSERKER("Berserker", "+20% obrazen. Otrzymujesz 10% wiecej ran. Brak tarczy."),
        CIEN("Cien", "15% na unik w nocy. +25% obrazen ze skradania. Kara: 15% wiecej ran w dzien."),
        PALADYN("Paladyn", "Otrzymujesz 15% mniej obrazen. Posiadasz staly efekt Spowolnienia I."),
        LOWCA("Lowca", "Strzaly nakladaja spowolnienie. Zadajesz 20% mniej obrazen wrecz."),
        ALCHEMIK("Alchemik", "Mikstury trwaja 50% dluzej. Slabosc: Perly endu rania 2x mocniej."),
        WAMPIR("Wampir", "Leczysz sie o 12% zadanych obrazen. Regeneracja z jedzenia o 50% slabsza.");

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
