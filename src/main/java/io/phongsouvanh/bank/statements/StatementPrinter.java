package io.phongsouvanh.bank.statements;

import java.util.List;

public interface StatementPrinter {
    void print(List<String> formattedStatement);
}
