package Expense.Tracker.Service;

import Expense.Tracker.Entity.ExpenseEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    private final static String file_path="Expense.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void fileExist(){
        File file=new File(file_path);
        if(!file.exists()){
            try{
                file.createNewFile();
                Files.write(Paths.get(file_path),"[]".getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void writeExpenses(List<ExpenseEntity> expenseEntity){
        try{
            String json=objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(expenseEntity);
            Files.write(Paths.get(file_path),json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    public List<ExpenseEntity> getAllexpenses(){
        fileExist();
        try{
            String json =Files.readString(Paths.get(file_path));
            return objectMapper.readValue(json, new TypeReference<List<ExpenseEntity>>() {});

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

    public void addExpenses(Integer amount, String description) {
        fileExist();
        List<ExpenseEntity> expenses=getAllexpenses();
        try {
            int newId = expenses.stream()
                    .mapToInt(task -> Integer.parseInt(task.getId()))
                    .max()
                    .orElse(0) + 1;
            ExpenseEntity expenseEntity = new ExpenseEntity(String.valueOf(newId), null, description, amount);
            if (expenseEntity.getDate() == null) {
                expenseEntity.setDate(LocalDate.now());
            }
            expenses.add(expenseEntity);
            writeExpenses(expenses);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public Integer totalExpense() {
        fileExist();
        List<ExpenseEntity> expenseEntities=getAllexpenses();
        Integer summary=0;
        for(ExpenseEntity expense:expenseEntities){
            summary+=expense.getAmount();
        }
        return summary;
    }

    public Integer monthWise(Integer month) {
        fileExist();
        List<ExpenseEntity> expenseEntities=getAllexpenses();
        Integer month_wise=0;
         for(ExpenseEntity expense:expenseEntities){
             if(expense.getDate().getMonthValue()==month){
                 month_wise+=expense.getAmount();
             }
         }
         return month_wise;
    }

    public boolean deleteExpense(String id) {
        fileExist();
        List<ExpenseEntity> Expense=getAllexpenses();
        boolean remove=Expense.removeIf(expense->expense.getId().equals(id));
        if(remove){
            writeExpenses(Expense);
        }
        return remove;
    }


    public boolean updateExpense(String id, Integer amount, String description, LocalDate date) {
        fileExist();
        List<ExpenseEntity> expenses = getAllexpenses();
        boolean updated = false;

        for (ExpenseEntity expense : expenses) {
            if (expense.getId().equals(id)) {
                if (amount != null) expense.setAmount(amount);
                if (description != null) expense.setDescription(description);
                if (date != null) expense.setDate(date);
                else expense.setDate(LocalDate.now());
                updated = true;
                break;
            }
        }

        if (updated) {
            writeExpenses(expenses);
        }

        return updated;
    }

}
