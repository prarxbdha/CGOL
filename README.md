# CGOL

A minimal Swing implementation of Conway's Game of Life with a squircle grid, synchronized parameter controls, and live color selection.

## Structure

```
CGOL/
├── pom.xml
├── README.md
├── src/
│   ├── main/java/com/cgol/
│   │   ├── Main.java                 entry point
│   │   ├── model/GameOfLife.java     simulation rules and grid state
│   │   ├── ui/
│   │   │   ├── MainWindow.java       window layout and statistics
│   │   │   ├── GridPanel.java        grid rendering and interaction
│   │   │   └── SettingsPanel.java    sidebar controls
│   │   ├── ui/components/            reusable custom Swing controls
│   │   └── config/AppTheme.java      palette, fonts, squircle geometry
│   └── test/java/com/cgol/GameOfLifeTest.java
└── docs/screenshots/
```

## Build and run

With Maven:

```bash
mvn test
mvn exec:java
```

Without Maven (JDK only):

```bash
mkdir -p out
javac -d out $(find src/main/java -name '*.java')
java -cp out com.cgol.Main
```

To run the JUnit tests without Maven, download `junit-platform-console-standalone` and run:

```bash
javac -cp junit-platform-console-standalone.jar -d out \
  $(find src/main/java src/test/java -name '*.java')
java -jar junit-platform-console-standalone.jar --class-path out --scan-class-path
```

## Controls

- Click or drag on the grid to draw or erase cells while paused.
- **Start** advances generations at the configured tick interval; **Pause** keeps the current generation; **Clear** empties the grid and resets the generation counter.
- Rows, columns, cell size, and tick interval each have a slider and an editable numeric field that stay in sync.
- The three swatches change alive, dead, and canvas colors immediately.
- Generation and living-cell counts are shown below the canvas.
