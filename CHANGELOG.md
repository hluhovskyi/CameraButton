# Change Log

## Version 2.0.0

### New

- Add scrollable icons inside the button (`setIcons`, `scrollIconsToPosition`, `setIconsPosition`)
- Add ability to specify action which will collapse the button (`setCollapseAction`)
- Add ability to cancel expanded state of the button (`cancel`)

## Breaking changes

- Change name of package
- Rename `Mode.TAP` -> `Mode.PHOTO`, `Mode.HOLD` -> `Mode.VIDEO`
- Use `@IntDef` constants instead of enum for `Mode`
- Rename listener methods `setTapEventListener` -> `setPhotoEventListener`, `setHoldEventListener` -> `setVideoEventListener`

### Fixes

- `cb_gradient_rotation_multiplier` now is taken into account
- `State.PRESSED` is no longer dispatched when `Mode.VIDEO` (ex `Mode.HOLD`)

### Also
- Add a lot of new tests
- Add documentation
