package com.example.rpgaspects;

public class PlayerData {
    private AspectType currentAspect = AspectType.NONE;

    public enum AspectType {
        NONE("Brak", "Zwykly smiertelnik bez zadnych modyfikatorow."),
        BERSERKER("Berserker", "+20% obrazen. Otrzymujesz 10% wiecej ran. Brak tarczy."),
        CIEN("Cien", "15% na unik w nocy. +25% obrazen ze skradania. Kara: 15% wiecej ran w dzien."),
        PALADYN("Paladyn", "Otrzymujesz 15% mniej obrazen. Kara: Zadajesz o 15% mniej obrazen."),
        LOWCA("Lowca", "Strzaly nakladaja zamrozenie. Zadajesz 20% mniej obrazen wrecz."),
        ALCHEMIK("Alchemik", "Zwiekszona odpornosc na ogien. Slabosc: 2x wieksze obrazenia magiczne."),
        WAMPIR("Wampir", "Leczysz sie o 12% zadanych obrazen. Regeneracja z jedzenia slabsza o 50%.");

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
