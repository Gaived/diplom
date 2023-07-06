import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.*;
import java.util.*;


public class BooleanSearchEngine implements SearchEngine {
    private Map<String, List<PageEntry>> foundWords = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        List<File> filesInDirectory = new ArrayList<>(Arrays.asList(pdfsDir.listFiles()));
        for (int i = 0; i < filesInDirectory.size(); i++) {
            var doc = new PdfDocument(new PdfReader(filesInDirectory.get(i)));
            for (int pageNumber = 1; pageNumber < doc.getNumberOfPages(); pageNumber++) {
                String text = PdfTextExtractor.getTextFromPage(doc.getPage(pageNumber));
                var words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> wordAsKey = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    wordAsKey.put(word, wordAsKey.getOrDefault(word, 0) + 1);
                }
                for (var word : wordAsKey.entrySet()) {
                    List<PageEntry> numberOfWords;
                    if (foundWords.containsKey(word.getKey())) {
                        numberOfWords = foundWords.get(word.getKey());
                    } else {
                        numberOfWords = new ArrayList<>();
                    }
                    numberOfWords.add(new PageEntry(filesInDirectory.get(i).getName(), pageNumber, word.getValue()));
                    foundWords.put(word.getKey(), numberOfWords);
                }
            }
        }
        for (List<PageEntry> entries : foundWords.values()) {
            Collections.sort(entries, Collections.reverseOrder());
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        return foundWords.getOrDefault(word.toLowerCase(), List.of());
    }

    public Map<String, List<PageEntry>> getFoundWords() {
        return foundWords;
    }

}