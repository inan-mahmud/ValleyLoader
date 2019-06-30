### Gradle Versions Plugin
Provides a task to determine which dependencies have updates. Additionally, the plugin checks for updates to Gradle itself.
```
gradle dependencyUpdates -Drevision=release -DoutputFormatter=json 
```

### JaCoCo Test Coverage
```
./gradlew jacocoTestReport
```

### Lint Check
The lint tool checks your Android project source files for potential bugs and optimization improvements for correctness, security, performance, usability, accessibility, and internationalization.

[![Lint](https://developer.android.com/studio/images/write/lint.png)](https://developer.android.com/studio/write/lint)

##### Configs:

- lintConfig -> path to lint rulesets file where you can suppress issues.
- htmlOutput -> path where html report will be generated.

```
./gradlew lint
```

### Findbugs
A program which uses static analysis to look for bugs in Java code. 

##### Configs:

- excludeFilter -> path to findbugs rulesets file where you can suppress issues.
- classes -> path to generated classes (if you have more then one flavor, path consists of flavor name + flavor name).
- source -> path to source code.
- html.destination -> path where html report will be generated.

```
./gradlew findbugs
```

### PMD
PMD is a source code analyzer. It finds common programming flaws like unused variables, empty catch blocks, unnecessary object creation, and so forth.

##### Configs:

- ruleSetFiles -> path to pmd rulesets file where you can suppress issues and define which issues to track.
- source -> path to source code.
- html.destination -> path where html report will be generated.

```
./gradlew pmd
```