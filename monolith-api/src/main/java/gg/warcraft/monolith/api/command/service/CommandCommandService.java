package gg.warcraft.monolith.api.command.service;

import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;

import java.util.List;
import java.util.UUID;

/**
 * This service is injectable.
 * <p>
 * The CommandCommandService serves as a point of entry into the command module implementation. It provides methods to
 * create a new {@code Command} at run time and to dispatch a {@code Command} as if a player or the console executed it.
 * <p>
 * A {@code Command} created at run time only lasts for as long as the server is running and needs to be created again
 * when the server restarts.
 */
public interface CommandCommandService {

    /**
     * @param name    The name of the new command. Can not be null or empty.
     * @param aliases An optional list of aliases. Can not be null, but can be empty. Items can not be null or empty.
     * @param handler The command handler. Can not be null.
     * @throws IllegalArgumentException Thrown when any of the parameter constraints are not met and if the name or any
     *                                  of the aliases is already in use by another command.
     */
    void createCommand(String name, List<String> aliases, CommandHandler handler) throws IllegalArgumentException;

    /**
     * @param command   The command to execute. Can not be null.
     * @param playerId  The id of the player to execute the command as. Can not be null.
     * @param arguments The command arguments. Can not be null, but can be empty. Items can not be null or empty.
     */
    void dispatchCommandFor(Command command, UUID playerId, String... arguments);

    /**
     * @param command   The command to execute as the console. Can not be null.
     * @param arguments The command arguments. Can not be null, but can be empty. Items can not be null or empty.
     */
    void dispatchConsoleCommand(Command command, String... arguments);
}