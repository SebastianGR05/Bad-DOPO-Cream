[bad-dopo-cream-readme.md](https://github.com/user-attachments/files/24196236/bad-dopo-cream-readme.md)
# Bad DOPO Cream 🍦

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-007396?style=for-the-badge&logo=java&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=junit5&logoColor=white)

**Un juego de acción y estrategia inspirado en Bad Ice Cream**

[Características](#características) • [Instalación](#instalación) • [Cómo Jugar](#cómo-jugar) • [Estructura del Proyecto](#estructura-del-proyecto)

</div>

---

## 📖 Descripción

Bad DOPO Cream es una versión recreada del clásico juego Bad Ice Cream, desarrollada en Java como proyecto final para el curso de Desarrollo Orientado Por Objetos. El jugador controla un helado que debe recolectar frutas mientras evita enemigos y utiliza bloques de hielo estratégicamente para protegerse o atrapar a sus adversarios.

## ✨ Características

### 🎮 Modos de Juego
- **Player**: Modalidad de un jugador (completamente funcional)
- **Player vs Player**: Modo multijugador (próximamente)
- **Machine vs Machine**: Modo de máquinas automáticas (próximamente)

### 🍨 Personajes Jugables
Tres sabores de helado para elegir:
- **Vainilla** 🤍 - Clásico y versátil
- **Fresa** 💗 - Dulce y ágil
- **Chocolate** 🤎 - Fuerte y determinado

### 🗺️ Niveles
**Nivel 1 - Fácil**
- Enemigos: 2 Trolls
- Frutas: 8 Uvas (50 pts) + 8 Plátanos (100 pts)
- Obstáculos: Bloques de hielo

**Nivel 2 - Intermedio**
- Enemigos: 1 Maceta (persigue al jugador)
- Frutas: 8 Piñas (200 pts) + 8 Plátanos (100 pts)
- Obstáculos: Bloques de hielo + Baldosas calientes

**Nivel 3 - Difícil**
- Enemigos: 1 Calamar Naranja (rompe hielo)
- Frutas: 8 Piñas (200 pts) + 8 Cerezas (150 pts)
- Obstáculos: Bloques de hielo + Fogatas

### 🎯 Mecánicas de Juego

#### Movimiento
- Controles con **WASD**
- Movimiento en 4 direcciones (arriba, abajo, izquierda, derecha)

#### Bloques de Hielo
- **ESPACIO**: Crear/destruir bloques de hielo
- Crea líneas de bloques en la dirección que miras
- Destruye bloques en efecto dominó
- Úsalos para protegerte o atrapar enemigos

#### Frutas
| Fruta | Puntos | Comportamiento |
|-------|--------|----------------|
| 🍇 Uva | 50 | Estática |
| 🍌 Plátano | 100 | Estático |
| 🍒 Cereza | 150 | Se teletransporta cada 20 segundos |
| 🍍 Piña | 200 | Se mueve aleatoriamente |

#### Enemigos
- **Trolls**: Se mueven en línea recta, cambian de dirección al chocar
- **Maceta**: Persigue al jugador, no rompe bloques
- **Calamar Naranja**: Persigue al jugador y rompe un bloque de hielo a la vez

#### Obstáculos
- **Bloques de hielo**: Bloquean el movimiento, pueden ser creados/destruidos
- **Fogatas**: Eliminan al jugador al contacto, se apagan temporalmente con hielo (10 seg)
- **Baldosas calientes**: Derriten instantáneamente los bloques de hielo creados sobre ellas y no elminan al jugador

### ⏱️ Reglas
- Tiempo límite: **3 minutos** por nivel
- Recolecta todas las frutas para ganar
- Si un enemigo o una fogata te toca, pierdes
- Si se acaba el tiempo, pierdes

## 🚀 Instalación

### Requisitos Previos
- Java JDK 11 o superior
- IDE compatible con Java (Eclipse, IntelliJ IDEA, NetBeans, etc.)

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/SebastianGR05/BadDOPOCream.git
cd BadDOPOCream
```

2. **Importar el proyecto en tu IDE**
   - En Eclipse: `File > Import > Existing Projects into Workspace`
   - En IntelliJ: `File > Open` y selecciona la carpeta del proyecto

3. **Verificar la estructura de carpetas**
```
BadDOPOCream/
├── src/
│   ├── Domain/          # Lógica del juego
│   ├── Presentation/    # Interfaz gráfica
│   └── Test/           # Pruebas unitarias
└── Resource/
    └── images/         # Recursos gráficos
```

4. **Ejecutar el juego**
   - Ejecuta la clase `MainMenu.java` ubicada en `src/Presentation/MainMenu.java`

## 🎮 Cómo Jugar

### Controles

| Acción | Teclas |
|--------|--------|
| Mover arriba | W |
| Mover abajo | S |
| Mover izquierda | A |
| Mover derecha | D |
| Crear/Destruir hielo | ESPACIO |
| Pausar/Reanudar | P / ESC |
| Reiniciar nivel | R |

### Flujo del Juego

1. **Menú Principal**: Selecciona "PLAY"
2. **Selección de Modalidad**: Elige "Player"
3. **Selección de Helado**: Escoge tu sabor favorito
4. **Selección de Nivel**: Elige el nivel que deseas jugar
5. **¡A Jugar!**: Recolecta todas las frutas antes de que se acabe el tiempo

### Estrategias

💡 **Consejo**: Usa los bloques de hielo para:
- Crear barreras entre tú y los enemigos
- Atrapar enemigos en espacios cerrados
- Apagar fogatas temporalmente
- Planear rutas seguras hacia las frutas

## 📁 Estructura del Proyecto

### Arquitectura de Capas

```
src/
├── Domain/                    # Capa de Dominio
│   ├── Game.java             # Controlador principal del juego
│   ├── Board.java            # Tablero del juego
│   ├── IceCream.java         # Clase del jugador
│   ├── Position.java         # Sistema de coordenadas
│   │
│   ├── Fruits/               # Frutas
│   │   ├── Fruit.java        # Clase abstracta
│   │   ├── Banana.java
│   │   ├── Grape.java
│   │   ├── Cherry.java
│   │   └── Pineapple.java
│   │
│   ├── Enemies/              # Enemigos
│   │   ├── Enemy.java        # Clase abstracta
│   │   ├── Troll.java
│   │   ├── Pot.java
│   │   └── OrangeSquid.java
│   │
│   ├── Obstacles/            # Obstáculos
│   │   ├── Obstacle.java     # Clase abstracta
│   │   ├── IceBlock.java
│   │   ├── Campfire.java
│   │   └── HotTile.java
│   │
│   └── BadDopoCreamException.java
│
├── Presentation/              # Capa de Presentación
│   ├── MainMenu.java         # Menú principal
│   ├── ModalityMenu.java     # Selección de modalidad
│   ├── IceCreamSelectionMenu.java
│   ├── LevelSelectionMenu.java
│   ├── GameWindow.java       # Ventana de juego
│   │
│   └── LevelPanels/          # Paneles de visualización
│       ├── LevelPanel.java   # Clase abstracta
│       ├── Level1Panel.java
│       ├── Level2Panel.java
│       └── Level3Panel.java
│
└── Test/                      # Pruebas Unitarias
    └── GameTest.java         # Suite de pruebas JUnit
```

### Patrones de Diseño Utilizados

- **Herencia**: Clases abstractas para Fruit, Enemy y Obstacle
- **Polimorfismo**: Diferentes comportamientos para cada tipo de entidad
- **Encapsulamiento**: Atributos privados con getters/setters
- **Separación de Responsabilidades**: Arquitectura en capas (Domain/Presentation)

## 🧪 Pruebas

El proyecto incluye una suite completa de pruebas unitarias con JUnit 5.

### Ejecutar las Pruebas

```bash
# En la línea de comandos
./gradlew test  # Si usas Gradle

# O desde tu IDE
# Click derecho en GameTest.java > Run As > JUnit Test
```

### Cobertura de Pruebas

- ✅ Inicialización del juego
- ✅ Movimiento del jugador
- ✅ Creación y destrucción de bloques de hielo
- ✅ Colisión con enemigos
- ✅ Recolección de frutas
- ✅ Sistema de pausa
- ✅ Condiciones de victoria/derrota
- ✅ Reinicio de niveles

## 📊 Diagrama de Clases (Simplificado)

```
┌─────────────┐
│    Game     │
├─────────────┤
│ - board     │────────┐
│ - player    │────┐   │
│ - enemies   │──┐ │   │
│ - fruits    │─┐│ │   │
└─────────────┘ ││ │   │
                ││ │   │
    ┌───────────┘│ │   │
    │  ┌─────────┘ │   │
    │  │  ┌────────┘   │
    │  │  │            │
    ▼  ▼  ▼            ▼
┌──────────┐      ┌─────────┐
│  Fruit   │      │  Board  │
├──────────┤      ├─────────┤
│ Cherry   │      │ grid[][]│
│ Banana   │      │ ...     │
│ Grape    │      └─────────┘
│ Pineapple│
└──────────┘
    
┌──────────┐      ┌───────────┐
│  Enemy   │      │ Obstacle  │
├──────────┤      ├───────────┤
│ Troll    │      │ IceBlock  │
│ Pot      │      │ Campfire  │
│ Squid    │      │ HotTile   │
└──────────┘      └───────────┘
```

## 👥 Autores

- Sebastian Guerrero Ruge
- Santiago Sanchez Monroy


## 📚 Referencias

- **Juego Original**: Bad Ice Cream by Nitrome (2010)
- **Documentación**: [Nitrome Wiki - Bad Ice Cream](https://nitrome.fandom.com/wiki/Bad_Ice_Cream)
- **Curso**: Desarrollo Orientado Por Objetos - Escuela Colombiana de Ingeniería

## 📝 Licencia

Este proyecto es un trabajo académico desarrollado para fines educativos.

---

<div align="center">

**¿Encontraste un bug? ¿Tienes una sugerencia?**

[Reportar Issue](../../issues) • [Contribuir](../../pulls)

Hecho con ❤️ y ☕ por estudiantes de la Escuela Colombiana de Ingeniería

</div>
