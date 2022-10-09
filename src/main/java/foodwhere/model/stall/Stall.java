package foodwhere.model.stall;

import static foodwhere.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import foodwhere.model.detail.Detail;

/**
 * Represents a Stall in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Stall {

    // Identity fields
    private final Name name;

    // Data fields
    private final Address address;
    private final Set<Detail> details = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Stall(Name name, Address address, Set<Detail> details) {
        requireAllNonNull(name, address, details);
        this.name = name;
        this.address = address;
        this.details.addAll(details);
    }

    public Name getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable detail set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Detail> getDetails() {
        return Collections.unmodifiableSet(details);
    }

    /**
     * Returns true if both stalls have the same name.
     * This defines a weaker notion of equality between two stalls.
     */
    public boolean isSameStall(Stall otherStall) {
        if (otherStall == this) {
            return true;
        }

        return otherStall != null
                && otherStall.getName().equals(getName());
    }

    /**
     * Returns true if both stalls have the same identity and data fields.
     * This defines a stronger notion of equality between two stalls.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Stall)) {
            return false;
        }

        Stall otherStall = (Stall) other;
        return otherStall.getName().equals(getName())
                && otherStall.getAddress().equals(getAddress())
                && otherStall.getDetails().equals(getDetails());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, address, details);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append("; Address: ")
                .append(getAddress());

        Set<Detail> details = getDetails();
        if (!details.isEmpty()) {
            builder.append("; Details: ");
            details.forEach(builder::append);
        }
        return builder.toString();
    }

}