package net.n2oapp.framework.config.register.scanner;

/**
 * Сравнение сканеров, чтобы опроделить их очередность в N2oMetadataScannerFactory
 */
public interface ScannerComparable extends Comparable<ScannerComparable> {
    Integer getOrder();

    @Override
    default int compareTo(ScannerComparable o) {
       return getOrder().compareTo(o.getOrder());
    }
}
