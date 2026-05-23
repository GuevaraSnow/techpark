# 🎡 Tech-Park UQ — Sistema de Gestión de Parque de Atracciones

Proyecto final de Estructuras de Datos — Universidad del Quindío 2026-1.
Sistema integral para administrar las operaciones de un parque de atracciones
inteligente, implementando estructuras de datos propias en Java con interfaz
gráfica en JavaFX.

---

## 👥 Integrantes

| Nombre | GitHub |
|---|---|
| Santiago Guevara | @GuevaraSnow |
| Josephe Cortex | @ |
| Juan David Torres | @ |

---

## 🛠 Tecnologías

- Java 21
- JavaFX 21
- Gson 2.10.1
- JUnit Jupiter 5.10.0
- Maven

---

## 📁 Estructura del proyecto
src/
├── main/
│   ├── java/
│   │   ├── model/
│   │   │   ├── atraccion/       # Atraccion, Zona, EstadoAtraccion
│   │   │   ├── estructuras/     # ListaEnlazada, ColaPrioridad, ArbolBST, Grafo, SetSimple
│   │   │   ├── gestores/        # ControlAcceso, GestorColas, GestorMantenimiento, GestorReportes
│   │   │   ├── persistencia/    # DataLoader
│   │   │   ├── ticket/          # Ticket, TicketGeneral, TicketFamiliar, TicketFastPass
│   │   │   └── Parque, Persona, Visitante, Empleado, Operador, Administrador
│   │   ├── techpark/            # Main.java
│   │   └── ui/
│   │       └── admin/           # AdminController, AdminView.fxml
│   └── resources/
│       └── data/
│           └── escenario_prueba.json
└── test/
└── java/
└── test/
├── ListaEnlazadaTest.java
└── AtraccionTest.java

---

## 🚀 Requisitos

- JDK 21 o superior
- IntelliJ IDEA 2024+
- Maven 3.8+
- Conexión a internet para descargar dependencias la primera vez

---

## ▶ Cómo ejecutar

**Opción 1 — IntelliJ IDEA:**
1. Clonar el repositorio
```bash
git clone https://github.com/GuevaraSnow/techpark.git
```
2. Abrir en IntelliJ → `File → Open`
3. Esperar que Maven descargue las dependencias
4. Ejecutar `techpark/Main.java`

**Opción 2 — Maven:**
```bash
mvn javafx:run
```

---

## 🧪 Ejecutar pruebas unitarias

```bash
mvn test
```

Pruebas implementadas:
- `ListaEnlazadaTest` — 11 casos sobre la lista enlazada genérica
- `AtraccionTest` — 10 casos sobre lógica de mantenimiento y clima

---

## 📦 Estructuras de datos propias

| Estructura | Uso en el sistema |
|---|---|
| `ListaEnlazada<T>` | Historial de visitas, operadores por zona, notificaciones |
| `ColaPrioridad<T>` | Filas de atracciones (FastPass > General) |
| `ArbolBST` | Catálogo de atracciones para búsqueda en O(log n) |
| `Grafo` | Mapa físico del parque con algoritmos BFS y Dijkstra |
| `SetSimple<T>` | Atracciones favoritas sin duplicados |

---

## 🗂 Carga de datos iniciales

Al iniciar la aplicación se carga automáticamente `escenario_prueba.json` con:
- 4 zonas del parque
- 8 atracciones de distintos tipos
- 4 visitantes con diferentes tickets
- 4 operadores asignados por zona
- 10 senderos que forman el grafo del mapa

---

## 📋 Convención de commits

El proyecto usa [Conventional Commits](https://www.conventionalcommits.org/):