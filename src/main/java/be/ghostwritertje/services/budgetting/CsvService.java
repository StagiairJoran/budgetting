package be.ghostwritertje.services.budgetting;


import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.domain.budgetting.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by jorandeboever
 * on 18/03/16.
 */
@Service
public class CsvService {

    @Autowired
    private StatementService statementService;

    @Autowired
    private BankAccountService bankAccountService;

    public void uploadCSVFile(String fileUrl, Person person) {

        BufferedReader br = null;
        String line;
        String cvsSplitBy = ";";

        Map<String, BankAccount> bankAccountMap = bankAccountService.findByAdministrator(person).stream().collect(Collectors.toConcurrentMap(BankAccount::getNumber, b -> b));

        List<Statement> statementList = new ArrayList<>();

        try {

            br = new BufferedReader(new FileReader(fileUrl));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] row = line.split(cvsSplitBy);


                if (row.length > 0 && row[0].startsWith("BE")) {
                    Statement statement = new Statement();
                    statement.setAmount(BigDecimal.valueOf(Double.parseDouble(row[10].replace(",", "."))));
                    LocalDate date = LocalDate.parse(row[1], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    statement.setDate(date);

                    String fromAccount = row[0];
                    String toAccount = row[4];

                    BankAccount from = Optional.ofNullable(bankAccountMap.get(fromAccount)).orElseGet(() -> {
                        BankAccount bankAccount = new BankAccount();
                        bankAccount.setNumber(fromAccount);
                        bankAccount.setOwner(person);
                        bankAccount.setAdministrator(person);
                        bankAccountMap.put(fromAccount, bankAccount);
                        return bankAccount;
                    });

                    BankAccount to = Optional.ofNullable(bankAccountMap.get(toAccount)).orElseGet(() -> {
                        BankAccount bankAccount = new BankAccount();
                        bankAccount.setNumber(toAccount);
                        bankAccount.setAdministrator(person);
                        bankAccountMap.put(toAccount, bankAccount);
                        return bankAccount;
                    });

                    statement.setOriginatingAccount(from);
                    statement.setDestinationAccount(to);
                    statementList.add(statement);
                }
            }

            this.bankAccountService.save(bankAccountMap.values());
            this.statementService.save(statementList);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
