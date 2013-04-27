package org.idea.plugin.duplicatelines;

final class Range {
    private final int start;
    private final int end;

    Range(final int begin, final int end) {
        this.start = begin;
        this.end = end;
    }

    public int size() {
        return this.end - this.start;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}