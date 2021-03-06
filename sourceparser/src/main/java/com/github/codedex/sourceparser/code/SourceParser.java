package com.github.codedex.sourceparser.code;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;

/**
 * @author Fabian Terhorst
 */

public class SourceParser {

    /*file*/
    private static final ByteString PACKAGE = ByteString.encodeUtf8("package");
    private static final ByteString IMPORT = ByteString.encodeUtf8("import");
    private static final ByteString CLASS = ByteString.encodeUtf8("class");
    private static final ByteString INTERFACE = ByteString.encodeUtf8("interface");
    /*syntax*/
    private static final ByteString SEMICOLON = ByteString.encodeUtf8(";");
    private static final ByteString EMPTY = ByteString.encodeUtf8(" ");
    private static final ByteString LINE_BREAK = ByteString.encodeUtf8("\n");
    private static final ByteString BRACKET_OPENED = ByteString.encodeUtf8("{");
    private static final ByteString BRACKET_CLOSED = ByteString.encodeUtf8("}");

    //Todo: only parse public methods?? maybe??
    //Todo: constructor parser
    //Todo: first imports, then search all classes, parse them, then search all interfaces, parse them, search variables, parse them, search methods, parse them (only the method declaration, not the implementation)
    public static void parse(String sourceCode) {
        Buffer buffer = new Buffer();
        buffer.writeUtf8(sourceCode);
        parse(buffer);
    }

    public static void parse(BufferedSource buffer) {
        try {
            int nextType = 0;
            ByteString string;
            while ((string = nextByteString(buffer)) != null) {
                Log.d("string", string.utf8());
                if (nextType == 0) {
                    if (string.equals(PACKAGE)) {
                        nextType = 1;
                        long endIndex = buffer.indexOf(SEMICOLON);
                        Log.d("package", buffer.readUtf8(endIndex));
                        buffer.skip(1);//semicolon
                    }
                } else if (nextType == 1 && string.equals(IMPORT)) {
                    long endIndex = buffer.indexOf(SEMICOLON);
                    Log.d("import", buffer.readUtf8(endIndex));
                    buffer.skip(1);//semicolon
                } else if (string.equals(CLASS)) {
                    nextType = 2;
                    long endIndex = buffer.indexOf(BRACKET_OPENED);
                    Log.d("class", buffer.readUtf8(endIndex));
                    buffer.skip(1);//bracket

                    trimNext(buffer);
                    int opened = 0;
                    while ((string = nextByteString(buffer)) != null) {
                        Log.d("class_imp_log", string.utf8());
                        if(string.equals(BRACKET_OPENED)) {
                            opened++;
                        }
                        if (string.equals(BRACKET_CLOSED)) {
                            if(opened == 0) {
                                break;
                            }
                            opened--;
                        }
                    }
                    //endIndex = buffer.indexOf(BRACKET_CLOSED);
                    //Log.d("class_impl", buffer.readUtf8(endIndex));
                } else if(string.equals(INTERFACE)) {
                    nextType = 2;
                    long endIndex = buffer.indexOf(BRACKET_OPENED);
                    Log.d("interface", buffer.readUtf8(endIndex));
                    buffer.skip(1);//bracket

                    trimNext(buffer);
                    int opened = 0;
                    while ((string = nextByteString(buffer)) != null) {
                        Log.d("interface_imp_log", string.utf8());
                        if(string.equals(BRACKET_OPENED)) {
                            opened++;
                        }
                        if (string.equals(BRACKET_CLOSED)) {
                            if(opened == 0) {
                                break;
                            }
                            opened--;
                        }
                    }
                }
                trimNext(buffer);
            }
        } catch (IOException eof) {
            //File end
        }
        Log.d("result", buffer.toString());//should be empty
    }

    @Nullable
    private static ByteString nextByteString(BufferedSource buffer) throws IOException {
        trimNext(buffer);
        ByteString byteString;
        long endIndex1 = buffer.indexOf(EMPTY);
        long endIndex2 = buffer.indexOf(LINE_BREAK);
        long endIndex = Math.min(endIndex1, endIndex1);
        if (endIndex == -1) {
            if (endIndex1 == -1) {
                endIndex = endIndex2;
            } else if (endIndex2 == -1) {
                endIndex = endIndex1;
            }
        }
        if (endIndex != -1) {
            byteString = buffer.readByteString(endIndex);
        } else {
            if (!buffer.exhausted()) {
                byteString = buffer.readByteString();
            } else {
                return null;
            }
        }
        trimNext(buffer);
        return byteString;
    }

    private static void trimNext(BufferedSource buffer) throws IOException {
        while (buffer.rangeEquals(0, EMPTY) || buffer.rangeEquals(0, LINE_BREAK)) {
            buffer.skip(1);
        }
    }
}
