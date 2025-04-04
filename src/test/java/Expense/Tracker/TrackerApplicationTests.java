package Expense.Tracker;

import Expense.Tracker.Entity.ExpenseEntity;
import Expense.Tracker.Service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class TrackerApplicationTests {

	private ExpenseService expenseService;

	@BeforeEach
	void setUp() {
		expenseService = new ExpenseService();
	}

	@Test
	void testAddAndGetAllExpenses() {
		expenseService.addExpenses(300, "Book Purchase");
		System.out.println("Added expense: 300 for 'Book Purchase'");

		List<ExpenseEntity> all = expenseService.getAllexpenses();
		System.out.println("Retrieved all expenses: " + all);

		assertFalse(all.isEmpty(), "Expense list should not be empty.");
		assertTrue(all.stream().anyMatch(e -> "Book Purchase".equals(e.getDescription())), "Should contain 'Book Purchase'");

	}

	@Test
	void testDeleteExpense() {
		expenseService.addExpenses(150, "dinner");

		ExpenseEntity added = expenseService.getAllexpenses().get(0);
		String id = added.getId();
		System.out.println("ID of expense to delete: " + id);

		boolean deleted = expenseService.deleteExpense(id);
		System.out.println("Deletion result: " + deleted);

		List<ExpenseEntity> afterDelete = expenseService.getAllexpenses();
		System.out.println("Expenses after deletion: " + afterDelete);

		assertTrue(deleted, "Deletion should return true.");
		assertTrue(afterDelete.stream().noneMatch(e -> e.getId().equals(id)), "Expense should no longer exist.");

	}

	@Test
	void testDeleteNonExistingExpense() {
		String fakeId = "2";
		System.out.println("Trying to delete non-existing ID: " + fakeId);

		boolean deleted = expenseService.deleteExpense(fakeId);
		System.out.println("Deletion result for non-existing ID: " + deleted);

		assertFalse(deleted, "Deleting a non-existent expense should return false.");
	}
}
