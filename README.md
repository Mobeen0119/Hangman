# 🌑 Hangman

A stylish dark-themed Hangman game built using **Java Swing** with glowing neon UI effects, login/signup system, animated loading screens, high scores, and an immersive horror-inspired interface.

---

## ✨ Features

* 🎮 Classic Hangman gameplay
* 🌑 Dark neon-red horror UI theme
* 🔥 Custom glowing animated buttons
* 👤 Login & Signup system
* 💾 Load game support structure
* 🏆 High scores screen
* 📖 Instructions dialog with animated loading bar
* 🎨 Custom-painted Swing components
* ⌨️ Interactive on-screen keyboard
* 💀 Animated glowing hangman drawing
* 🖼️ Background image support with fallback gradient
* ⚡ Smooth hover effects and transitions

---

## 📸 Preview

### Main Menu

* Dark cinematic UI
* Neon glowing buttons
* Responsive layout

### Gameplay

* Interactive keyboard
* Dynamic word reveal
* Lives tracking
* Custom hangman rendering

---

## 🛠️ Technologies Used

| Technology     | Purpose                        |
| -------------- | ------------------------------ |
| Java           | Core programming language      |
| Java Swing     | GUI framework                  |
| AWT Graphics2D | Custom rendering & effects     |
| Timer API      | Animations and loading effects |
| ImageIO        | Background image loading       |

---

## 📂 Project Structure

```bash
DarkHangman/
│
├── DarkHangman.java
├── resources/
│   └── background.jpg
│
└── README.md
```

---

## 🚀 How to Run

### 1️⃣ Clone Repository

```bash
git clone https://github.com/your-username/dark-hangman.git
```

### 2️⃣ Navigate into Project

```bash
cd dark-hangman
```

### 3️⃣ Compile

```bash
javac DarkHangman.java
```

### 4️⃣ Run

```bash
java DarkHangman
```

---

## 🎯 Gameplay Rules

1. The game selects a random word.
2. Guess letters using the on-screen keyboard.
3. Correct guesses reveal letters.
4. Wrong guesses reduce lives.
5. Lose all 6 lives and the hangman is completed.
6. Guess the full word before that happens to win.

---

## 🧠 Current Word Categories

```text
Programming
Computer Science
Technology
Software Development
```

Example words:

* JAVA
* DATABASE
* NETWORK
* SOFTWARE
* FUNCTION
* ALGORITHM

---

## 🎨 UI Design Highlights

### Custom Components

* Glowing hover animations
* Neon borders
* Gradient backgrounds
* Custom scrollbar styling
* Dynamic button painting

### Visual Style

Inspired by:

* Horror game menus
* Cyberpunk aesthetics
* Dark terminal interfaces

---

## 🔥 Major Classes

| Class         | Responsibility          |
| ------------- | ----------------------- |
| `DarkHangman` | Application entry point |
| `StartScreen` | Main menu & navigation  |
| `GameFrame`   | Main gameplay window    |
| `UserManager` | Handles login state     |

---

## ⚠️ Current Limitations

The project currently uses:

* Temporary login system (no database)
* In-memory high scores
* No actual save/load persistence yet

These are planned for future updates.

---

## 🚧 Future Improvements

* 💾 File/database save system
* 🌍 Online leaderboard
* 🎵 Background music & sound effects
* 🧩 Difficulty modes
* 📚 More word categories
* 🖼️ Better animations
* 👥 Multiplayer mode
* ☠️ Story/horror mode

---

## 💡 Learning Concepts Demonstrated

This project demonstrates:

* Java Swing GUI development
* Event-driven programming
* Custom graphics rendering
* Object-oriented programming
* Timers and animations
* UI/UX design concepts
* State management



