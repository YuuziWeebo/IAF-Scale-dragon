# Release QA — Tamed Dragon Scaler 0.1.0-alpha

All boxes start unchecked until verified in actual Minecraft.

## Build and startup
- [ ] `gradle build` succeeds on the specified toolchain with no Mixin compilation/refmap warnings.
- [ ] Client reaches the title screen with IceAndFire CE 2.1.4 + addon + CE dependencies.
- [ ] Dedicated NeoForge server starts; there are no client-class loading crashes.
- [ ] Every connected client has the same addon version.

## UI and access
- [ ] Owner can open Dragon Inventory and sees slider 40–120% and Reset.
- [ ] Slider is rendered/clickable and does not overlap slots on common UI scales (1–4).
- [ ] Reset restores 100%.
- [ ] Other players cannot change the dragon through a crafted packet or GUI.
- [ ] Packet with invalid percent or entity ID is ignored.
- [ ] Packet rejected when the dragon inventory is closed, out of range, or wrong dimension.

## Scale and visuals
- [ ] Fire, Ice and Lightning tame dragons scale 40%, 60%, 100%, and 120%.
- [ ] Wild dragons remain unchanged.
- [ ] Body AABB and head/neck/wing/tail AABBs align with the resized model.
- [ ] Parts respond after a scale change without relogging.
- [ ] All clients see the same resized dragon, including players entering chunk later.
- [ ] Rider does not float/sink; dismount doesn't clip walls.
- [ ] Breath/charge originates from the expected head location.
- [ ] Third-person camera, animation, attacks, pathfinding, and reach are usable.

## Persistence
- [ ] Age/Stage do not change, HP maximum and damage remain intact.
- [ ] Scale survives chunk unload/reload and server restart.
- [ ] Scale survives storing into and releasing from Dragon Horn.
- [ ] Newly tamed dragon defaults to 100%.
- [ ] Existing tamed dragons in old saves default to 100%.
- [ ] Resizing while riding or while dragon is taking damage causes no crash.

## Performance
- [ ] No global every-tick entity search is introduced (static code review).
- [ ] No packet spam during mouse dragging.
- [ ] TPS remains stable in a 4-player modpack test.

## Releasing to the public
- [ ] Screenshots and short video showing real CE 2.1.4 gameplay.
- [ ] Clean startup and error logs (no stack traces).
- [ ] Public source, original mod credit and distinct mod identity.
- [ ] Release JAR is the exact CI-built artifact; document SHA256 and tested environment.
