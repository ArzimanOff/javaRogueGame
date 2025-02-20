package org.arzimanoff.model.map.generator;

import org.arzimanoff.model.common.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
class DisjointSet {
    private final Map<Position, Position> parent = new HashMap<>();

    public DisjointSet(Set<Position> positions) {
        for (Position pos : positions) {
            parent.put(pos, pos);
        }
    }

    public Position find(Position pos) {
        if (parent.get(pos) != pos) {
            parent.put(pos, find(parent.get(pos)));
        }
        return parent.get(pos);
    }

    public void union(Position posA, Position posB) {
        Position rootA = find(posA);
        Position rootB = find(posB);
        if (rootA != rootB) {
            parent.put(rootA, rootB);
        }
    }
}
