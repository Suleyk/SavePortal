# **🚧 WORK IN PROGRESS! 🚧**

# SavePortal User Guide

## Introduction

Welcome to SavePortal, your simple tool for managing game saves. SavePortal makes it easy to back up, restore, and
organize your game progress.

## Features

- **Backup and restore saves:** Protect your game progress effortlessly.
- **Profiles management:** Create, rename, delete, and duplicate profiles for each playthrough or scenario, for example,
  a profile for a specific speedrun route, a no-hit strat, etc.
- **Versatile backups:** Back up entire folder structures such as savegame folders, mod folders, or experiment with
  different game setups.
- **Intuitive UI:** Enjoy a straightforward interface for a seamless experience.

  <br>

  ![image](https://github.com/Suleyk/SavePortal/assets/20606042/fb6c22d3-4db6-4e15-82af-da13d01b59e0)

<br>

## Installation

1. **Download:** Get the latest SavePortal release from [here](https://github.com/Suleyk/SavePortal/releases).
2. **Extract Files:** Unzip the downloaded file to your chosen location.
3. **Run SavePortal:** Launch the application. SavePortal requires Java; ensure you have Java Runtime Environment (JRE)
   version 11 or later installed on your system. You can download it
   from [Oracle Java](https://www.oracle.com/java/technologies/javase-downloads.html)
   or [OpenJDK](https://adoptopenjdk.net/).

## Getting started

### Adding a game

1. Click "Add game."
2. Choose the folder where your game saves are stored.
3. Enter a name for the new game.
4. Click "OK" to confirm.

### Managing profiles

- **Add profile:** Create custom profiles for different playthroughs or characters.
- **Duplicate profile:** Quickly create a copy of a profile, including all its backups.
- **Rename profile:** Personalize your profiles with meaningful names.
- **Delete profile:** Remove unwanted profiles easily.

### Backing up saves

1. When adding a game, the chosen folder will be the target for backups.
2. Click "Import active save" to create a backup of your current game savestate.

### Restoring saves

1. Select from the list the backup you want to restore.
2. Click "Export backup save" to replace the active save with the selected backup.

**_Keep in mind:_** _exporting the backup save will restore the entire folder structure, removing any new subfolders or
files the game created. Make sure you know what each folder contains and how each game manages the savedata._

---

### **_How importing a save works:_**

When you click **Import**, SavePortal duplicates the entire folder you chose earlier when adding a game. This
duplication includes every file and subfolder inside the selected folder, maintaining the exact structure.

For example:

If the game has this structure:

```
Game Folder
├── GameData1
│   ├── Textures
│   ├── Binaries
│   └── Configuration
├── GameData2
└── SaveFolder <--------
    ├── Slot1
    │   ├── DataFile1.sav
    │   └── DataFile2.sav
    └── Slot2
        ├── DataFile1.sav
        └── DataFile2.sav
```

By choosing **SaveFolder**, when clicking **Import**, it would create an identical copy:

```
SavePortal
└── Backups
  └── AddedGame
    └── Profile1
        └── ImportedSave00 <--------
            ├── Slot1
            │   ├── DataFile1.sav
            │   └── DataFile2.sav
            └── Slot2
                ├── DataFile1.sav
                └── DataFile2.sav
```

---

## Tips for creativity

SavePortal is not just limited to game saves; it's a versatile tool that allows you to explore different possibilities,
since you're the one to choose the target folder which could be anything, from saves to mods folder, or even the entire
game folder.

- **Quick Practice:** Use SavePortal as a checkpoint maker to quickly return to specific in-game scenarios or boss
  fights.
- **Mod Folder Backup:** Get creative by using SavePortal to back up and experiment with mod folders.
- **Multiple Game Setups:** Manage different game setups for diverse gaming experiences.

Feel free to explore and make SavePortal your personalized gaming companion!

---

## Important note

SavePortal is a work in progress, and although it has been tested thoroughly, there may be unexpected behavior. Please
handle your data with care and consider creating backups manually occasionally as a precaution. Your feedback is
invaluable in improving the tool.

Feel free to explore and make SavePortal your personalized gaming companion!

<br>

---

<br><br><br>

---

# SavePortal developer section

## Welcome developers,

I appreciate your interest in SavePortal, a project that holds significance for me as a self-taught developer delving
into the world of software creation.

## Technical snapshot

- **Technology Stack:** SavePortal leverages JavaFX to deliver an intuitive and sleek user interface.
- **Objective:** SavePortal aims to simplify the management of game saves, offering an efficient solution for backup,
  restore, and organization.

## Development journey insights

SavePortal is a product of individual learning and exploration in the realm of programming. While the project has
received input from various sources and benefited from the insights of ChatGPT, the core learning experience revolves
around self-discovery.

## Continuous enhancement

SavePortal is an evolving project. Your feedback, suggestions, and contributions are highly valued and contribute to the
ongoing refinement and enhancement of this tool.

Feel free to explore the code, delve into functionalities, and share your insights. Together, we can collectively
enhance SavePortal.

Thank you for being part of this development journey.

---
