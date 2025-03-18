# PacManJava 🎮

Ett klassiskt Pac-Man-spel utvecklat i Java med moderna förbättringar och spelmekanik.

![PacMan Screenshot](pacman_screenshot.jpg)

## 📖 Om Spelet

Detta projekt är en modern återskapning av arkadklassikern Pac-Man. Spelet använder Java Swing för grafik och har implementerat alla viktiga funktioner från originalspelet, plus flera förbättringar. Projektet demonstrerar objektorienterad programmering, speldesign och grundläggande AI-beteende.

## ✨ Funktioner

- **Klassisk Pac-Man-spelmekanik** - Ät alla prickar och power pellets medan du undviker spöken
- **Intelligent spökbeteende** - Varje spöke har sin egen AI-personlighet:
  - Rött spöke (Blinky): Jagar Pac-Man direkt med sin aggressiva algoritm
  - Rosa spöke (Pinky): Försöker hamna framför Pac-Man för att blockera vägen
  - Blått spöke (Inky): Kombinerar Pac-Man och Blinkys position för att skapa komplexa rörelsemönster
  - Orange spöke (Clyde): Växlar mellan jakt och flykt beroende på avstånd till Pac-Man
- **Snygga animationer** - Jämna animationer för Pac-Man och spökena med antialiasing
- **Ljudeffekter** - Klassiska Pac-Man-ljud för olika spelmoment
- **Poängsystem** - Dubbel poäng för varje spöke som äts under samma power pellet
- **Flera liv** - Tre liv innan spelomgången är slut
- **Tunnlar** - Passera genom tunnlar på sidorna av spelplanen
- **Dynamisk bana** - Speciellt utformad labyrint med klassisk Pac-Man-design

## 🖥️ Systemkrav

- Java Runtime Environment (JRE) 8 eller senare
- Minst 1GB RAM
- 100MB ledigt diskutrymme
- Skärmupplösning på minst 1024x768

## 📁 Projektstruktur

Projektets viktigaste filer inkluderar:

```
PacManJava/
│
├── src/                      # Källkod
│   ├── App.java              # Huvudklass och startpunkt
│   ├── PacMan.java           # Spellogik, kollisionshantering och rendering
│   ├── GameImages.java       # Bildgenerering för spelfigurer
│   ├── SoundManager.java     # Ljudhantering
│   └── [ljudfiler]           # .wav-filer för spelets ljudeffekter
│
├── README.md                 # Projektdokumentation
└── LICENSE                   # Licensinformation
```

## 🚀 Installation och Körning

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

### 🔊 Hantering av ljudfiler

Spelet kräver vissa .wav-filer för ljudeffekter. Om du får felmeddelanden om saknade ljudfiler, kan du åtgärda detta genom att lägga till följande filer i `src`-mappen:

- `eat.wav` - Ljudet när Pac-Man äter prickar
- `power.wav` - Ljudet när Pac-Man äter en power pellet
- `ghost.wav` - Ljudet när Pac-Man äter ett spöke
- `death.wav` - Ljudet när Pac-Man förlorar ett liv
- `start.wav` - Startljudet för spelet
- `gameover.wav` - Ljudet vid Game Over

Du kan skapa eller ladda ner dessa ljudfiler från öppna källor.

## 🎮 Spelkontroller

- **Piltangenter**: Styr Pac-Man (upp, ner, vänster, höger)
- **P**: Pausa/återuppta spelet
- **M**: Ljud på/av
- **ENTER**: Starta om efter Game Over

## 📏 Spelregler

- Styr Pac-Man genom labyrinten och ät alla prickar för att avancera till nästa nivå
- Ät power pellets för att tillfälligt kunna jaga spökena (de blir blå)
- Varje uppäten prick ger 10 poäng
- Varje power pellet ger 50 poäng
- Poäng för att äta spöken: 200 för första, 400 för andra, 800 för tredje, 1600 för fjärde (under samma power pellet)
- Undvik spökena - vid kontakt förlorar du ett liv (om de inte är blå)
- När du har samlat alla prickar och power pellets har du klarat nivån
- Spelet är slut när alla dina tre liv är förbrukade

## 🔧 Felsökning

### Vanliga problem och lösningar

1. **Spelet startar inte:**
   - Kontrollera att du har Java installerat. Kör `java -version` i terminalen.
   - Se till att du befinner dig i rätt mapp när du kör spelet.

2. **Ljudfiler saknas:**
   - Lägg till de saknade .wav-filerna i src-mappen enligt beskrivningen ovan.
   - Om du föredrar att spela utan ljud, tryck på **M** för att stänga av ljudet.

3. **Långsam prestanda:**
   - Stäng andra program för att frigöra minne.
   - Minska skärmupplösningen eller spelfönstrets storlek.

4. **Spöken fastnar i spökgården:**
   - Detta kan hända i vissa versioner. Starta om spelet för att lösa problemet.

## 🔄 Förbättringar

- Modern grafik med antialiasing för jämnare kanter
- Förbättrad kollisionsdetektering för mer exakt spelkänsla
- Intelligent spökbeteende baserat på originalspelets mönster
- Visuell feedback när poäng samlas
- Pausfunktion för spelkontroll
- Ljudinställningar för en mer immersiv upplevelse
- Förbättrad spökgård med tydligare utgång

## 🛠️ Utvecklat med

- Java - Programmeringsspråket
- Swing - För grafik och GUI
- Egen implementation av spelmekanik och AI
- Objektorienterad design med klasshierarki för spelkomponenter

## 🔜 Framtida utveckling

- Flera nivåer med ökande svårighetsgrad
- Högre poänglista för att spara resultat
- Ytterligare spellägen, som klassiskt vs. modernt
- Multiplayer-funktionalitet
- Mobilversion

## 📝 Licens

Detta projekt är licensierat under MIT-licensen - se LICENSE-filen för detaljer.

## 👏 Erkännanden

- Inspirerat av det ursprungliga Pac-Man-spelet skapat av Namco
- Tack till alla som har bidragit till projektet

## 💡 Bidra

Om du vill bidra till projektet:

1. Skapa en fork av projektet
2. Skapa din funktionsgren (`git checkout -b feature/amazing-feature`)
3. Gör dina ändringar och commit dem (`git commit -m 'Lägg till någon fantastisk funktion'`)
4. Pusha till grenen (`git push origin feature/amazing-feature`)
5. Öppna en Pull Request
