package com.linuxense.javadbf;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author operehod
 * @since 29.10.2015
 * Основная цель - превращение одной записи dbf-документа в байты
 */
public class DbfRecord {
    private List values;
    private List<DBFField> fieldArray;
    private String encoding;

    public DbfRecord(List values, List<DBFField> fields, String encoding) {
        this.fieldArray = fields;
        this.values = values;
        this.encoding = encoding;
    }

    public byte[] toBytes() {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            DataOutputStream dataStream = new DataOutputStream(byteStream);
            writeValues(dataStream);
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Метод полностью скопипасчен из DBWriter. Все потому, что в оригинальном методе был баг, который не учитыват что в UTF-8 кодировке русские символы часто
     * представлены больше чем одним байтом на симовл.
     */
    protected void writeValues(DataOutput dataOutput)
            throws IOException {

        dataOutput.write((byte) ' ');
        int i = 0;
        for (DBFField dbfField : fieldArray) {
            Object value = values.get(i++);

            switch (dbfField.getDataType()) {

                case 'C':
                    if (value != null) {
                        String str_value = value.toString();
                        //весь копипаст нужен был из-за этого, потому что в методе textPadding был баг
                        dataOutput.write(textPadding(str_value, encoding, dbfField.getFieldLength()));
                    } else {
                        dataOutput.write(textPadding("", encoding, dbfField.getFieldLength()));
                    }

                    break;

                case 'D':
                    if (value != null) {
                        GregorianCalendar calendar = new GregorianCalendar();
                        calendar.setTime((Date) value);
                        dataOutput.write(String.valueOf(calendar.get(Calendar.YEAR)).getBytes());
                        dataOutput.write(Utils.textPadding(String.valueOf(calendar.get(Calendar.MONTH) + 1), encoding, 2, Utils.ALIGN_RIGHT, (byte) '0'));
                        dataOutput.write(Utils.textPadding(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)), encoding, 2, Utils.ALIGN_RIGHT, (byte) '0'));
                    } else {

                        dataOutput.write("        ".getBytes());
                    }

                    break;

                case 'F':

                    if (value != null) {

                        dataOutput.write(Utils.doubleFormating((Double) value, encoding, dbfField.getFieldLength(), dbfField.getDecimalCount()));
                    } else {

                        dataOutput.write(Utils.textPadding("?", encoding, dbfField.getFieldLength(), Utils.ALIGN_RIGHT));
                    }

                    break;

                case 'N':

                    if (value != null) {

                        dataOutput.write(
                                Utils.doubleFormating((Double) value, encoding, dbfField.getFieldLength(), dbfField.getDecimalCount()));
                    } else {

                        dataOutput.write(
                                Utils.textPadding("?", encoding, dbfField.getFieldLength(), Utils.ALIGN_RIGHT));
                    }

                    break;
                case 'L':

                    if (value != null) {
                        if (value == Boolean.TRUE) {
                            dataOutput.write((byte) 'T');
                        } else {

                            dataOutput.write((byte) 'F');
                        }
                    } else {
                        dataOutput.write((byte) '?');
                    }

                    break;
                case 'M':
                    break;

                default:
                    throw new DBFException("Unknown field type " + dbfField.getDataType());
            }
        }
    }


    protected static byte[] textPadding(String text, String characterSetName, int length) throws
            java.io.UnsupportedEncodingException {

        byte paddingByte = (byte) ' ';
        byte[] bytes = text.getBytes(characterSetName);

        if (bytes.length >= length) {
            return Arrays.copyOfRange(bytes, 0, length);
        }

        byte byte_array[] = new byte[length];
        Arrays.fill(byte_array, paddingByte);
        System.arraycopy(bytes, 0, byte_array, 0, bytes.length);
        return byte_array;
    }


}
