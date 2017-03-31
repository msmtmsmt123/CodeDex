package com.github.codedex.sourceparser.entity.project.specific.javalang;

import com.github.codedex.sourceparser.entity.project.MetaPackage;

/**
 * Created by IPat (Local) on 31.03.2017.
 */

final class MetaJavaLangPackage extends MetaPackage {

    private final static MetaJavaLangPackage SINGLETON = new MetaJavaLangPackage();

    private MetaJavaLangPackage() {
        // Passing null as the superclass parameter is an absolute exception and only allowed here, as java.lang.Object is a root class
        super("lang", new MetaPackage("java"));
    }

    public static MetaJavaLangPackage getMetaPackage() {
        return SINGLETON;
    }
}
