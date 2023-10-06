# Weave Optifine URL
Allows you to specify a new URL for the Optifine cape server.
This makes use of the [Weave Mod Loader](https://github.com/Weave-MC/Weave-Loader).
Right now, it also assumes Minecraft 1.8.9, but does not assume a specific client.

### Installing
1. Download [Weave Loader](https://github.com/Weave-MC/Weave-Loader/releases).
2. Place `weave-optifine-url-0.2.jar` in `.weave/mods`.
3. Launch Weave using a tool of choice, like [Weave Manager](https://github.com/exejar/Weave-Manager).

### Usage
Once in-game, you can type `/setoptifineurl <url>` to set your optifine URL to a service of choice.
Popular 3rd party services include (copy-paste):
- Cloaks+: `https://server.cloaksplus.com`
- Mantle: `http://capes.mantle.gg`
- Cosmetica/Arcmetica: `http://23.95.137.176`

To then reload your cape, go to Options -> Skin Customization -> Optifine Cape and click Reload Cape.
Wait a few seconds and it should work.
Additionally, the mod will remember the last set URL, so you will only have to do this setup once.

### Contributing
PRs are welcome, however I doubt there is much to amend to this repository.

### Support
Join the [Weave Discord Server](https://discord.gg/SHZUYWhwDP).

### Building
```shell
./gradlew build
```

### License
[None lol](LICENSE.md)
