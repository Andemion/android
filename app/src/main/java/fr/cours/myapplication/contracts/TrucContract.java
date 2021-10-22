package fr.cours.myapplication.contracts;

public class TrucContract {

    public static final String TABLE_NAME = "Trucs";

    public static final String COL_ID = "id";
    public static final String COL_LIBELLE = "libelle";
    public static final String COL_VALEUR = "valeur";

    public static final String CREATE_TABLE = "CREATE TABLE"+TABLE_NAME+"("
            +COL_ID+"INTEGER PRIMARY KEY AUTOINCREMENT,"
            +COL_LIBELLE+"TEXT"
            +COL_VALEUR+"TEXT);";
    public static final String DROP_TABLE = "DROP TABLE"+TABLE_NAME;
}
