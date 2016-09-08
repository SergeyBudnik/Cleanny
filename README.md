# Cleanny

## Overview

Cleanny is a tiny maven plugin for java code inspections.

## Abilities

**Check imports in java files**

Cleanny is able to scan your java sources code and search for unwanted imports. It can both fail builds on totally unwanted imports or simply warn you.

Everything you need to do is to add the following configuration into your pom.xml file.

```xml
<pluginRepositories>
    ...
    <pluginRepository>
        <id>oss-sonatype</id>
        <name>oss-sonatype</name>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </pluginRepository>
    ...
</pluginRepositories>

<plugins>
    ...
    <plugin>
        <groupId>com.bdev</groupId>
        <artifactId>cleanny</artifactId>
        <version>1.0-SNAPSHOT</version>
        <configuration>
            <errorImports>
                <errorImport>lombok.Data</errorImport>
            </errorImports>
            <warnImports>
                <warnImport>lombok.Getter</warnImport>
            </warnImports>
        </configuration>
        <executions>
            <execution>
                <phase>compile</phase>
                <goals>
                    <goal>check_imports</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
    ...
</plugins>    
```
