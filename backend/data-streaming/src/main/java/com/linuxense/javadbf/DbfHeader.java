package com.linuxense.javadbf;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author operehod
 * @since 29.10.2015
 * Основная цель - превращение заколовка dbf-документа в байты
 */
public class DbfHeader extends DBFHeader {


    public DbfHeader(int numberOfRecords, List<DBFField> fields) {
        this.numberOfRecords = numberOfRecords;
        this.fieldArray = fields.toArray(new DBFField[fields.size()]);
    }

    public byte[] toBytes() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            write(new DataOutputStream(outputStream));
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
