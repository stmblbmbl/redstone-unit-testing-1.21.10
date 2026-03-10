package rs_unit_test;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.minecraft.server.command.CommandManager.*;
import static rs_unit_test.RedstoneUnitTesting.MOD_ID;

public class CommandManager {
    TestManager testManager;
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public CommandManager(TestManager testManager) {
        this.testManager = testManager;
        this.registerCommands();
    }

    public CommandManager() {
        this(new TestManager());
    }

    public void registerCommands() {
        // Invalid syntax
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("rut").executes(context -> {
                context.getSource().sendFeedback(() -> Text.literal("Called /rut with no arguments"), false);
                return 1;
            }));
            dispatcher.register(literal("rut")
                    .then(literal("new")
                            .then(argument("name", StringArgumentType.string())
                                    .executes(context -> {
                                        String name = StringArgumentType.getString(context, "name");
                                        boolean success = this.testManager.newTest(name);
                                        if (success) {
                                            context.getSource().sendFeedback(() -> Text.literal("Successfully added test."), false);
                                            return 1;
                                        } else {
                                            context.getSource().sendFeedback(() -> Text.literal("Failed to add test"), false);
                                            return 1;
                                        }


                                    })
                            )
                    )
            );
            dispatcher.register(literal("rut")
                    .then(literal("list")
                            .executes(context -> {
                                List<String> names = new ArrayList<>(this.testManager.listTestNames());
                                // Sort it so it looks pretty
                                names.sort(String.CASE_INSENSITIVE_ORDER);
                                switch (names.size()) {
                                    case 0:
                                        context.getSource().sendFeedback(() -> Text.literal("No tests found."), false);
                                        return 1;
                                    case 1:
                                        context.getSource().sendFeedback(() -> Text.literal("Found 1 list."), false);
                                        break;
                                    case 2:
                                        context.getSource().sendFeedback(() -> Text.literal("Found " + names.size() + " lists."), false);
                                }
                                for (String name : names) {
                                    context.getSource().sendFeedback(() -> Text.literal("Test: " + name), false);
                                }
                                return 1;
                            })
                    )
            );
            dispatcher.register(literal("rut").then(literal("run").then(argument("name", StringArgumentType.string())
                    .executes(context -> {
                        String name = StringArgumentType.getString(context, "name");
                        this.testManager.runTest(name, context.getSource().getServer());
                        return 1;
                    })
            )));
            dispatcher.register(literal("rut").then(literal("listAssertions")
                    .executes(context -> {
                        this.testManager.listAssertions(context);
                        return 1;
                    })
            ));
            dispatcher.register(literal("rut").then(literal("listAssertions").then(argument("name", StringArgumentType.string())
                    .executes(context -> {
                        this.testManager.listAssertions(context, StringArgumentType.getString(context, "name"));
                        return 1;
                    }))
            ));
            dispatcher.register(literal("rut").then(literal("deleteAssertion")
                    .then(argument("x", IntegerArgumentType.integer())
                    .then(argument("y", IntegerArgumentType.integer())
                    .then(argument("z", IntegerArgumentType.integer())
                    .executes(context -> {
                        BlockPos pos = new BlockPos(IntegerArgumentType.getInteger(context, "x"), IntegerArgumentType.getInteger(context, "y"), IntegerArgumentType.getInteger(context, "z"));
                        this.testManager.deleteAssertion(pos);
                        return 1;
                    }))))
            ));
            dispatcher.register(literal("rut").then(literal("deleteAssertion")
                    .then(argument("x", IntegerArgumentType.integer())
                    .then(argument("y", IntegerArgumentType.integer())
                    .then(argument("z", IntegerArgumentType.integer())
                    .then(argument("name", StringArgumentType.string())
                    .executes(context -> {
                        BlockPos pos = new BlockPos(IntegerArgumentType.getInteger(context, "x"), IntegerArgumentType.getInteger(context, "y"), IntegerArgumentType.getInteger(context, "z"));
                        this.testManager.deleteAssertion(context, pos, StringArgumentType.getString(context, "name"));
                        return 1;
                    })))))
            ));
            dispatcher.register(literal("rut").then(literal("listInputs")
                    .executes(context -> {
                        this.testManager.listTestInputs(context);
                        return 1;
                    })
            ));
            dispatcher.register(literal("rut").then(literal("listInputs").then(argument("name", StringArgumentType.string())
                    .executes(context -> {
                        this.testManager.listTestInputs(context, StringArgumentType.getString(context, "name"));
                        return 1;
                    }))
            ));
            dispatcher.register(literal("rut").then(literal("deleteInput")
                    .then(argument("x", IntegerArgumentType.integer())
                            .then(argument("y", IntegerArgumentType.integer())
                                    .then(argument("z", IntegerArgumentType.integer())
                                            .executes(context -> {
                                                BlockPos pos = new BlockPos(IntegerArgumentType.getInteger(context, "x"), IntegerArgumentType.getInteger(context, "y"), IntegerArgumentType.getInteger(context, "z"));
                                                this.testManager.deleteTestInput(pos);
                                                return 1;
                                            }))))
            ));
            dispatcher.register(literal("rut").then(literal("deleteInput")
                    .then(argument("x", IntegerArgumentType.integer())
                            .then(argument("y", IntegerArgumentType.integer())
                                    .then(argument("z", IntegerArgumentType.integer())
                                            .then(argument("name", StringArgumentType.string())
                                                    .executes(context -> {
                                                        BlockPos pos = new BlockPos(IntegerArgumentType.getInteger(context, "x"), IntegerArgumentType.getInteger(context, "y"), IntegerArgumentType.getInteger(context, "z"));
                                                        this.testManager.deleteTestInput(context, pos, StringArgumentType.getString(context, "name"));
                                                        return 1;
                                                    })))))
            ));
            dispatcher.register(literal("rut").then(literal("setInputTick")
                    .then(argument("index", IntegerArgumentType.integer())
                            .then(argument("tick", IntegerArgumentType.integer())
                                    .executes(context -> {
                                        this.testManager.currentTest.setInputTick(IntegerArgumentType.getInteger(context, "index"), IntegerArgumentType.getInteger(context, "tick"));
                                        return 1;
                                    })))
            ));
            dispatcher.register(literal("rut").then(literal("setAssertionTick")
                    .then(argument("index", IntegerArgumentType.integer())
                            .then(argument("tick", IntegerArgumentType.integer())
                                    .executes(context -> {
                                        this.testManager.currentTest.setAssertionTick(IntegerArgumentType.getInteger(context, "index"), IntegerArgumentType.getInteger(context, "tick"));
                                        return 1;
                                    })))
            ));
        });
    }
}
