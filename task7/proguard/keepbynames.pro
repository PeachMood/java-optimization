-injars source.jar
-outjars withnames.jar
-libraryjars  <java.home>/jmods/java.base.jmod(!**.jar;!module-info.class)

-keep public class Main {
    public static void main(java.lang.String[]);
}

-keep public class DuplicatedCode {
    *** spoil(***);
    *** change(***);
}

-keep public class BubbleSort {
    *** sort(***);
}