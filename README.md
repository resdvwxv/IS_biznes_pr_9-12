# Практические работы №9–12

Репозиторий содержит четыре практические работы по темам ООП, паттернам проектирования и обработке данных с помощью Java Streams.

## Структура

Каждая работа находится в отдельной директории `pr_XX/` и включает:

| Элемент | Описание |
|---|---|
| `starter_project/` | Исходный проект с реализованным кодом |
| `starter_project/src/` | Исходные Java-файлы (`ru.edu.prXX`) |
| `starter_project/data/` | Тестовые данные (CSV) |
| `starter_project/run_selfcheck.sh` | Скрипт компиляции и запуска SelfCheck |
| `starter_project/DECISIONS.md` | Документация принятых архитектурных решений |
| `starter_project/Практическая работа №XX.docx` | Отчёт в формате Word (тетрадь) |
| `activity_partial.dot` | Activity-диаграмма (Mermaid DOT) |
| `class_partial.dot` | Диаграмма классов (Mermaid DOT) |

## Директории с тетрадями (.docx)

Файлы тетрадей расположены в `starter_project/` каждой практической работы:

| Практика | Файл тетради |
|---|---|
| №9 — Склад (Inventory) | `pr_09/starter_project/Практическая работа №9.docx` |
| №10 — Доставка (Delivery) | `pr_10/starter_project/Практическая работа №10.docx` |
| №11 — Импорт клиентов (Client Import) | `pr_11/starter_project/Практическая работа №11.docx` |
| №12 — Аналитика продаж (Sales Analytics) | `pr_12/starter_project/Практическая работа №12.docx` |

## Запуск SelfCheck

```bash
cd pr_09/starter_project && bash run_selfcheck.sh
cd pr_10/starter_project && bash run_selfcheck.sh
cd pr_11/starter_project && bash run_selfcheck.sh
cd pr_12/starter_project && bash run_selfcheck.sh
```

Каждый SelfCheck выводит `PASSED=N FAILED=M` и завершается с кодом `1` при ошибках.

## Краткое содержание работ

| № | Тема | Ключевые классы |
|---|---|---|
| 9 | Склад — учёт товаров и движений | `Warehouse`, `StockItem`, `StockMovement` |
| 10 | Доставка — стратегия расчёта стоимости | `DeliveryPolicy`, `CargoPolicy`, `DeliveryService` |
| 11 | Импорт клиентов — парсинг CSV и валидация | `ClientImporter`, `ClientValidator`, `ClientRepository` |
| 12 | Аналитика продаж — Java Streams | `SalesAnalyticsService`, `Sale` |
