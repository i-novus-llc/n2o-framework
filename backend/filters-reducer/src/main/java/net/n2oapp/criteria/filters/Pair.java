package net.n2oapp.criteria.filters;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:37
 */
public class Pair<V> {

    private V left;
    private V right;

    public Pair(V left, V right) {
        assert left != null;
        assert right != null;
        this.left = left;
        this.right = right;
    }

    public V getLeft() {
        return left;
    }

    public V getRight() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;

        return left.equals(pair.left) && right.equals(pair.right) || left.equals(pair.right) && right.equals(pair.left);

    }

    @Override
    public int hashCode() {
        return left.hashCode() + right.hashCode();
    }


    @Override
    public String toString() {
        return "Pair{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }
}
