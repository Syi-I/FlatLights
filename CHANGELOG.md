
# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]
### Added
-

### Changed
-

### Fixed
-

### Planned
- Curio Set: **Relic Hunter's Resolution**
  - Focuses on movement and looting/luck for better exploration and treasure hunting
  - Set Effect: undecided
- Curio Set: **Insight of the Arcane Technician**
  - Focuses on potion effect buffs and xp gain
  - Set Effect: **(Spec Bump)** Increases enchantment levels of worn equipment by a flat amount
- Curio Set: undecided (something about blacksmith/forging idk)
  - Focuses on armor stat improvement and durability
  - Set Effect: **(Smithing Efficiency)** Lowers the XP cost of using anvils
- Curio Set: **Elusive Assassin**
  - Dodge and movement related buffs, applying negative potion effects
  - Set Effect: **(Foul Play)** Increases the damage dealt to a target based on the number of negative potion effects that are applied
- Curio Set: undecided
  - Speed related buffs
  - Set Effect: **(Speed of Silence)** Increases the damage dealt by a factor of the wearer's movement speed
- Rudimentary power generator that just eats items and gives 1 rf per item or something silly
- Power transfer cable for the power generator
- Powered item repairer

## [1.23.1] - 2024-06-30
RIP Sorry to anyone who tried to use this on a server before, I didn't think to test running the mod on a dedicated server, so I didn't realize it instantly crashed. Things should be better now but there's a good chance I missed some stuff so do let me know what stuff still causes crashes.
### Added
-

### Changed
-

### Fixed
- Crashing when running the mod on servers due to reaching across logical sides, trying to get clientside stuff in certain classes and swapping out code segments as necessary

## [1.23.0] - 2024-06-30
### Added
- **Curse of Lifted Pickup Truck** 
  - Increases armor points of the cursed armor piece, while also increasing damage taken by the wearer while wearing a cursed armor piece
- Curio sets with different rarity tiers and effects, which spawn in generated chests as loot
  - Curios have a chance to generate in any structure's loot chests
  - Roll found curios to determine what rarity `Tier` it is
    - The `Tier` of the curio will increase the stat buff values by a certain amount
    - `Tiers` include: **Common < Rare < Epic < Legendary < Growth**
  - Specially added curio slots for the added curios: **Light Cube, Light Prism, Light Sphere**
  - Each `Curio Set` has a special `Set Effect`
    - Activates if the player is wearing a curio in each of the three added slots, and are all from the same `Curio Set`
    - `Set Effects` can be toggled with a keybind, in case an effect gets too annoying in certain scenarios such as being at your base
  - Various new stats for players that can be gained via the different `Curio Sets`
    - **Dodge Chance %**
      - A percentage chance to completely mitigate damage taken
    - **XP Boost**
      - Boosts the amount of XP that is gained from picking up XP orbs by a flat amount
    - **Healing Boost**
      - Boosts the amount of healing that is received by a flat amount
    - **Bonus Loot Rolls**
      - Increases the number of times the loot table of killed mobs is rolled by a flat amount 
    - **Bonus Loot Roll Chance %**
      - The percent chance of triggering the Bonus Loot Rolls effect
  - Curio Set: **Dragon's Final Test**
    - Basic but well-rounded stat increases, providing a decent all around combat strength buff
    - Set Effect: **(Erosion Aura)** Projects an aura around the wearer which damages entities with lower HP than the wearer
  - Curio Set: **On the Forgotten Shore**
    - Water focused buffs for movement and survivability
    - Set Effect: **(Domain of Still Waters)** Provides an extreme damage boost to the wearer when either the wearer or target is in water or rain
  - Curio Set: **Radiance of the False Sun**
    - Fire focused buffs for damage, health, and healing
    - Set Effect: **(Rising Heat)** Provides boosted damage to burning mobs and increases fire damage to nearby mobs
- New notification system for various actions, such as toggling a tool's ability on and off or activating a curio's effect
- The `Motivational Chair` block now has a 40% chance to generate in structure loot chests instead of being a creative only item

### Changed
- Improved tooltip displays and toggle notifications for the various items and tools utilizing action bar text
- Fixed tooltip styling to be more uniform across items
- Rearranged layout of the config files for better organization and readability

### Fixed
- `Entangled` effect's linked damage triggering on mobs that do not have the potion effect active anymore

## [1.22.1] - 2024-04-01
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- This changelog lmao, now things will follow an actual changelog and versioning format instead of being so random, vague, and incomplete
### Changed

### Fixed
- Localization for some Tiles block names, now all are unified in style again

## [1.22.0] - 2024-03-24
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Blackout Powder item, used for crafting Blackout block variants of lighting blocks
- Blackout variants which have a lower light level of 7 (vs normal 15) for all Flat Block, Panel, Pillar, Edge Light colors and hue shifted colors
- Recipes for all blackout block variants
- Recipe for Blackout Powder item
- Localization text for all new blackout block variants and Blackout Powder item
### Changed

### Fixed

## [1.21.4] - 2024-03-21
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- Removed old renderers for the projectiles of Final Sunset and Prismatic Sword
### Fixed

## [1.21.3] - 2024-03-20
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- New item model for inactive state of Prismatic Sword, using item property override method from Final Sunset
### Changed

### Fixed

## [1.21.2] - 2024-03-20
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- Rendering code and model for the Bomb from the Prismatic Sword, now it's a bomb instead of a black square
- Tweaked velocity of the bomb projectile of the Prismatic Sword making it launch at a lower speed than before
- Rendering code and model for the Black Hole from Final Sunset, now uses a sphere projectile before the black hole entity is spawned in
### Fixed

## [1.21.1] - 2024-03-19
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- New spear model for Final Sunset spear mode, making use of the item property override that was previously implemented
### Changed

### Fixed

## [1.21.0] - 2024-03-15
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Spear mode for Final Sunset, testing attack reach extension as well as item property overrides for item model changes
### Changed

### Fixed
- A couple of typos

## [1.20.5] - 2024-02-12
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- Texture update for the Health Reduction potion effect icon
- Included all assets in the assets folder for Git commits since manually added files weren't always added to the repo
### Fixed
- Some typos in the localization text

## [1.20.4] - 2024-02-08
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Lethality enchantment which reduces the max HP of a target through the Health Reduction effect
- Localization for Lethality enchantment name, enchantment description, Health Reduction effect name
### Changed

### Fixed


## [1.20.3] - 2024-02-08
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- Tooltip localization for Gravity Lift and Portable Black Hole Generator, instead of having hard coded tool tip descriptions
- Updated textures for the potion effect icons of Entangled and Bleed
### Fixed


## [1.20.2] - 2024-02-08
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Gravity Lift item which lifts the player up off the ground a set amount of blocks when standing on the lift
- Recipe for the Gravity Lift item
- Config options for Gravity Lift lifetime and cooldown
- Localization for Gravity Lift item name
### Changed

### Fixed
- Some formatting issues with the Portable Black Hole Generator item


## [1.20.1] - 2024-01-18
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Black Hand enchantment which stops the user from knocking back targets during melee combat
### Changed
- Removed some enchantment level translation text since it was just used for easier readability of high level enchantments during previous testing
### Fixed


## [1.20.0] - 2024-01-16
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- Swapped from custom enchantment helper logic to the ones provided by Forge, because I didn't realize that those existed
- Damage calculations for Quantum Strike enchantment now take into account damage increase from the vanilla Sharpness enchantment, instead of only base weapon damage from generic attack attributes
### Fixed


## [1.19.1] - 2023-12-20
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed

### Fixed
- Forgot to change mod version for upload again

## [1.19.0] - 2023-12-20
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Global loot modifier for Jogoat item dropping after extinguishing fire
- Advancement for acquiring Jogoat item
- Localization text for Jogoat item and advancement
- Skeleton of custom criterion structure for advancements
### Changed

### Fixed


## [1.18.4] - 2023-12-19
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- Name scheme of some variables used in the Portable Black Hole Generator's code and config options
### Fixed


## [1.18.3] - 2023-12-12
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed

### Fixed
- Forgot to change mod version for upload

## [1.18.2] - 2023-12-12
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Recipe for the Portable Black Hole Generator
### Changed

### Fixed


## [1.18.1] - 2023-12-11
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Config options for suction power, damage, and cooldown length of the Portable Black Hole Generator
### Changed

### Fixed


## [1.18.0] - 2023-12-11
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Portable Black Hole Generator item
### Changed
- Cleaned up some old code from the black hole projectile which was previously implemented in Final Sunset, now also used for the new Portable Black Hole Generator item
### Fixed


## [1.17.8] - 2023-12-10
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Hue shifted variants of the plate blocks (hexblock, large hexblock, tiles, large tiles)
- Recipes for all new hue shifted plate blocks
- Localization text for all new hue shifted plate blocks
### Changed

### Fixed


## [1.17.7] - 2023-12-09
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed

### Fixed
- Dark Purple hue shifted block variants not having the same name scheme anymore due to previous changes again, now all have the same style

## [1.17.6] - 2023-12-09
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed

### Fixed
- Dark Purple hue shifted block still causing issues due to the name changes not being made everywhere


## [1.17.5] - 2023-12-09
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- Dark Purple hue shifted block name changed in registry to match previous name change (changes block ID, so it disappears from existing worlds)
### Fixed


## [1.17.4] - 2023-12-09
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Recipe for the Hue Shifting Vial finally
### Changed

### Fixed


## [1.17.3] - 2023-12-09
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- Hue shifted purple block item name to fit better with the other hue shifted block names
### Fixed


## [1.17.2] - 2023-12-09
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Reusable Dye items for each of the 16 dye colors
- Recipes for the Reusable Dye items
- Localization text for Reusable Dye items, hue shifted block variants
### Changed

### Fixed


## [1.17.1] - 2023-12-08
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Hue shifted variants for Panels, Edge Lights, Pillars
- Recipes for each of the new hue shifted block variants
### Changed

### Fixed


## [1.17.0] - 2023-12-07
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Hue shifted blocks which adds 16 new color variants to all the Flat Block lights
- Hue Shifting Vial which is used for crafting the hue shifted blocks
- Recipes for hue shifted blocks
### Changed

### Fixed


## [1.16.4] - 2023-10-30
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- Final Sunset damage type from Physical to Entangled damage
### Fixed
- Some tooltip description formatting that was showing up incorrectly for Prismatic Armor
- Bonesaw and Bleeding Edge stack mechanic spamming logs when applying their respective effects on entities
- Crafting sounds chance to play being too high by default


## [1.16.3] - 2023-08-25
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- Changed some localization text to be more descriptive, mostly tooltips
### Fixed


## [1.16.2] - 2023-10-28
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- Improved Prismatic Armor tooltips to show current damage reduction percentage
- Performance changes to the structure of command implementation because it was really rough
### Fixed


## [1.16.1] - 2023-10-28
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- Optimized some code for Final Sunset mechanics
- Adjustments to tooltip description and nbt usage of Final Sunset
### Fixed


## [1.16.0] - 2023-10-28
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Basic command structure with a test command
- A couple custom paintings
### Changed
- Rewrote tooltips for Prismatic Armor to have more clarity on when its effects work
### Fixed


## [1.15.0] - 2023-10-08
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Player data tracker for Final Sunset stat tracker to sync with in case of item loss
### Changed

### Fixed


## [1.14.0] - 2023-09-28
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Prisma Nucleus crafting recipe
- Sound effects that play while crafting, with config options to enable/disable
### Changed

### Fixed
- JEI integration with Plating Machine recipes being visually incorrect


## [1.13.3] - 2023-09-21
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Motivational Chair now gives saturation when a player sits in it
- More visual and audio effects for black hole projectile of Final Sunset
- Visual effects on player to indicate Final Sunset mode switches
### Changed
- Textures for Prismatic Ingot and hexblocks remade to better match other texture style
### Fixed
- Cleaned up extraneous/old files from the project, so it doesn't take as much space


## [1.13.2] - 2023-09-18
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- Formatting on Motivational Chair tooltip
- Hexblock textures remade again
### Fixed
- Stack mechanic of Bleeding Edge and Bonesaw enchantments double applying stacks on hit


## [1.13.1] - 2023-09-01
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed

### Fixed
- Crashing from some serverside and clientside logic being where it shouldn't be
- Some sounds not playing correctly when playing on a server


## [1.13.0] - 2023-08-28
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- Updated Plating Machine model and texture to better fit the overall theme of the mod, now that the mod is more fleshed out
- Reworked projectile launching feature of Final Sunset, soon to be implemented into a separate craftable item
- Updated projectile renderer for Final Sunset
### Fixed
- Damage bonus type from Prismatic Blade correctly gives kill credit to the attacker
- Damaging radius from Pulsing Arrows enchantment correctly gives kill credit to the attacker
- Flash of Brilliance enchantment issue crashing the game when killing a player


## [1.12.0] - 2023-08-27
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss. 
I have no idea where 8 other version bumps went I'm just going off git commits trying to fill in gaps for this changelog. It'll get better eventually, maybe.
### Added
- Creative Inventory tab for all the items in the mod
- Recipes for the glass blocks
### Changed
- Adjusted block names to have a more unified name scheme, again
### Fixed


## [1.4.0] - 2023-08-26
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Recipes for edge lights
- JEI integration including Plating Machine and Spectralizer recipe types
- Tooltips for how edge lights function
### Changed
- Item stack limit adjusted for Plating Machine and Spectralizer, so you can have more than one item in the internal buffer at a time
### Fixed


## [1.3.0] - 2023-08-25
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed

### Fixed
- Reformatted creation of edge lights
- Gave edge lights actual block drop table so that mining them actually drops the block

## [1.2.0] - 2023-08-24
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Edge lights which follow along the sides of solid blocks, available in the regular 16 colors
### Changed
- Optimized some data generation and imports, cutting down on copy-pasted stuff
### Fixed


## [1.1.0] - 2023-08-15
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Final Sunset sword (creative only item with untested/future features)
- New Wire Glass block variants, with the same patterns as the plate blocks, and also Wither proof
- New items, mostly for crafting purposes
- Prismatic Armor set with damage reduction ability, full set bonus, individual pieces with unique effects
- Config files with some basic options for better balancing ability on the user's end
- A bunch of new potion effects and enchantments
- Plating Machine block, used as a new method of crafting items
- Light Storage block, basically a much bigger chest that looks fancy
- Spectralizer block, used for some more complex recipes and items
- Motivational Chair block, a decoration block players can sit on
### Changed
- Changed block textures again, this time flattening out the plate block textures to look smoother
- Data generation for generic registry stuff
- License change again because I am indecisive
### Fixed
- Block name scheme unified


## [1.0.10] - 2022-10-18
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- License update
- README updates to reflect all new block changes and additions
### Fixed


## [1.0.9] - 2022-10-18
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- Recipes now use forge tags when possible, instead of being stuck with hard coded items
### Fixed


## [1.0.8] - 2022-10-16
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Recipes for all the new block variants
### Changed

### Fixed


## [1.0.7] - 2022-10-15
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Tiles and large tiles variants for each of the 16 colors
### Changed

### Fixed


## [1.0.6] - 2022-10-13
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Large hexblock variants for each of the 16 colors
### Changed

### Fixed


## [1.0.5] - 2022-10-11
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Tile light variants for each of the 16 colors
### Changed

### Fixed


## [1.0.4] - 2022-10-07
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- Hexblock light variants for each of the 16 colors
### Changed

### Fixed


## [1.0.3] - 2022-10-06
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- Refactored some block initialization code
- Adjusted startup events for registering blocks and items
### Fixed


## [1.0.2] - 2022-10-04
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- Adjusted README file
- Redid licensing and description in mods.toml

### Fixed


## [1.0.1] - 2022-10-04
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added

### Changed
- Updated block textures to better differentiate some color variants
- Adjusted README file 

### Fixed


## [1.0.0] - 2022-10-04
Ignore the mess of a changelog that is in the initial versions, I was not keeping track of changes well at the time so version bumping was hit or miss
### Added
- First version with initial files

### Changed

### Fixed