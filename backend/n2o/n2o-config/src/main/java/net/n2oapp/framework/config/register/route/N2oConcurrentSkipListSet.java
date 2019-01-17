package net.n2oapp.framework.config.register.route;

import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * Реализация сортированного потокобезопасного Set, в котором при добавлении элементы перезаписываются
 */
public class N2oConcurrentSkipListSet<E> extends AbstractSet<E>
        implements NavigableSet<E>, Cloneable, java.io.Serializable {

    private static final long serialVersionUID = -2479143111061671589L;

    /**
     * The underlying map. Uses Boolean.TRUE as value for each
     * element.  This field is declared final for the sake of thread
     * safety, which entails some ugliness in clone().
     */
    private final ConcurrentNavigableMap<E, Object> m;

    /**
     * Constructs a new, empty set that orders its elements according to
     * their {@linkplain Comparable natural ordering}.
     */
    public N2oConcurrentSkipListSet() {
        m = new N2oConcurrentSkipListMap<E, Object>();
    }

    /**
     * Constructs a new, empty set that orders its elements according to
     * the specified comparator.
     *
     * @param comparator the comparator that will be used to order this set.
     *                   If {@code null}, the {@linkplain Comparable natural
     *                   ordering} of the elements will be used.
     */
    public N2oConcurrentSkipListSet(Comparator<? super E> comparator) {
        m = new N2oConcurrentSkipListMap<E, Object>(comparator);
    }

    /**
     * Constructs a new set containing the elements in the specified
     * collection, that orders its elements according to their
     * {@linkplain Comparable natural ordering}.
     *
     * @param c The elements that will comprise the new set
     * @throws ClassCastException   if the elements in {@code c} are
     *                              not {@link Comparable}, or are not mutually comparable
     * @throws NullPointerException if the specified collection or any
     *                              of its elements are null
     */
    public N2oConcurrentSkipListSet(Collection<? extends E> c) {
        m = new N2oConcurrentSkipListMap<E, Object>();
        addAll(c);
    }

    /**
     * Constructs a new set containing the same elements and using the
     * same ordering as the specified sorted set.
     *
     * @param s sorted set whose elements will comprise the new set
     * @throws NullPointerException if the specified sorted set or any
     *                              of its elements are null
     */
    public N2oConcurrentSkipListSet(SortedSet<E> s) {
        m = new N2oConcurrentSkipListMap<E, Object>(s.comparator());
        addAll(s);
    }

    /**
     * For use by submaps
     */
    N2oConcurrentSkipListSet(ConcurrentNavigableMap<E, Object> m) {
        this.m = m;
    }


    /* ---------------- Set operations -------------- */

    /**
     * Returns the number of elements in this set.  If this set
     * contains more than {@code Integer.MAX_VALUE} elements, it
     * returns {@code Integer.MAX_VALUE}.
     *
     * <p>Beware that, unlike in most collections, this method is
     * <em>NOT</em> a constant-time operation. Because of the
     * asynchronous nature of these sets, determining the current
     * number of elements requires traversing them all to count them.
     * Additionally, it is possible for the size to change during
     * execution of this method, in which case the returned result
     * will be inaccurate. Thus, this method is typically not very
     * useful in concurrent applications.
     *
     * @return the number of elements in this set
     */
    public int size() {
        return m.size();
    }

    /**
     * Returns {@code true} if this set contains no elements.
     *
     * @return {@code true} if this set contains no elements
     */
    public boolean isEmpty() {
        return m.isEmpty();
    }

    /**
     * Returns {@code true} if this set contains the specified element.
     * More formally, returns {@code true} if and only if this set
     * contains an element {@code e} such that {@code o.equals(e)}.
     *
     * @param o object to be checked for containment in this set
     * @return {@code true} if this set contains the specified element
     * @throws ClassCastException   if the specified element cannot be
     *                              compared with the elements currently in this set
     * @throws NullPointerException if the specified element is null
     */
    public boolean contains(Object o) {
        return m.containsKey(o);
    }

    /**
     * Adds the specified element to this set if it is not already present.
     * More formally, adds the specified element {@code e} to this set if
     * the set contains no element {@code e2} such that {@code e.equals(e2)}.
     * If this set already contains the element, the call leaves the set
     * unchanged and returns {@code false}.
     *
     * @param e element to be added to this set
     * @return {@code true} if this set did not already contain the
     * specified element
     * @throws ClassCastException   if {@code e} cannot be compared
     *                              with the elements currently in this set
     * @throws NullPointerException if the specified element is null
     */
    public boolean add(E e) {
        return m.putIfAbsent(e, Boolean.TRUE) == null;
    }

    /**
     * Removes the specified element from this set if it is present.
     * More formally, removes an element {@code e} such that
     * {@code o.equals(e)}, if this set contains such an element.
     * Returns {@code true} if this set contained the element (or
     * equivalently, if this set changed as a result of the call).
     * (This set will not contain the element once the call returns.)
     *
     * @param o object to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws ClassCastException   if {@code o} cannot be compared
     *                              with the elements currently in this set
     * @throws NullPointerException if the specified element is null
     */
    public boolean remove(Object o) {
        return m.remove(o, Boolean.TRUE);
    }

    /**
     * Removes all of the elements from this set.
     */
    public void clear() {
        m.clear();
    }

    /**
     * Returns an iterator over the elements in this set in ascending order.
     *
     * @return an iterator over the elements in this set in ascending order
     */
    public Iterator<E> iterator() {
        return m.navigableKeySet().iterator();
    }

    /**
     * Returns an iterator over the elements in this set in descending order.
     *
     * @return an iterator over the elements in this set in descending order
     */
    public Iterator<E> descendingIterator() {
        return m.descendingKeySet().iterator();
    }


    /* ---------------- AbstractSet Overrides -------------- */

    /**
     * Compares the specified object with this set for equality.  Returns
     * {@code true} if the specified object is also a set, the two sets
     * have the same size, and every member of the specified set is
     * contained in this set (or equivalently, every member of this set is
     * contained in the specified set).  This definition ensures that the
     * equals method works properly across different implementations of the
     * set interface.
     *
     * @param o the object to be compared for equality with this set
     * @return {@code true} if the specified object is equal to this set
     */
    public boolean equals(Object o) {
        // Override AbstractSet version to avoid calling size()
        if (o == this)
            return true;
        if (!(o instanceof Set))
            return false;
        Collection<?> c = (Collection<?>) o;
        try {
            return containsAll(c) && c.containsAll(this);
        } catch (ClassCastException unused) {
            return false;
        } catch (NullPointerException unused) {
            return false;
        }
    }

    /**
     * Removes from this set all of its elements that are contained in
     * the specified collection.  If the specified collection is also
     * a set, this operation effectively modifies this set so that its
     * value is the <i>asymmetric set difference</i> of the two sets.
     *
     * @param c collection containing elements to be removed from this set
     * @return {@code true} if this set changed as a result of the call
     * @throws ClassCastException   if the types of one or more elements in this
     *                              set are incompatible with the specified collection
     * @throws NullPointerException if the specified collection or any
     *                              of its elements are null
     */
    public boolean removeAll(Collection<?> c) {
        // Override AbstractSet version to avoid unnecessary call to size()
        boolean modified = false;
        for (Object e : c)
            if (remove(e))
                modified = true;
        return modified;
    }

    /* ---------------- Relational operations -------------- */

    /**
     * @throws ClassCastException   {@inheritDoc}
     * @throws NullPointerException if the specified element is null
     */
    public E lower(E e) {
        return m.lowerKey(e);
    }

    /**
     * @throws ClassCastException   {@inheritDoc}
     * @throws NullPointerException if the specified element is null
     */
    public E floor(E e) {
        return m.floorKey(e);
    }

    /**
     * @throws ClassCastException   {@inheritDoc}
     * @throws NullPointerException if the specified element is null
     */
    public E ceiling(E e) {
        return m.ceilingKey(e);
    }

    /**
     * @throws ClassCastException   {@inheritDoc}
     * @throws NullPointerException if the specified element is null
     */
    public E higher(E e) {
        return m.higherKey(e);
    }

    public E pollFirst() {
        Map.Entry<E, Object> e = m.pollFirstEntry();
        return (e == null) ? null : e.getKey();
    }

    public E pollLast() {
        Map.Entry<E, Object> e = m.pollLastEntry();
        return (e == null) ? null : e.getKey();
    }


    /* ---------------- SortedSet operations -------------- */


    public Comparator<? super E> comparator() {
        return m.comparator();
    }

    /**
     * @throws java.util.NoSuchElementException {@inheritDoc}
     */
    public E first() {
        return m.firstKey();
    }

    /**
     * @throws java.util.NoSuchElementException {@inheritDoc}
     */
    public E last() {
        return m.lastKey();
    }

    /**
     * @throws ClassCastException       {@inheritDoc}
     * @throws NullPointerException     if {@code fromElement} or
     *                                  {@code toElement} is null
     * @throws IllegalArgumentException {@inheritDoc}
     */
    public NavigableSet<E> subSet(E fromElement,
                                  boolean fromInclusive,
                                  E toElement,
                                  boolean toInclusive) {
        return new N2oConcurrentSkipListSet<E>
                (m.subMap(fromElement, fromInclusive,
                        toElement, toInclusive));
    }

    /**
     * @throws ClassCastException       {@inheritDoc}
     * @throws NullPointerException     if {@code toElement} is null
     * @throws IllegalArgumentException {@inheritDoc}
     */
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        return new N2oConcurrentSkipListSet<E>(m.headMap(toElement, inclusive));
    }

    /**
     * @throws ClassCastException       {@inheritDoc}
     * @throws NullPointerException     if {@code fromElement} is null
     * @throws IllegalArgumentException {@inheritDoc}
     */
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        return new N2oConcurrentSkipListSet<E>(m.tailMap(fromElement, inclusive));
    }

    /**
     * @throws ClassCastException       {@inheritDoc}
     * @throws NullPointerException     if {@code fromElement} or
     *                                  {@code toElement} is null
     * @throws IllegalArgumentException {@inheritDoc}
     */
    public NavigableSet<E> subSet(E fromElement, E toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    /**
     * @throws ClassCastException       {@inheritDoc}
     * @throws NullPointerException     if {@code toElement} is null
     * @throws IllegalArgumentException {@inheritDoc}
     */
    public NavigableSet<E> headSet(E toElement) {
        return headSet(toElement, false);
    }

    /**
     * @throws ClassCastException       {@inheritDoc}
     * @throws NullPointerException     if {@code fromElement} is null
     * @throws IllegalArgumentException {@inheritDoc}
     */
    public NavigableSet<E> tailSet(E fromElement) {
        return tailSet(fromElement, true);
    }

    /**
     * Returns a reverse order view of the elements contained in this set.
     * The descending set is backed by this set, so changes to the set are
     * reflected in the descending set, and vice-versa.
     *
     * <p>The returned set has an ordering equivalent to
     * {@link Collections#reverseOrder(Comparator) Collections.reverseOrder}{@code (comparator())}.
     * The expression {@code s.descendingSet().descendingSet()} returns a
     * view of {@code s} essentially equivalent to {@code s}.
     *
     * @return a reverse order view of this set
     */
    public NavigableSet<E> descendingSet() {
        return new N2oConcurrentSkipListSet<E>(m.descendingMap());
    }

    /**
     * Returns a {@link Spliterator} over the elements in this set.
     *
     * <p>The {@code Spliterator} reports {@link Spliterator#CONCURRENT},
     * {@link Spliterator#NONNULL}, {@link Spliterator#DISTINCT},
     * {@link Spliterator#SORTED} and {@link Spliterator#ORDERED}, with an
     * encounter order that is ascending order.  Overriding implementations
     * should document the reporting of additional characteristic values.
     *
     * <p>The spliterator's comparator (see
     * {@link java.util.Spliterator#getComparator()}) is {@code null} if
     * the set's comparator (see {@link #comparator()}) is {@code null}.
     * Otherwise, the spliterator's comparator is the same as or imposes the
     * same total ordering as the set's comparator.
     *
     * @return a {@code Spliterator} over the elements in this set
     * @since 1.8
     */
    @SuppressWarnings("unchecked")
    public Spliterator<E> spliterator() {
        if (m instanceof N2oConcurrentSkipListMap)
            return ((N2oConcurrentSkipListMap<E, ?>) m).keySpliterator();
        else
            return (Spliterator<E>) ((N2oConcurrentSkipListMap.SubMap<E, ?>) m).keyIterator();
    }
}
