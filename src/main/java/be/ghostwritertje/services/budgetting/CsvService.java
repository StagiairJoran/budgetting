package be.ghostwritertje.services.budgetting;


import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.domain.budgetting.Statement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger LOG = LogManager.getLogger();

    public void uploadCSVFile(String fileUrl, BankAccount bankAccount, String bank) {

        if(bank.toLowerCase().contains("belfius")){
            this.uploadCsvBelfius(fileUrl, bankAccount);
        }else {
            this.uploadCsvKeytrade(fileUrl, bankAccount);
        }

    }

    private void uploadCsvKeytrade(String fileUrl, BankAccount originatingBankAccount) {
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ";";

        Map<String, BankAccount> bankAccountMap = this.bankAccountService.findByAdministrator(originatingBankAccount.getAdministrator())
                .stream()
                .collect(Collectors.toConcurrentMap(BankAccount::getNumber, b -> b));

        List<Statement> statementList = new ArrayList<>();

        try {

            br = new BufferedReader(new FileReader(fileUrl));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] row = line.split(cvsSplitBy);


                if (row.length > 0 && line.endsWith("EUR")) {
                    Statement statement = new Statement();
                    statement.setAmount(BigDecimal.valueOf(Double.parseDouble(row[5].replace(",", "."))));
                    LocalDate date = LocalDate.parse(row[1], DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    statement.setDate(date);

                    String toAccount = row[3];
                    BankAccount to = bankAccountMap.get(toAccount);

                    if(to == null && toAccount != null && !"-".equalsIgnoreCase(toAccount)){
                        to = new BankAccount();
                        to.setNumber(toAccount);
                        to.setAdministrator(originatingBankAccount.getAdministrator());
                        bankAccountMap.put(toAccount, to);
                    }
                    statement.setDescription(row[4]);
                    statement.setOriginatingAccount(originatingBankAccount);
                    statement.setDestinationAccount(to);

                    statement.setCsv_line(line);
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

    private void uploadCsvBelfius(String fileUrl, BankAccount originatingBankAccount) {
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ";";

        Map<String, BankAccount> bankAccountMap = bankAccountService.findByAdministrator(originatingBankAccount.getAdministrator()).stream()
                .collect(Collectors.toConcurrentMap(BankAccount::getNumber, b -> b));

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

                    String toAccount = row[4];

                    BankAccount to = Optional.ofNullable(bankAccountMap.get(toAccount)).orElseGet(() -> {
                        BankAccount bankAccount = new BankAccount();
                        bankAccount.setNumber(toAccount);
                        bankAccount.setAdministrator(originatingBankAccount.getAdministrator());
                        bankAccountMap.put(toAccount, bankAccount);
                        return bankAccount;
                    });

                    statement.setOriginatingAccount(originatingBankAccount);
                    statement.setDestinationAccount(to);
                    statementList.add(statement);
                }
            }

            this.bankAccountService.save(bankAccountMap.values());
            this.statementService.save(statementList);

        } catch (IOException e) {
            e.printStackTrace();
            LOG.error(String.format("Error importing csv data %s", e));
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    LOG.error(String.format("Error importing csv data %s", e));
                }
            }
        }
    }
}
