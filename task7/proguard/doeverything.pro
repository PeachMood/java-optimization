-injars source.jar
-outjars changed.jar
-libraryjars  <java.home>/jmods/java.base.jmod(!**.jar;!module-info.class)

-keep public class Main {
    public static void main(java.lang.String[]);
}