package com.sforce.ws.codegen.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import com.sforce.ws.bind.NameMapper;
import com.sforce.ws.bind.TypeMapper;
import com.sforce.ws.wsdl.*;

/**
 * @author hhildebrand
 * @since 184
 */
public class SimpleClassMetadata extends ClassMetadata {
    private static final Pattern DASH_PATTERN = Pattern.compile("-");

    public static Collection<String> getEnumerations(SimpleType simpleType, TypeMapper typeMapper) {
        Collection<String> enumerations = new ArrayList<String>();
        for (Enumeration e : simpleType.getRestriction()) {
            enumerations.add(javaName(e, typeMapper));
        }
        return enumerations;
    }

    public static String javaName(Enumeration enumeration, TypeMapper typeMapper) {
        String name = enumeration.getValue();
        int index = name.indexOf(":");
        String subname = index == -1 ? name : name.substring(index + 1);
        if (typeMapper.isKeyWord(subname)) {
            subname = "_" + subname;
        }
        if (subname.indexOf("-") > 0) {
            subname = DASH_PATTERN.matcher(subname).replaceAll("_");
        }
        return subname;
    }

    private final Collection<String> enumerations;

    public SimpleClassMetadata(Schema schema, SimpleType simpleType, TypeMapper typeMapper) {
        this(NameMapper.getPackageName(schema.getTargetNamespace(), typeMapper.getPackagePrefix()), NameMapper
                .getClassName(simpleType.getName()), getEnumerations(simpleType, typeMapper));
    }

    public SimpleClassMetadata(String packageName, String className, Collection<String> enumerations) {
        super(packageName, className);
        this.enumerations = enumerations;
    }

    public Collection<String> getEnumerations() {
        return this.enumerations;
    }

}
