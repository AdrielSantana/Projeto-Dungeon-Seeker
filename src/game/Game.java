package game;

/**
 * This class is the main class of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game. Users
 * can walk around some scenery. That's all. It should really be extended
 * to make it more interesting!
 * 
 * To play this game, create an instance of this class and call the "play"
 * method.
 * 
 * This main class creates and initialises all the others: it creates all
 * rooms, creates the parser and starts the game. It also evaluates and
 * executes the commands that the parser returns.
 * 
 * @author Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game {
    private Parser parser;
    private Room currentRoom;
    private Player player;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() {
        player = new Player();
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms() {
        Room corpos, mina, biblioteca, portao, calabouco, estatuas, tunel, prisao, passagem, salaFinal, deposito, covil, central;

        portao = new Room("no portao principal da masmorra, foi por aqui que voce entrou.");
        central = new Room("numa sala com 2 corredores, aqui voce escutou um barulho estranho, tenha cuidado.");
        deposito = new Room("em um deposito abandonado, varias caixas e tudo escuro, tem um bau aberto com joias dentro, voce esta tentado a abrir.");
        calabouco = new Room("num calabouco, tem um corredor grande a frente, mas esta bem vazio.");
        mina = new Room("na mina abandonada, na porta a sua frente voce ve um rastro de sangue, melhor ter cuidado.");
        corpos = new Room("na sala de corpos, varios cadaveres no chao empilhados, nao se sabe o que tem pela frente.");
        biblioteca = new Room("numa biblioteca antiga, quase caindo aos pedacos, porem ha uma mesa com um livro no meio dela, ele ja esta aberto.");
        estatuas = new Room("numa sala cheia de estatuas antigas, sao bem estranhas e assustadoras.");
        prisao = new Room("numa sala com varias celas de prisao, uma delas esta aberta com um cadaver e parece haver algo em seu bolso...");
        tunel = new Room("em um tunel com cheiro forte de enxofre... esta escuro, acho melhor ligar uma torcha.");
        passagem = new Room("depois de uma escadaria enorme, numa sala fria, voce escuta um pisado muito alto, como se houvesse um elefante ao seu lado...");
        covil = new Room("voce entrou em algo que se parece um covil, carcacas de animais mortos pelo chao, porem esta muito escuro, sera que deve prosseguir?");
        salaFinal = new Room("essa sala esta muita quieta, so tem uma caveira de cristal cravejada de diamantes num pedestal.");

        // initialise room exits

        portao.setExit("sul", central);

        central.setExit("norte", portao);
        central.setExit("leste", deposito);
        central.setExit("oeste", estatuas);

        deposito.setExit("oeste", central);
        deposito.setExit("norte", calabouco);

        calabouco.setExit("sul", deposito);
        calabouco.setExit("oeste", mina);

        mina.setExit("leste", calabouco);
        mina.setExit("oeste", corpos);

        corpos.setExit("leste", mina);
        corpos.setExit("sul", biblioteca);

        biblioteca.setExit("norte", corpos);

        estatuas.setExit("leste", central);
        estatuas.setExit("sul", prisao);

        prisao.setExit("norte", estatuas);
        prisao.setExit("oeste", tunel);
        prisao.setExit("leste", passagem);

        tunel.setExit("leste", prisao);

        passagem.setExit("oeste", prisao);
        passagem.setExit("leste", salaFinal);
        passagem.setExit("sul", covil);

        covil.setExit("norte", passagem);

        salaFinal.setExit("oeste", passagem);

        currentRoom = portao; // start game outside

        // keys area

        salaFinal.setLockDoor(true);
        salaFinal.setNeededKey("final");
        prisao.setOtherRoomKey("final");

        covil.setLockDoor(true);
        covil.setNeededKey("covil");
        corpos.setOtherRoomKey("covil");

        // trap area

        tunel.setTrap(true);
        deposito.setTrap(true);
        salaFinal.setTrap(true);

        // hint area

        biblioteca.setHint("O livro estava se desmanchando, mas voce conseguiu ler algo nele... Nao tenha medo do gigante, ele odeia carne humana e nem sabe contar moeda");

        // treasure area

        covil.setTreasure(true);
    }

    /**
     * Main play routine. Loops until end of play.
     */
    public void play() {
        printWelcome();

        // Enter the main command loop. Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Obrigado por jogar :D! Que tal tentar novamente?");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() {
        System.out.println();
        System.out.println("Bem vindo ao Dungeon Seeker!");
        System.out.println("Voce esta atras de um grande tesouro, boa sorte tentando encontra-lo!");
        System.out.println("Digite '" + CommandWord.HELP + "' se precisar de ajuda.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("Eu nao consegui entender o que voce disse...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case USE:
                useKey(command);
                break;

            case INTERACT:
                wantToQuit = interact();
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
        }
        return wantToQuit;
    }

    // Usar chave

    private void useKey(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know which room to use key...
            System.out.println("Usar chave em qual sala?");
            return;
        }

        String direction = command.getSecondWord();

        Room nextRoom = currentRoom.getExit(direction);

        if(nextRoom == null){
            System.out.println("nao ha salas para usar a chave");
        }else {
            if (!nextRoom.getLockDoor()) {
                System.out.println("Sala ja destrancada.");
            } else {
                String neededKey = nextRoom.getNeededKey();
                Boolean hasKey = player.checkKey(neededKey);

                if (hasKey) {
                    nextRoom.setLockDoor(false);
                    System.out.println("Sala destracada, agora voce pode prosseguir.");
                } else {
                    System.out.println("Voce nao tem a chave para essa porta.");
                }
            }
        }
    }

    // Pegar chave

    private boolean interact() {
        if (currentRoom.getTreasure()){
            System.out.println("Voce encontrou o tesouro! O gigante era docil, ele te ofereceu um churrasco e te deu todas as moedas de ouro dele.");
            return true;
        }

        if (currentRoom.getTrap()) {
            System.out.println("Oh nao! Um barulho alto ocorreu, voce caiu numa armadilha! Seus ossos foram esmagados e transformados em migalhas.");
            return true;
        } else {
            String roomKey = currentRoom.getOtherRoomKey();
            String roomHint = currentRoom.getHint();
            if(roomKey != null){
                player.addKey(roomKey);
                System.out.println("A sala tinha uma chave, chave da sala " + roomKey + " adicionada a bolsa.");
                if (roomHint != null) {
                    System.out.println(roomHint);
                }
            } else {
                if (roomHint != null){
                    System.out.println(roomHint);
                } else {
                    System.out.println("Nao ha nada nessa sala");
                }
            }

            return false;
        }
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the
     * command words.
     */
    private void printHelp() {
        System.out.println("Nao sabe para onde ir? Explore e interaja com as salas, mas com cuidado...");
        System.out.println();
        System.out.println("Suas palavras de comando sao:");
        parser.showCommands();
    }

    /**
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Ir para onde?");
            System.out.println(currentRoom.getExitString());
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("Nao ha salas para ir!");
        } else {
            if (nextRoom.getLockDoor()) {
                System.out.println("Porta trancada! Tente achar uma chave. 'usar'");
            } else {
                currentRoom = nextRoom;
                System.out.println(currentRoom.getLongDescription());
            }
        }
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) {
        if (command.hasSecondWord()) {
            System.out.println("Sair do que?");
            return false;
        } else {
            return true; // signal that we want to quit
        }
    }
}
