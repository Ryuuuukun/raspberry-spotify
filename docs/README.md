### Сборка и запуск проекта в режиме разработки

---

Сборка и запуск всех модулей:

```bash
docker-compose -f docker-compose.dev.yml up --build
```

Сборка и запуск отдельных модулей:

```bash
docker-compose -f docker-compose.dev.yml up --build [spotify-backend] [spotify-search] [spotify-frontend] 
```
