<div align="center">

# Air Traffic Simulation System

### Object-Oriented Java Application with GUI

![Language](https://img.shields.io/badge/language-Java-orange)
![GUI](https://img.shields.io/badge/GUI-Swing%20%7C%20JavaFX-green)
![Architecture](https://img.shields.io/badge/architecture-MVC-blue)
![OOP](https://img.shields.io/badge/paradigm-Object%20Oriented-success)
![Status](https://img.shields.io/badge/status-completed-brightgreen)

A fully object-oriented Java application for simulating air traffic, airport visualization,  
and real-time flight animation.

</div>

---

# Table of Contents

- [Overview](#overview)
- [Features](#features)
- [System Requirements](#system-requirements)
- [Building the Project](#building-the-project)
- [Running the Application](#running-the-application)
- [User Interface](#user-interface)
- [Development Phases](#development-phases)
- [Architecture](#architecture)
- [Error Handling](#error-handling)
- [Examples](#examples)
- [Conclusion](#conclusion)

---

# Overview

This project implements a complete **Air Traffic Simulation System** using **Java** and a graphical user interface.

The application simulates:

- airport management
- flight scheduling
- map visualization
- airport interaction
- real-time aircraft animation

The system follows strong **object-oriented design principles** and uses layered architecture.

---

# Features

✅ Airport management  
✅ Flight scheduling  
✅ CSV import/export  
✅ Interactive GUI  
✅ Airport visualization  
✅ Airport filtering  
✅ Airport selection animation  
✅ Real-time simulation  
✅ Pause / Resume / Reset  
✅ Automatic inactivity timeout  
✅ Multithreading and timers  
✅ Detailed error handling  

---

# System Requirements

- Java 8+
- Windows / Linux / macOS

---

# Building the Project

```bash
javac -d out src/**/*.java
```

---

# Running the Application

```bash
java -cp out Main
```

---

# User Interface

The application provides a simple and intuitive graphical interface.

## Main Menu Functionality

From the **top menu**, the user can:

### Data Management

- 📂 **Import CSV file**
  - Load airport or flight data from a file  
- ➕ **Add new data**
  - Manually enter airports and flights through GUI forms  
- 💾 **Export data**
  - Save current data into a CSV file  

---

### Simulation Control

The user can also manage the simulation directly:

- ▶️ **Start simulation**
- ⏸ **Pause simulation**
- 🔄 **Reset simulation**

---

### Summary

```txt
Top Menu Options:
- Import CSV
- Add Data
- Export Data
- Start Simulation
- Pause Simulation
- Reset Simulation
```

This menu acts as the central control point for the entire application.

---

# Development Phases

---

## Phase A — Data Entry

- Airport creation (name, code, coordinates)
- Flight creation (departure, destination, time, duration)
- CSV import/export
- Input validation
- Error messages
- 60-second inactivity timeout

---

## Phase B — Visualization

- Airport map rendering
- Airports shown as squares
- Airport labels (codes)
- Click to select airport
- Selected airport blinks red
- Airport filtering with checkboxes

---

## Phase C — Simulation

- Simulation starts at 00:00
- 1 second = 10 simulated minutes
- Aircraft represented as moving circles
- Movement updates every 200 ms
- Only one plane per airport every 10 minutes
- Queue system for delayed flights
- Controls: Start / Pause / Reset

---

# Architecture

The system follows **MVC (Model-View-Controller)** design.

---

## Model

- Airport
- Flight
- Aircraft

---

## View

- GUI components
- Map rendering
- Tables
- Dialogs

---

## Controller

- Input handling
- Simulation logic
- Event processing

---

# Error Handling

The system provides clear and user-friendly error messages.

Examples:

```txt
Invalid coordinates (must be between -90 and 90)
```

```txt
Airport code must be 3 uppercase letters
```

```txt
File format is incorrect
```

```txt
Unable to start simulation
```

All errors are handled gracefully without crashing the application.

---

# Examples

---

## Creating Airport

```txt
Name: Nikola Tesla
Code: BEG
Coordinates: (25, -13)
```

---

## Creating Flight

```txt
BEG → FRA
08:30
120 minutes
```

---

## Running Simulation

```txt
Click Start Simulation
```

---

## Filtering Airports

```txt
☑ BEG
☐ FRA
☑ JFK
```

---

---

# Testing Files

> [!NOTE]
> All files required for testing the application and simulation are included in the repository.

The `test_data/` directory contains:

- `airports.csv` — sample airport data
- `flights.csv` — sample flight schedule
- additional test scenarios for simulation

These files can be loaded directly through:

```txt
File → Import CSV
```

and are recommended for quick testing of all application features.

---

# Conclusion

This project demonstrates a complete object-oriented implementation of a **real-time air traffic simulation system** in Java.

Key concepts:

- OOP design
- GUI development
- MVC architecture
- Multithreading
- Timers and synchronization
- Error handling
- File persistence

The system is modular, scalable, and suitable for further extensions.

---

<div align="center">

### Faculty of Electrical Engineering, University of Belgrade  
### Object-Oriented Programming 2  
### Academic Year 2024/2025  

</div>
