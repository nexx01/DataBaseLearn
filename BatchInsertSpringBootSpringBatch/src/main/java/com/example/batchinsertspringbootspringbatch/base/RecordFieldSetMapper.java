package com.example.batchinsertspringbootspringbatch.base;

import com.example.batchinsertspringbootspringbatch.base.dto.Transaction;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RecordFieldSetMapper implements FieldSetMapper<Transaction> {
    @Override
    public Transaction mapFieldSet(FieldSet fieldSet) throws BindException {
        var formatter = DateTimeFormatter.ofPattern("d/M/yyy");
        var transaction = new Transaction();

        transaction.setUsername(fieldSet.readString("username"));
        transaction.setUserId(fieldSet.readInt("userid"));
        transaction.setAmount(fieldSet.readDouble(3));
        var dateString = fieldSet.readString(2);

        transaction.setTransactionDate(LocalDate.parse(dateString, formatter).atStartOfDay());
        return transaction;
    }
}
