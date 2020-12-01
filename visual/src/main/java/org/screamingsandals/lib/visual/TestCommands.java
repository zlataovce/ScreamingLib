package org.screamingsandals.lib.visual;

import net.kyori.adventure.text.Component;
import org.screamingsandals.commands.api.auto.ScreamingCommand;
import org.screamingsandals.commands.api.builder.SCBuilder;

public class TestCommands implements ScreamingCommand {
    @Override
    public void register() {
        SCBuilder.command("test")
                .usage("/test")
                .description("test")

                .callback(context -> context.getSender().sendMessage(Component.text("kokot")))
                .withSubNode(SCBuilder.subCommand("kokot")
                        .callback(context -> context.getSender().sendMessage(Component.text("extrakokot"))))
                .build();

        SCBuilder.command("kokot")
                .usage("/kokot")
                .description("kokot")
                .callback(context -> context.getSender().sendMessage(Component.text("uuuu")))
                .build();
    }
}
