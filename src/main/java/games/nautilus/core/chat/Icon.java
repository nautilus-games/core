package games.nautilus.core.chat;

public enum Icon {

    GEM('⠀'),
    GEM_2('⠁'),
    GEM_3('⠂'),
    TEA('⠃'),
    CAP('⠄'),
    CLAP('⠅'),
    COOKIE('⠆'),
    DOLLAR('⠇'),
    GLOBE('⠈'),
    EZ('⠉'),
    FLOWER('⠊'),
    BAN('⠋'),
    GG('⠌'),
    BEE('⠍'),
    GHOST('⠎'),
    PUMPKIN('⠏'),
    PALM_TREE('⠐'),
    PINATA('⠑'),
    POPCORN('⠒'),
    PRIDE('⠓'),
    ROSE('⠔'),
    PIZZA('⠕'),
    GALAXY('⠖'),
    SALT('⠗'),
    SPARKLES('⠘'),
    STAR('⠙'),
    BEAR('⠚'),
    THUMBS_UP('⠛'),
    THUMBS_DOWN('⠜'),
    TROPHY('⠝'),
    TOILET('⠞'),
    NONE(' ');

    private final char c;

    Icon(char c) {
        this.c = c;
    }

    public char getChar() { return c; }

}
