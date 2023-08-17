package com.grappenmaker.optifineweave

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraft.util.EnumChatFormatting
import net.weavemc.loader.api.Hook
import net.weavemc.loader.api.ModInitializer
import net.weavemc.loader.api.command.Command
import net.weavemc.loader.api.command.CommandBus
import net.weavemc.loader.api.util.asm
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodNode
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

var optifineURL: String = "http://s.optifine.net"
val config = Paths.get(System.getProperty("user.home"), ".weave", "weaveOptifineURL.txt")

@Suppress("unused")
class WeaveOptifineURL : ModInitializer {
    override fun preInit() {
        println("Initializing WeaveOptifineURL!")

        if (!config.exists()) {
            println("Creating config file $config, with default URL $optifineURL")
            config.writeText(optifineURL)
        } else {
            optifineURL = config.readText()
            println("Reading optifine URL from file: $optifineURL")
        }

        CommandBus.register(SetURLCommand)
    }
}

object SetURLCommand : Command("setoptifineurl", "seturl") {
    override fun handle(args: Array<String>) {
        optifineURL = args.firstOrNull() ?: return sendMessage("Specify a new URL!", EnumChatFormatting.RED)
        config.writeText(optifineURL)
        sendMessage("Updated optifine URL to $optifineURL!")
    }

    private fun sendMessage(msg: String, color: EnumChatFormatting = EnumChatFormatting.GREEN) =
        Minecraft.getMinecraft().thePlayer
            .addChatMessage(ChatComponentText(msg).setChatStyle(ChatStyle().setColor(color)))
}

fun ClassNode.find(target: String) = methods.find { it.name == target }
    ?: throw IncompatibleClassChangeError("$target was not found in $name!")

fun loadURL() = asm {
    invokestatic("com/grappenmaker/optifineweave/WeaveOptifineURLKt", "getOptifineURL", "()Ljava/lang/String;")
}

@Suppress("unused")
class HttpHook : Hook("net/optifine/http/HttpUtils") {
    override fun transform(node: ClassNode, cfg: AssemblerConfig) = node.find("getPlayerItemsUrl").overwrite()
}

fun MethodNode.overwrite() {
    tryCatchBlocks.clear()
    localVariables.clear()

    instructions = asm {
        +loadURL()
        areturn
    }
}

@Suppress("unused")
class CapeHook : Hook("net/optifine/player/CapeUtils") {
    override fun transform(node: ClassNode, cfg: AssemblerConfig) {
        val target = node.find("downloadCape")
        val insn = target.instructions.filterIsInstance<LdcInsnNode>()
            .find { "s.optifine.net" in it.cst.toString() } ?: return

        target.instructions.insert(insn, loadURL())
        target.instructions.remove(insn)
    }
}

@Suppress("unused")
class LunarBridgeHook : Hook("*") {
    override fun transform(node: ClassNode, cfg: AssemblerConfig) {
        if (!node.name.startsWith("com/moonsworth/")) return

        val clinit = node.methods.find { it.name == "<clinit>" } ?: return
        val insn = clinit.instructions.filterIsInstance<LdcInsnNode>()
            .find { it.cst == "https://s-optifine.net" } ?: return

        val targetField = insn.next as? FieldInsnNode ?: return
        (node.methods.find { m ->
            m.desc == "()Ljava/lang/String;" && m.instructions.filterIsInstance<FieldInsnNode>().any {
                it.name == targetField.name && it.desc == targetField.desc && it.owner == node.name
            }
        } ?: return).overwrite()
    }
}