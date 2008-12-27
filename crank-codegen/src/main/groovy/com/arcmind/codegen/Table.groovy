package com.arcmind.codegen

class Table {
    String name    
    List <Column> columns = []
    Set <String> primaryKeys = []
    List <Key> exportedKeys = []
    List <Key> importedKeys = []
    public String toString() {
        "Table (name=${name}, columns=${columns}, primaryKeys=${primaryKeys} )"
    }
}