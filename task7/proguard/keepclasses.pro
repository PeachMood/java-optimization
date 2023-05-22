-injars source.jar
-outjars withclasses.jar
-libraryjars  <java.home>/jmods/java.base.jmod(!**.jar;!module-info.class)

-keep public class Main {
    public static void main(java.lang.String[]);
}

-keep public class EmptyMethod
-keep public class RedundantVariables