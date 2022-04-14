# JustAnimations

JustAnimations is a plugin that allows you to animateblocks in Minecraft. 
Not the textures but animate stuff with blocks. It can make for cool lobbies,
RPG servers, and more.

Basic usage:
- /ja create <name>
- /ja animation {name} addframe <ticks/optional, default is 20>
- /ja animation {name} start
- /ja animation {name} stop

To create a frame use worldedit and select an area, the selected area will be the frame

Requires WorldEdit (Probably 7.2.10) and 1.18.2 (Maybe earlier but not tested)

Discord Server: https://discord.gg/wVCSqV7ptB

## Permissions

- `justanimations.admin` - All permissions
- `justanimations.command` - All commands

## What file storage should I use for my animation?
There are two type:
- Single file (Stores all frames in a single file)
- Multiple files (Stores each frame in a separate file)

You select which one to use on animation creation.  The default is multiplefile.
/justanimations create <name> <singlefile/multiplefile>

### Multiple files

Pros:

- Less memory usage
- Easier to find certain frames in the files

Cons:

- Uses more CPU to read the files each frame

This one is good for bigger animations or servers with less ram but decent CPU power

### Single file

Pros:

- Less CPU usage

Cons:

- Uses more ram as it keeps the whole file loaded in ram
- Takes longer to load the file

This one is good for smaller animations or servers with lots of ram but not so much CPU power

## Animation load types

There are two animation load types. ram and file.
File loads the animation frames from the file each time it is played. and ram
loads it from ram each time it is played.

Ram is good for smaller animations and file is good for bigger animations.

## API

### Installation

#### Maven

Add this to the <repositories> section of your pom.xml:
```xml
<repositories>
	<repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Add this to the <dependencies> section of your pom.xml:
```xml
<dependency>
    <groupId>com.github.JustDoom</groupId>
    <artifactId>JustAnimations</artifactId>
    <version>version</version>
</dependency>
```

Replace "version" with the latest commit id from [here](https://jitpack.io/#JustDoom/JustAnimations/).

#### Gradle

Add this in your root build.gradle at the end of repositories
```gradle
repositories {
    maven {
        url 'https://jitpack.io'
    }
}
```

Add this to the dependencies in build.gradle
```gradle
dependencies {
    implementation 'com.github.JustDoom:JustAnimations:version'
}
```

Replace "version" with the latest commit id from [here](https://jitpack.io/#JustDoom/JustAnimations/).

### Features

There are some events and an util class you can use AnimationUtil

Events:
- AnimationStartEvent
- AnimationEndEvent
- AnimationFrameChangeEvent

### Examples

