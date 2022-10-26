package io.phongsouvanh.bank.statements;

import java.util.List;

public class StatementGridPrinter implements StatementPrinter {
    @Override
    public void print(List<String> formattedStatement) {
        formattedStatement.forEach(System.out::println);
    }

}
