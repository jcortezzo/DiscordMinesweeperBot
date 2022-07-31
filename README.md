# DiscordMinesweeperBot
Discord Bot that can let you play Minesweeper.

# Instructions
## Building and Running the Bot
1. Add a `config.json` file to the root of this repository. In the JSON, add a `botToken` key with the value of your bot token.
2. Run `mvn clean compile package`
3. Run `java -jar target/discordminesweeper-1.jar`

## Playing Minesweeper in Discord
After running the bot, use the command `!minesweeper` to start a new game. There are four actions you can do. The basic format of using an action is `<ACTION> <X COORD> <Y COORD>`.

|Action|Description|
|-|-|
|`!dig`|Equivalent to clicking on a coordinate. Uncovers that tile.|
|`!flag`|Flags a tile, meaning that tile cannot be dug at.|
|`!unflag`|Removes a flag from a tile. Only possible on flagged tiles.|
|`!chord`|In Minesweeper, if you have flagged `n` neighbors of an uncovered tile labeled with `n`, you can *chord* the space which will uncover all unflagged neighbors.|

### Example Game

**User**: `!minesweeper`

**MinesweeperBot**: 
```
8️⃣⬛🟩🟩🟩🟩🟩🟩🟩🟩🟩
7️⃣⬛🟩🟩🟩🟩🟩🟩🟩🟩🟩
6️⃣⬛🟩🟩🟩🟩🟩🟩🟩🟩🟩
5️⃣⬛🟩🟩🟩🟩🟩🟩🟩🟩🟩
4️⃣⬛🟩🟩🟩🟩🟩🟩🟩🟩🟩
3️⃣⬛🟩🟩🟩🟩🟩🟩🟩🟩🟩
2️⃣⬛🟩🟩🟩🟩🟩🟩🟩🟩🟩
1️⃣⬛🟩🟩🟩🟩🟩🟩🟩🟩🟩
0️⃣⬛🟩🟩🟩🟩🟩🟩🟩🟩🟩
⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛
⬛⬛0️⃣1️⃣2️⃣3️⃣4️⃣5️⃣6️⃣7️⃣8️⃣
```

**User**: `!dig 0 0`

**MinesweeperBot**:
```
8️⃣⬛⬜1️⃣🟩🟩🟩🟩🟩🟩🟩
7️⃣⬛⬜1️⃣2️⃣🟩🟩🟩🟩🟩🟩
6️⃣⬛⬜⬜1️⃣🟩🟩🟩🟩🟩🟩
5️⃣⬛⬜⬜1️⃣2️⃣🟩🟩🟩🟩🟩
4️⃣⬛⬜⬜⬜1️⃣🟩🟩🟩🟩🟩
3️⃣⬛⬜⬜⬜1️⃣1️⃣🟩🟩🟩🟩
2️⃣⬛⬜⬜⬜⬜1️⃣🟩🟩🟩🟩
1️⃣⬛⬜⬜⬜⬜2️⃣🟩🟩🟩🟩
0️⃣⬛⬜⬜⬜⬜2️⃣🟩🟩🟩🟩
⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛
⬛⬛0️⃣1️⃣2️⃣3️⃣4️⃣5️⃣6️⃣7️⃣8️⃣
```

**User**: `!flag 2 8`

**MinesweeperBot**:
```
8️⃣⬛⬜1️⃣🚩🟩🟩🟩🟩🟩🟩
7️⃣⬛⬜1️⃣2️⃣🟩🟩🟩🟩🟩🟩
6️⃣⬛⬜⬜1️⃣🟩🟩🟩🟩🟩🟩
5️⃣⬛⬜⬜1️⃣2️⃣🟩🟩🟩🟩🟩
4️⃣⬛⬜⬜⬜1️⃣🟩🟩🟩🟩🟩
3️⃣⬛⬜⬜⬜1️⃣1️⃣🟩🟩🟩🟩
2️⃣⬛⬜⬜⬜⬜1️⃣🟩🟩🟩🟩
1️⃣⬛⬜⬜⬜⬜2️⃣🟩🟩🟩🟩
0️⃣⬛⬜⬜⬜⬜2️⃣🟩🟩🟩🟩
⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛
⬛⬛0️⃣1️⃣2️⃣3️⃣4️⃣5️⃣6️⃣7️⃣8️⃣
```

**User**: `!flag 3 6`

**MinesweeperBot**:
```
8️⃣⬛⬜1️⃣🚩🟩🟩🟩🟩🟩🟩
7️⃣⬛⬜1️⃣2️⃣🟩🟩🟩🟩🟩🟩
6️⃣⬛⬜⬜1️⃣🚩🟩🟩🟩🟩🟩
5️⃣⬛⬜⬜1️⃣2️⃣🟩🟩🟩🟩🟩
4️⃣⬛⬜⬜⬜1️⃣🟩🟩🟩🟩🟩
3️⃣⬛⬜⬜⬜1️⃣1️⃣🟩🟩🟩🟩
2️⃣⬛⬜⬜⬜⬜1️⃣🟩🟩🟩🟩
1️⃣⬛⬜⬜⬜⬜2️⃣🟩🟩🟩🟩
0️⃣⬛⬜⬜⬜⬜2️⃣🟩🟩🟩🟩
⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛
⬛⬛0️⃣1️⃣2️⃣3️⃣4️⃣5️⃣6️⃣7️⃣8️⃣
```

**User**: `!unflag 3 6`

**MinesweeperBot**:
```
8️⃣⬛⬜1️⃣🚩🟩🟩🟩🟩🟩🟩
7️⃣⬛⬜1️⃣2️⃣🟩🟩🟩🟩🟩🟩
6️⃣⬛⬜⬜1️⃣🟩🟩🟩🟩🟩🟩
5️⃣⬛⬜⬜1️⃣2️⃣🟩🟩🟩🟩🟩
4️⃣⬛⬜⬜⬜1️⃣🟩🟩🟩🟩🟩
3️⃣⬛⬜⬜⬜1️⃣1️⃣🟩🟩🟩🟩
2️⃣⬛⬜⬜⬜⬜1️⃣🟩🟩🟩🟩
1️⃣⬛⬜⬜⬜⬜2️⃣🟩🟩🟩🟩
0️⃣⬛⬜⬜⬜⬜2️⃣🟩🟩🟩🟩
⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛
⬛⬛0️⃣1️⃣2️⃣3️⃣4️⃣5️⃣6️⃣7️⃣8️⃣
```

**User**: `!flag 3 6`

**MinesweeperBot**:
```
8️⃣⬛⬜1️⃣🚩🟩🟩🟩🟩🟩🟩
7️⃣⬛⬜1️⃣2️⃣🟩🟩🟩🟩🟩🟩
6️⃣⬛⬜⬜1️⃣🚩🟩🟩🟩🟩🟩
5️⃣⬛⬜⬜1️⃣2️⃣🟩🟩🟩🟩🟩
4️⃣⬛⬜⬜⬜1️⃣🟩🟩🟩🟩🟩
3️⃣⬛⬜⬜⬜1️⃣1️⃣🟩🟩🟩🟩
2️⃣⬛⬜⬜⬜⬜1️⃣🟩🟩🟩🟩
1️⃣⬛⬜⬜⬜⬜2️⃣🟩🟩🟩🟩
0️⃣⬛⬜⬜⬜⬜2️⃣🟩🟩🟩🟩
⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛
⬛⬛0️⃣1️⃣2️⃣3️⃣4️⃣5️⃣6️⃣7️⃣8️⃣
```

**User**: `!flag 2 7`

**MinesweeperBot**:
```
8️⃣⬛⬜1️⃣🚩1️⃣🟩🟩🟩🟩🟩
7️⃣⬛⬜1️⃣2️⃣2️⃣🟩🟩🟩🟩🟩
6️⃣⬛⬜⬜1️⃣🚩🟩🟩🟩🟩🟩
5️⃣⬛⬜⬜1️⃣2️⃣🟩🟩🟩🟩🟩
4️⃣⬛⬜⬜⬜1️⃣🟩🟩🟩🟩🟩
3️⃣⬛⬜⬜⬜1️⃣1️⃣🟩🟩🟩🟩
2️⃣⬛⬜⬜⬜⬜1️⃣🟩🟩🟩🟩
1️⃣⬛⬜⬜⬜⬜2️⃣🟩🟩🟩🟩
0️⃣⬛⬜⬜⬜⬜2️⃣🟩🟩🟩🟩
⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛
⬛⬛0️⃣1️⃣2️⃣3️⃣4️⃣5️⃣6️⃣7️⃣8️⃣
```
