package org.idea.plugin.duplicatelines;

final class Range {
    private final int start;
    private final int end;

    Range(final int begin, final int end) {
        this.start = begin;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}