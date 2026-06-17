package com.example.rpgaspects;

public class PlayerData {
    private AspectType currentAspect = AspectType.NONE;

    public enum AspectType {
        NONE("Brak", "Zwykly smiertelnik bez zadnych modyfikatorow."),
        BERSERKER("Berserker", "+20% obrazen, ale otrzymujesz 10% wiecej obrazen i brak tarczy."),
        CIEŃ("Cien", "15% szansy na unik w nocy i +25% ze skradania, ale staly pancerz mniejszy o 15%."),
        PALADYN("Paladyn", "Otrzymujesz 15% mniej obrazen, ale poruszasz sie o 10% wolniej."),
        ŁOWCA("Lowca", "Strzaly nakladaja spowolnienie. Zadajesz 20% mniej obrazen mieczem/toporem."),
        ALCHEMIK("Alchemik", "Mikstury trwaja 50% dluzej. Slabosc: Perly Endu zadaja podwojne obrażenia."),
        WAMPIR("Wampir", "Leczysz sie o 12% zadanych obrazen, ale leczenie z jedzenia jest o 50% slabsze.");

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
