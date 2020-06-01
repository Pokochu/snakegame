# snakegame
Classic Snake game created in Java, JavaFX and H2db.

## Features:
- Start game with 5 lives
- Display current score, lives and top score during game.
- Save score if no lives left.
- Display top ten scores at High Scores menu
- Score data is stored in embedded H2 db, so no data is available at startup

## TroubleShooting:
If application says that cannot create preferences, try to run the program "as administrator".\
If it still says, do the following:
- Open RegEdit
- create Prefs key in HKEY_LOCAL_MACHINE\Software\JavaSoft (Windows 10 seems to now have this here: HKEY_LOCAL_MACHINE\Software\WOW6432Node\JavaSoft)
