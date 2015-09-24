GameStatus
==========

Simple plugin to switch between predefined motd values 

# Commands

## `/gamestatus`

Clears the current MOTD and uses the server's default

## `/gamestatus <key>`

Sets the current MOTD to the value defined for `<key>` in the config file

Permissions: `uhc.gamestatus` - allows use of `/gamestatus`, default op

# Configuration

Default config:

```yaml
motds:
  example: "&6HI, THIS IS SERVER"
on start: example
```

## `motds`

A list of key -> motd pairs. Running `/gamestatus example` in the above would choose the `example` value (`&6HI, THIS IS SERVER`)

## `on start`

When the plugin loads, which key should be used. If this is not given or it is invalid then it uses the servers default.
(The same as running `/gamestatus` without arguments)