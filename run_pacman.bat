@echo off
echo Kompilerar PacMan...
cd src
javac PacMan.java
if %errorlevel% neq 0 (
    echo Kompileringsfel! Avbryter.
    pause
    exit /b %errorlevel%
)
echo Kompilering klar. Startar spelet...
java App
echo Spelet har avslutats.
pause 