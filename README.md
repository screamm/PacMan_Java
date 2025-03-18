# PacManJava

Ett klassiskt Pac-Man-spel utvecklat i Java med moderna förbättringar och spelmekanik.

![PacMan Screenshot](pacman_screenshot.jpg)

## Om Spelet

Detta projekt är en modern återskapning av arkadklassikern Pac-Man. Spelet använder Java Swing för grafik och har implementerat alla viktiga funktioner från originalspelet, plus flera förbättringar.

## Funktioner

- **Klassisk Pac-Man-spelmekanik** - Ät alla prickar och power pellets medan du undviker spöken
- **Intelligent spökbeteende** - Varje spöke har sin egen AI-personlighet:
  - Rött spöke (Blinky): Jagar Pac-Man direkt
  - Rosa spöke (Pinky): Försöker hamna framför Pac-Man
  - Blått spöke (Inky): Kombinerar Pac-Man och Blinkys position för att bestämma målpunkt
  - Orange spöke (Clyde): Växlar mellan jakt och flykt beroende på avstånd
- **Snygga animationer** - Jämna animationer för Pac-Man och spökena
- **Ljudeffekter** - Klassiska Pac-Man-ljud
- **Poängsystem** - Dubbel poäng för varje spöke som äts under samma power pellet
- **Flera liv** - Tre liv innan spelomgången är slut

## Systemkrav

- Java Runtime Environment (JRE) 8 eller senare
- Minst 1GB RAM
- 100MB ledigt diskutrymme

## Installation och Körning

1. Klona eller ladda ner detta repository
2. Navigera till projektmappen i terminalen
3. Kompilera spelet:
   ```
   cd src
   javac App.java
   ```
4. Kör spelet:
   ```
   java App
   ```

## Spelkontroller

- **Piltangenter**: Styr Pac-Man (upp, ner, vänster, höger)
- **P**: Pausa/återuppta spelet
- **M**: Ljud på/av
- **ENTER**: Starta om efter Game Over

## Spelregler

- Styr Pac-Man genom labyrinten och ät alla prickar för att avancera till nästa nivå
- Ät power pellets för att tillfälligt kunna jaga spökena
- Undvik spökena - vid kontakt förlorar du ett liv
- När du har samlat alla prickar och power pellets har du klarat nivån

## Förbättringar

- Modern grafik med antialiasing
- Förbättrad kollisionsdetektering
- Intelligent spökbeteende baserat på originalspelets mönster
- Visuell feedback när poäng samlas
- Pausfunktion
- Ljudinställningar

## Utvecklat med

- Java
- Swing för grafik
- Egen implementation av spelmekanik och AI

## Licens

Detta projekt är licensierat under MIT-licensen - se LICENSE-filen för detaljer.

## Erkännanden

- Inspirerat av det ursprungliga Pac-Man-spelet skapat av Namco
- Tack till alla som har bidragit till projektet
