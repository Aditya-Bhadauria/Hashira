import java.io.FileReader;
import java.util.*;
import java.math.BigInteger;

public class Algo {

    static BigInteger decode(String val, int base) {
        return new BigInteger(val, base);
    }
    
    static BigInteger lagrangeAtZero(BigInteger[] xs, BigInteger[] ys) {
        int k = xs.length;
        BigInteger secret = BigInteger.ZERO;
        for(int j=0; j<k; ++j) {
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;
            for(int m=0; m<k; ++m) {
                if(m == j) continue;
                numerator = numerator.multiply(xs[m].negate());
                denominator = denominator.multiply(xs[j].subtract(xs[m]));
            }
            BigInteger term = ys[j].multiply(numerator).divide(denominator);
            secret = secret.add(term);
        }
        return secret;
    }

    static BigInteger processJSONFileWithoutLib(String filename) throws Exception {
        Scanner scanner = new Scanner(new FileReader(filename));
        StringBuilder sb = new StringBuilder();
        while(scanner.hasNextLine()) {
            sb.append(scanner.nextLine().trim());
        }
        scanner.close();
        String json = sb.toString();

        int keysStart = json.indexOf("\"keys\"");
        int keysEnd = json.indexOf("}", keysStart);
        String keysStr = json.substring(keysStart, keysEnd+1);
        int n = Integer.parseInt(extractValue(keysStr, "\"n\""));
        int k = Integer.parseInt(extractValue(keysStr, "\"k\""));

        List<BigInteger> xs = new ArrayList<>();
        List<BigInteger> ys = new ArrayList<>();

        int pos = keysEnd+1;
        while (pos < json.length()) {
            int keyStart = json.indexOf("\"", pos);
            if (keyStart == -1) break;
            int keyEnd = json.indexOf("\"", keyStart+1);
            if (keyEnd == -1) break;
            String key = json.substring(keyStart+1, keyEnd);
            if (key.equals("keys")) {
                pos = keyEnd + 1;
                continue;
            }
            int objStart = json.indexOf("{", keyEnd);
            int objEnd = findMatchingBrace(json, objStart);
            String objStr = json.substring(objStart, objEnd+1);
            int base = Integer.parseInt(extractStringValue(objStr, "\"base\""));  // FIX HERE
            String value = extractStringValue(objStr, "\"value\"");
            xs.add(BigInteger.valueOf(Integer.parseInt(key)));
            ys.add(new BigInteger(value, base));
            pos = objEnd + 1;
            if (xs.size() >= k) break;
        }
        BigInteger[] xArr = new BigInteger[k];
        BigInteger[] yArr = new BigInteger[k];
        for (int i=0; i<k; i++) {
            xArr[i] = xs.get(i);
            yArr[i] = ys.get(i);
        }
        return lagrangeAtZero(xArr, yArr);
    }

    static String extractValue(String s, String key) {
        int idx = s.indexOf(key);
        if (idx == -1) return null;
        int colon = s.indexOf(":", idx);
        int comma = s.indexOf(",", colon);
        int end = comma != -1 ? comma : s.indexOf("}", colon);
        String val = s.substring(colon+1, end).trim();
        return val;
    }

    static String extractStringValue(String s, String key) {
        int idx = s.indexOf(key);
        if (idx == -1) return null;
        int colon = s.indexOf(":", idx);
        int quoteStart = s.indexOf("\"", colon);
        int quoteEnd = s.indexOf("\"", quoteStart+1);
        return s.substring(quoteStart+1, quoteEnd);
    }

    static int findMatchingBrace(String s, int pos) {
        if (s.charAt(pos) != '{') return -1;
        int depth = 1;
        for (int i = pos+1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) throws Exception {
        String[] jsonFiles = { "TestCase1.json","TestCase2.json"};
        for(String file : jsonFiles) {
            BigInteger secretCode = processJSONFileWithoutLib(file);
            System.out.println("Secret (constant term) for " + file + ": " + secretCode);
        }
    }
}

