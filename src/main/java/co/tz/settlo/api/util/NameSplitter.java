package co.tz.settlo.api.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NameSplitter {

    public static class Name {
        private String firstName;
        private String lastName;

        public Name(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        @Override
        public String toString() {
            return "Name{firstName='" + firstName + "', lastName='" + lastName + "'}";
        }
    }

    public static Name splitName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return new Name("", "");
        }

        // Remove any leading or trailing whitespace
        fullName = fullName.trim();

        // Split the name into parts
        List<String> nameParts = new ArrayList<>(Arrays.asList(fullName.split("\\s+")));

        // Handle special cases
        nameParts = handlePrefixesAndSuffixes(nameParts);

        // Determine first name and last name
        String firstName = "";
        String lastName = "";

        if (nameParts.size() == 1) {
            // Only one name provided, assume it's the first name
            firstName = nameParts.get(0);
        } else if (nameParts.size() == 2) {
            // Typical case: first name and last name
            firstName = nameParts.get(0);
            lastName = nameParts.get(1);
        } else {
            // Multiple names: assume last part is the last name, the rest is the first name
            lastName = nameParts.get(nameParts.size() - 1);
            firstName = String.join(" ", nameParts.subList(0, nameParts.size() - 1));
        }

        return new Name(firstName, lastName);
    }

    private static List<String> handlePrefixesAndSuffixes(List<String> nameParts) {
        List<String> prefixes = Arrays.asList("Mr", "Mrs", "Ms", "Miss", "Dr", "Prof");
        List<String> suffixes = Arrays.asList("Jr", "Sr", "II", "III", "IV", "Ph.D", "MD", "DDS", "Esq");

        // Remove prefixes
        while (!nameParts.isEmpty() && prefixes.contains(nameParts.get(0).replace(".", ""))) {
            nameParts.remove(0);
        }

        // Handle suffixes
        if (nameParts.size() > 1) {
            String lastPart = nameParts.get(nameParts.size() - 1).replace(".", "");
            if (suffixes.contains(lastPart)) {
                // Move the suffix to the end of the last name
                String lastNameWithSuffix = nameParts.get(nameParts.size() - 2) + " " + lastPart;
                nameParts.remove(nameParts.size() - 1);
                nameParts.set(nameParts.size() - 1, lastNameWithSuffix);
            }
        }

        return nameParts;
    }
}