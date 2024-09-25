package co.tz.settlo.api.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class UniqueIdGenerator {
    private static final String BASE36CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new Random();

    public String generate() {
        StringBuilder sb = new StringBuilder(6);

        // Generate Unique 6 Characters
        for (int i = 0; i < 6; i++) {
            sb.append(BASE36CHARS.charAt(RANDOM.nextInt(36)));
        }

        // Hash timestamp
        long timestamp = System.currentTimeMillis(); // Get current timestamp in milliseconds
        String hashedTimestamp = Long.toString(timestamp, 36).toUpperCase(); // Convert timestamp to Base36

        return hashedTimestamp +"-"+ sb;
    }
}
