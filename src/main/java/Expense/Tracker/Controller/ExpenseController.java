package Expense.Tracker.Controller;


import Expense.Tracker.Entity.ExpenseEntity;
import Expense.Tracker.Service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<List<ExpenseEntity>> getAllexpenses(){
        List<ExpenseEntity> expenses=expenseService.getAllexpenses();
        return ResponseEntity.ok(expenses);
    }
    @PostMapping
    public ResponseEntity<String> addExpenses(@RequestBody ExpenseEntity expense){
       expenseService.addExpenses(expense.getAmount(),expense.getDescription());
        return  ResponseEntity.ok("Expense Added Sucessfully");
    }
    @GetMapping("/total")
    public ResponseEntity<Integer> totalExpense(){
       Integer summary= expenseService.totalExpense();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/month")
    public ResponseEntity<Integer> monthWise(@RequestParam Integer month){
        Integer month_wise=expenseService.monthWise(month);
        return ResponseEntity.ok(month_wise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable String id){
        boolean Expense=expenseService.deleteExpense(id);
        return Expense ? ResponseEntity.ok("Deleted Sucessfully "):ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateExpense(@PathVariable String id,@RequestBody ExpenseEntity expense){

        boolean update=expenseService.updateExpense(id,expense.getAmount(),expense.getDescription(),expense.getDate());
        return update? ResponseEntity.ok("Updated Sucessfully"):ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
    }

}
