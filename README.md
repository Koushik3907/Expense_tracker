# 💸 Expense Tracker

A simple Spring Boot application to manage your expenses. You can add, update, delete, and view expenses. It also provides total and monthly summaries.

---

## ✅ Features

- Add a new expense (amount + description)
- Update an expense by ID
- Delete an expense by ID
- View all expenses
- View total expense
- View expenses for a specific month

---

## 📬 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET`  | `/ExpenseTracker` | Get all expenses |
| `POST` | `/ExpenseTracker` | Add an expense |
| `PUT`  | `/ExpenseTracker/{id}` | Update an expense |
| `DELETE` | `/ExpenseTracker/{id}` | Delete an expense |
| `GET`  | `/ExpenseTracker/total` | Get total expense |
| `GET`  | `/ExpenseTracker/month?month=MM` | Get monthly expense |

---

## 📦 Data Storage

All expenses are saved locally in a file named `Expense.json`.
