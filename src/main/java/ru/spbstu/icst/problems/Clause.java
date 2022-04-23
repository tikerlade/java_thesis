package ru.spbstu.icst.problems;

import ru.spbstu.icst.exceptions.InputException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class Clause implements Iterable<Literal> {
    ArrayList<Literal> literals = new ArrayList<>();

    public Clause() {}

    public Clause(Clause other) {
        this.literals = other.literals;
    }

    public Clause(ArrayList<Literal> literals) {
        this.literals = literals;
    }


    public Clause(String clauseString) throws Exception {
        StringBuilder buffer = new StringBuilder();

        // Remove leading and following spaces
        clauseString = clauseString.strip();

        // In valid clause must be at least two parenthesis
        if (clauseString.length() < 2) {
            throw new InputException("Clauses must have at least two characters: be covered in parentheses.");
        } else if (clauseString.charAt(0) != '(') {
            throw new InputException(String.format("Not allowed character: %s, clause must start with opening parentheses.",
                    clauseString.charAt(0)));
        } else if (clauseString.charAt(clauseString.length()-1) != ')') {
            throw new InputException(String.format("Not allowed character: %s, clause must end with closing parentheses.",
                    clauseString.charAt(clauseString.length()-1)));
        }
        clauseString = clauseString.substring(1, clauseString.length()-1);

        // Collect literals by splitting clause at OR operations
        for (Character ch : clauseString.toCharArray()) {
            // Not reached OR -> collect character to buffer
            if (!BooleanOperation.OR.equalsName(ch.toString())) {
                buffer.append(ch);
            }

            // When OR reached -> try to parse literal
            else {
                Literal newLiteral = new Literal(buffer.toString());
                this.literals.add(newLiteral);
                buffer.setLength(0);
            }
        }

        // At this point buffer still contain something
        Literal lastLiteral = new Literal(buffer.toString());
        this.literals.add(lastLiteral);
    }

    public void addLiteral(Literal literal) {
        this.literals.add(literal);
    }

    public boolean isSatisfyed() {
        boolean isCluaseSatisfyed = false;

        for (Literal literal: literals) {
            isCluaseSatisfyed = isCluaseSatisfyed || literal.getLiteralValue();
        }

        return isCluaseSatisfyed;
    }

    public int size() {
        return this.literals.size();
    }

    public Literal get(int index) {
        return this.literals.get(index);
    }

    @Override
    public String toString() {
        // Represent literals as Strings
        List<String> literalsAsString = literals.stream().map(Literal::toString).toList();

        // Join string representations of literals with | sign
        return "(" + String.join(" | ", literalsAsString) + ")";
    }

    @Override
    public Iterator<Literal> iterator() {
        return this.literals.iterator();
    }

    @Override
    public void forEach(Consumer action) {
        for (Literal literal : this.literals) {
            action.accept(literal);
        }
    }
}
