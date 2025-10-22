@echo off
cd /d "C:\Users\ospar\code\GymManagementSystem"
javac -cp ".;lib/mysql-connector-j-8.0.33.jar" -d bin src/*.java
java -cp "bin;lib/mysql-connector-j-8.0.33.jar" GymManagementSystem
pause