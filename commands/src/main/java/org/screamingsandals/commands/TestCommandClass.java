package org.screamingsandals.commands;

import com.google.inject.Inject;
import org.screamingsandals.commands.api.auto.ScreamingCommand;
import org.screamingsandals.commands.api.builder.SCBuilder;

import java.util.List;

import static org.screamingsandals.lib.core.lang.SLang.log;
import static org.screamingsandals.lib.core.lang.SLang.mpr;

public class TestCommandClass implements ScreamingCommand {

    @Override
    @Inject
    public void register() {
        final var node = SCBuilder.command("test")
                .callback(context -> {
                    log.debug("Sender is player: {}", context.getSender().isPlayer());
                    mpr("this.is.test.idksomething", context.getSender())
                            .send();
                    mpr("this.is.test.somethingelse", context.getSender())
                            .send();
                })
                .description("Big ass test")
                .usage("/test test")
                .permission("U.DUMB.ASS")

                .withSubNode(SCBuilder.subCommand("test")
                        .callback(context -> {
                            final var sender = context.getSender();
                            final var args = context.getArguments();
                            final var parent = context.getNode();

                            if (!sender.isPlayer()) {
                                mpr("this.shit.is.not.for.you", sender)
                                        .send();
                                return;
                            }

                            final var player = sender.getPlayer();
                            player.kick(mpr("retard").getFirst());
                        })
                        .tabCallback(context -> {
                            if (context.getArguments().size() == 2) {
                                return List.of("kokot");
                            }

                            return List.of();
                        })
                        .permission("oi")
                        .usage("idk"))

                .withSubNode(SCBuilder.subCommand("test2")
                        .callback(context -> {

                        })
                        .permission("oi"))
                .build();
    }
}
