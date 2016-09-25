package com.github.codedex.sourceparser;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;

import okio.Buffer;
import okio.ByteString;

/**
 * Parse imports from file
 */

public class ImportParser {

    private static final ByteString IMPORT_SYNTAX = ByteString.encodeUtf8("import");
    private static final ByteString IMPORT_END_SYNTAX = ByteString.encodeUtf8(";");
    private static final ByteString EMPTY = ByteString.encodeUtf8(" ");

    @Nullable
    public static ArrayList<String> parseImports(String sourceCode) {
        Buffer buffer = new Buffer();
        buffer.writeUtf8(sourceCode);
        try {
            ArrayList<String> imports = new ArrayList<>();
            long startIndex;
            while ((startIndex = buffer.indexOf(IMPORT_SYNTAX)) != -1) {
                buffer.skip(startIndex + 6);
                while (buffer.indexOf(EMPTY) == 0) {
                    buffer.skip(1);
                }
                long endIndex = buffer.indexOf(IMPORT_END_SYNTAX);
                imports.add(buffer.readByteString(endIndex).utf8());
            }
            return imports;
        } catch (IOException io) {
            //Source has no imports
            return null;
        }
    }
}