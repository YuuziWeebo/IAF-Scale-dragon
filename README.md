# Tamed Dragon Scaler 🐉

**Community addon prototype for IceAndFire Community Edition** — adjust an individual tamed dragon's size without editing the base mod.

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-blue) ![NeoForge](https://img.shields.io/badge/NeoForge-21.1.x-orange) ![IceAndFire--CE](https://img.shields.io/badge/IceAndFire--CE-2.1.4-green) ![Status](https://img.shields.io/badge/status-unverified%20alpha-yellow)

> **Status: 0.1.0-alpha SOURCE PREVIEW.** This code has been statically reviewed, **not built or tested inside Minecraft**. Please build on GitHub Actions first, inspect CI results, then test with a backup world before distributing a release JAR. The source zip is **not** an installable mod.

## Features

- Per-dragon size slider in the existing dragon inventory GUI (40–120% in 5% steps), with Reset to 100%.
- Only the **tamed dragon's owner** can change its size. The server validates menu ownership, entity ID, entity survival, dimensions, and distance (`DragonMenu.stillValid`).
- Uses vanilla `minecraft:generic.scale` for visual scale, built-in attribute persistence, and client/server attribute syncing. The addon does **not** create a global tick-scanning task.
- Adds CE-specific mixins to adjust the **primary bounding box** and the **multipart hitboxes** (head, neck, wings, tail) that bypass vanilla SCALE in CE 2.1.4.
- Adjusts rider and breath-origin offsets to track the smaller/larger model.
- Maintains original dragon **age, stage, max-health, attack damage, armor and breeding requirements**. The ordinary physics/gameplay consequences of a different hitbox can still alter practical combat or navigation.
- Intended for all three dragons: Fire, Ice and Lightning.
- Does not ship with IceAndFire CE or any of its dependencies.

## Compatibility

- **Minecraft**: Java 1.21.1
- **Mod loader**: NeoForge 21.1.x (development baseline 21.1.248)
- **Required mod**: IceAndFire Community Edition **2.1.4** (exact version targeted)
- **Install sides**: client **and** server (required for network protocol, GUI and synced hitboxes)
- **Java**: 21

**Do not use this with the original Ice and Fire mod or a different CE branch without reviewing its internal classes.**

## Build with GitHub Actions (free for public repositories with standard runners)

1. Create a **public GitHub repository** from this source (push files from the root of this folder to `main`).
2. Go to **Actions → Build addon → Run workflow**. The workflow also triggers on `main` pushes.
3. If Gradle compilation passes, open the workflow run → **Artifacts → tamed-dragon-scaler-1.21.1-neoforge**.
4. Extract the ZIP downloaded from GitHub Actions. The JAR under `build/libs` is the addon; the workflow does not bundle IceAndFire itself.
5. Install the resulting addon JAR in `mods/` **alongside** `iceandfire-2.1.4.jar` on **both** the server and every client, plus all dependencies CE requires.
6. Back up your world, then test a tame dragon using its inventory. Open it as its actual owner, adjust the slider, close/reopen, reload the world and test Dragon Horn storage.

### Local build (optional)

Install JDK 21 and Gradle 9.2.1, then run:

```bash
gradle build
```

A Gradle wrapper is not bundled in this source preview. CI provisions Gradle automatically. You can generate and commit the wrapper after obtaining Gradle (`gradle wrapper --gradle-version 9.2.1`).

## How to use

1. Tame a dragon using normal IceAndFire-CE gameplay.
2. As its owner, open its normal inventory screen.
3. Find the **Dragon size / Kích thước rồng** slider near the original GUI.
4. Choose 40%, 45%, ... 120%; release the slider to send the change to the server. Keyboard changes are sent when pressed.
5. Use Reset to return to 100%.

An untamed dragon or a dragon belonging to another player cannot be changed through this addon. Other players will see the updated size through synced entity attributes.

## Implementation notes

### Why no standalone custom NBT field?

Vanilla `Attributes.SCALE` already synchronizes to watching clients and persists in each entity's saved attributes. `DragonHornItem` saves the dragon's full entity NBT, so the scale should survive capture/release. This should be verified in game.

### Why are mixins needed?

CE 2.1.4 renders dragons through `DragonBaseEntityRenderer`, which inherits vanilla living-renderer scaling, but CE's `DragonBaseEntity#getDimensions` overrides body dimensions with an age-only formula. Its `DragonPartEntity#updateScale` independently positions and sizes multipart hitboxes. An attribute-only datapack therefore cannot ensure correct multipart hitboxes.

This addon applies SCALE at the two CE dimension paths and includes SCALE in CE's age-size change detector, then adjusts the rider/breath origin offsets. It does not multiply the renderer scale a second time.

### Known limitations / release blockers

- **Build and gameplay not verified here.** GitHub Actions needs a green build, followed by real 1.21.1 NeoForge client+server tests.
- The GUI may be cramped with very small Minecraft GUI scales / screen sizes; fallback placement may overlap another mod's overlays.
- CE contains age-based camera distance, entity combat reach, movement, particles and animations beyond the body and multipart dimensions. Some may not scale perfectly in this prototype. In particular third-person camera distance has **not** been adjusted.
- Multipart registration/tracking and updated part positioning must be checked in multiplayer, including join-in-progress and riding.
- The custom slider uses the vanilla SCALE attribute: an external mod or command also editing that attribute could conflict with this addon's chosen value.
- **This project is not an established published release.** Users should not treat its alpha source as a tested public mod until the test plan below passes.

## Test checklist

See [`docs/TEST_PLAN.md`](docs/TEST_PLAN.md). Please test on a copy of a world.

## Acknowledgements and license

- Inspired by [IceAndFire Community Edition](https://github.com/IAFEnvoy/IceAndFire-CE) by IAFEnvoy and contributors. **Not an official project and not affiliated with its maintainers.**
- Based on NeoForge's documented screen and networking APIs and CE's public 1.21.1 code.
- This addon's original code is MIT licensed; the original CE mod is not included or relicensed.

---

<details>
<summary>🇻🇳 Hướng dẫn nhanh bằng tiếng Việt</summary>

**Đây là source alpha, chưa phải mod JAR đã build/test.**

- Yêu cầu: Minecraft Java 1.21.1, NeoForge, IceAndFire-CE 2.1.4.
- Đưa source lên repo GitHub public → **Actions → Build addon** → tải artifact sau khi build thành công.
- Lấy JAR của addon bỏ vào `mods/` trên **server và máy từng người chơi**, bên cạnh JAR IceAndFire-CE, không thay thế JAR gốc.
- Vào giao diện rồng đã tame bằng đúng tài khoản chủ rồng. Kéo 40–120% và bấm Đặt lại khi cần.
- Giữ nguyên tuổi/Stage/máu/damage; rồng khác không bị đổi scale.
- Bắt buộc backup world và thử trên world test, nhất là cưỡi rồng, hitbox đầu/cánh/đuôi, Dragon Horn.

</details>
