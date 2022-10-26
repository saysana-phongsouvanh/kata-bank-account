package io.phongsouvanh.bank.statements;

import java.util.List;

public interface StatementFormatter {
    List<String> format(Statement statement);
}
