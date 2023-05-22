-injars source.jar
-outjars withsignatures.jar
-libraryjars  <java.home>/jmods/java.base.jmod(!**.jar;!module-info.class)

-keep public class Main {
    public static void main(java.lang.String[]);
}

-keep public class InlineExpansion {
    private static int multiplyByTwo(int);
}

-keep public class RedundantVariables {
    public static int sum(int, int, int);
}