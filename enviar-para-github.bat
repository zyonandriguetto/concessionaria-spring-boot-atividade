@echo off
chcp 65001 >nul
cd /d "%~dp0"
set REPO=concessionaria-spring-boot-atividade
where git >nul 2>nul || (echo [ERRO] Git nao esta instalado. & pause & exit /b 1)
if not exist .git (
  git init -b main
)
git config user.name >nul 2>nul || git config user.name "Zyon Andriguetto"
git config user.email >nul 2>nul || git config user.email "zyonandriguetto@gmail.com"
git add -A
git commit -m "Concessionaria: Plataforma 1-N Modelo 1-N Peca com CRUD completo"
git remote get-url origin >nul 2>nul || git remote add origin https://github.com/zyonandriguetto/%REPO%.git
git push -u origin main
echo.
echo ==== FIM (codigo de saida: %ERRORLEVEL%) ====
pause
