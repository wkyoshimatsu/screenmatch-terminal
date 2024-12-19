package me.wky.screenmatch_terminal.model;

public enum Category {
    ACAO("Action"),
    AVENTURA("Adventure"),
    ANIMACAO("Animation"),
    BIOGRAFIA("Biography"),
    COMEDIA("Comedy"),
    CRIME("Crime"),
    DRAMA("Drama"),
    FAMILIA("Family"),
    FANTASIA("Fantasy"),
    NOIR("Film-Noir"),
    HISTORICO("History"),
    TERROR("Horror"),
    MUSICA("Music"),
    MUSICAL("Musical"),
    MISTERIO("Mystery"),
    ROMANCE("Romance"),
    FICCAO_CIENTIFICA("Sci-Fi"),
    ESPORTE("Sport"),
    THRILLER("Thriller"),
    GUERRA("War"),
    WESTERN("Western");

    private String omdbCategory;

    private Category(String omdbCategory) {
        this.omdbCategory = omdbCategory;
    }

    public static Category fromString(String text) {
        for (Category categoria : Category.values()) {
            if (categoria.omdbCategory.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}
