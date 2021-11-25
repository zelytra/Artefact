package fr.zelytra.novaStructura.utils;

public enum Message {

    PLAYER_PREFIX("§8[§6Artefact§8]§r "),
    CONSOLE_PREFIX("§8[§6Artefact§8]§r "),
    CONSOLE_STARTUP("§6\n" +
            "    ___         __       ____           __ \n" +
            "   /   |  _____/ /____  / __/___ ______/ /_\n" +
            "  / /| | / ___/ __/ _ \\/ /_/ __ `/ ___/ __/\n" +
            " / ___ |/ /  / /_/  __/ __/ /_/ / /__/ /_  \n" +
            "/_/  |_/_/   \\__/\\___/_/  \\__,_/\\___/\\__/  \n" +
            "                                           \n§r");

    private final String message;

    Message(String message) {
        this.message = message;
    }

    public String getMsg() {
        return message;
    }

    @Override
    public String toString(){
        return message;
    }

}
