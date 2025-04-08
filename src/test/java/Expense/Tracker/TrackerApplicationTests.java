package Expense.Tracker;

import Expense.Tracker.Entity.ExpenseEntity;
import Expense.Tracker.Service.ExpenseService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TrackerApplicationTests {

	@Autowired
	private ExpenseService expenseService;

	@BeforeEach
	void setUp() {
		expenseService.getAllexpenses().forEach(e -> expenseService.deleteExpense(e.getId()));
	}

	@Order(1)
	@Test
	void testAddExpense() {
		ExpenseEntity added = addSampleExpense("Book Purchase", 300);

		List<ExpenseEntity> all = expenseService.getAllexpenses();
		assertFalse(all.isEmpty());
		assertTrue(all.stream().anyMatch(e -> e.getDescription().equals("Book Purchase")));

		System.out.println(" Added " + added.getAmount() + " for '" + added.getDescription() + "'");
	}

	@Order(2)
	@Test
	void testAddMultipleExpenses() {
		addSampleExpense("Laptop EMI", 1200);
		addSampleExpense("Coffee", 200);
		addSampleExpense("Internet Bill", 300);

		List<ExpenseEntity> all = expenseService.getAllexpenses();
		List<String> descriptions = all.stream().map(ExpenseEntity::getDescription).toList();

		assertEquals(3, all.size());
		assertTrue(descriptions.containsAll(List.of("Laptop EMI", "Coffee", "Internet Bill")));

		System.out.println(" Added " +
				all.stream().map(e -> e.getAmount() + " for '" + e.getDescription() + "'")
						.reduce((a, b) -> a + ", " + b).orElse(""));
	}

	@Order(3)
	@Test
	void testDeleteExpense() {
		ExpenseEntity added = addSampleExpense("Dinner", 150);
		String id = added.getId();

		boolean deleted = expenseService.deleteExpense(id);
		List<ExpenseEntity> afterDelete = expenseService.getAllexpenses();

		assertTrue(deleted);
		assertTrue(afterDelete.stream().noneMatch(e -> e.getId().equals(id)));

		System.out.println("Deleted ID = " + id);
	}

	@Order(4)
	@Test
	void testDeleteNonExistingExpense() {
		String fakeId = "non-existent-id";

		boolean deleted = expenseService.deleteExpense(fakeId);

		assertFalse(deleted);
		System.out.println("Attempted deletion of non-existing ID = " + fakeId);
	}

	@Order(5)
	@Test
	void testUpdateExpense() {
		ExpenseEntity original = addSampleExpense("Snacks", 100);
		LocalDate newDate = LocalDate.of(2025, 4, 8);
        System.out.println("Original Description-"+original.getDescription()+"Amount-"+original.getAmount());
		boolean updated = expenseService.updateExpense(original.getId(), 250, "Evening Snacks", newDate);
		assertTrue(updated);

		ExpenseEntity updatedEntity = expenseService.getAllexpenses().stream()
				.filter(e -> e.getId().equals(original.getId()))
				.findFirst()
				.orElseThrow();

		assertEquals(250, updatedEntity.getAmount());
		assertEquals("Evening Snacks", updatedEntity.getDescription());
		assertEquals(newDate, updatedEntity.getDate());

		System.out.println("Updated to " + updatedEntity.getAmount() +
				" for '" + updatedEntity.getDescription() + "' on " + updatedEntity.getDate());
	}

	@Order(6)
	@Test
	void testFindByMonth() {
		LocalDate currentDate = LocalDate.now();
		int currentMonth = currentDate.getMonthValue();

		addSampleExpense("Internet", 300);
		addSampleExpense("Groceries", 500);

		int total = expenseService.monthWise(currentMonth);

		List<ExpenseEntity> all = expenseService.getAllexpenses();
		int expectedTotal = all.stream()
				.filter(e -> e.getDate().getMonthValue() == currentMonth)
				.mapToInt(ExpenseEntity::getAmount)
				.sum();

		assertEquals(expectedTotal, total);

		System.out.println("Total expenses for month " + currentMonth + " = ₹" + total);
	}

	//Helper Function
	private ExpenseEntity addSampleExpense(String description, int amount) {
		expenseService.addExpenses(amount, description);
		return expenseService.getAllexpenses().stream()
				.filter(e -> e.getDescription().equals(description))
				.findFirst()
				.orElseThrow();
	}
}
