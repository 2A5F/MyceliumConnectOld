package co.volight.math;

import javax.annotation.Nonnull;
import java.util.Iterator;

public class Range implements Iterable<Integer> {
    public int from;
    public int to;

    public Range(int from, int to) {
        assert to >= from;
        this.from = from;
        this.to = to;
    }

    public int size() {
        return to - from;
    }

    @Nonnull @Override
    public Iterator<Integer> iterator() {
        return new RangeIterator(this);
    }
}

class RangeIterator implements Iterator<Integer> {
    Range range;
    int index;

    public RangeIterator(Range range) {
        this.range = range;
        index = range.from;
    }

    @Override
    public boolean hasNext() {
        return index <= range.to;
    }

    @Override
    public Integer next() {
        return index++;
    }
}