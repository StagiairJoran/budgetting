package be.ghostwritertje.services.budgetting.csv;


import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.domain.budgetting.Statement;
import be.ghostwritertje.services.budgetting.BankAccountService;
import be.ghostwritertje.services.budgetting.StatementService;
import be.ghostwritertje.webapp.form.Display;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by jorandeboever
 * on 18/03/16.
 */
@Service
public class CsvService {
    private static final Pattern PATTERN = Pattern.compile(",", Pattern.LITERAL);
    private static final Logger LOG = LogManager.getLogger();

    private final StatementService statementService;
    private final BankAccountService bankAccountService;

    @Autowired
    public CsvService(StatementService statementService, BankAccountService bankAccountService) {
        this.statementService = statementService;
        this.bankAccountService = bankAccountService;
    }

    private static final Map<BankType, Options> SUPPORTED_BANKS = ImmutableMap.of(
            BankType.KEYTRADE, new Options(";", s -> s.endsWith("EUR"), "dd.MM.yyyy", 5, 1, 3, 4),
            BankType.BELFIUS, new Options(";", s -> s.startsWith("BE"), "dd/MM/yyyy", 10, 1, 4, 8)
    );

    public void uploadCSVFile(String fileUrl, BankAccount originatingBankAccount, BankType bankType) {
        this.uploadCsv(fileUrl, originatingBankAccount, SUPPORTED_BANKS.get(bankType));
    }

    private void uploadCsv(String fileUrl, BankAccount originatingBankAccount, Options options) {
        String line;

        Map<String, BankAccount> bankAccountMap = this.bankAccountService.findByAdministrator(originatingBankAccount.getAdministrator())
                .stream()
                .collect(Collectors.toConcurrentMap(BankAccount::getNumber, b -> b));

        Collection<Statement> statementList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileUrl))) {
            while ((line = br.readLine()) != null) {

                String[] row = line.split(options.cvsSplitBy);

                if (row.length > 0 && options.identifyStatementPredicate.test(line)) {

                    Statement statement = new Statement();
                    statement.setAmount(BigDecimal.valueOf(Double.parseDouble(PATTERN.matcher(row[options.rowNumberAmount])
                            .replaceAll(Matcher.quoteReplacement(".")))));

                    LocalDate date = LocalDate.parse(row[options.rowNumberDate], DateTimeFormatter.ofPattern(options.datePattern));
                    statement.setDate(date);


                    String toAccount = StringUtils.replaceChars(row[options.rowNumberToAccount], " ", "");
                    BankAccount to = bankAccountMap.get(toAccount);

                    if (to == null && toAccount != null && !"-".equalsIgnoreCase(toAccount)) {
                        to = new BankAccount();
                        to.setNumber(toAccount);
                        to.setAdministrator(originatingBankAccount.getAdministrator());
                        bankAccountMap.put(toAccount, to);
                    }

                    statement.setDescription(StringUtils.abbreviate(row[options.rowNumberDescription], 2000));
                    statement.setOriginatingAccount(originatingBankAccount);
                    statement.setDestinationAccount(to);

                    statement.setCsvLine(StringUtils.abbreviate(line, 8000));
                    statementList.add(statement);
                }
            }

            this.bankAccountService.save(bankAccountMap.values());
            this.statementService.save(statementList);

        } catch (IOException e) {
            e.printStackTrace();
            LOG.error(String.format("IO exception in uploadCsv:%s", e));

        }
    }

    public enum BankType implements Display, Serializable {
        KEYTRADE, BELFIUS;

        private static final long serialVersionUID = 5475356075454193997L;

        @Override
        public String getId() {
            return this.name();
        }

        @Override
        public String getDisplayValue() {
            return this.name();
        }

    }

    private static class Options {
        private String cvsSplitBy = ";";
        private Predicate<String> identifyStatementPredicate = s -> s.endsWith("EUR");
        private String datePattern = "dd.MM.yyyy";
        ;
        private int rowNumberAmount = 5;
        private int rowNumberDate = 1;
        private int rowNumberToAccount = 3;
        private int rowNumberDescription = 4;

        Options(
                String cvsSplitBy,
                Predicate<String> identifyStatementPredicate,
                String datePattern,
                int rowNumberAmount,
                int rowNumberDate,
                int rowNumberToAccount,
                int rowNumberDescription
        ) {
            this.cvsSplitBy = cvsSplitBy;
            this.identifyStatementPredicate = identifyStatementPredicate;
            this.datePattern = datePattern;
            this.rowNumberAmount = rowNumberAmount;
            this.rowNumberDate = rowNumberDate;
            this.rowNumberToAccount = rowNumberToAccount;
            this.rowNumberDescription = rowNumberDescription;
        }
    }
}
