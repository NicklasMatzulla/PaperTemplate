/*
 * MIT License
 *
 * Copyright (c) 2024 Nicklas Matzulla
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

@file:Suppress("SpellCheckingInspection")

plugins {
    alias(libs.plugins.runPaper)
    alias(libs.plugins.shadow)
    alias(libs.plugins.paperweight)
    id("java")
}

group = "de.nicklasmatzulla"
version = "1.0.0-SNAPSHOT"
description = "PaperTemplate is a quick starter that makes it easy to create new programming projects using my libraries."

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor(libs.annotations)
    compileOnly(libs.annotations)
    implementation(libs.annotations)
    paperweight.paperDevBundle(libs.versions.paperApi)

    testAnnotationProcessor(libs.annotations)
    testCompileOnly(libs.annotations)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release = 17
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    shadowJar {
        fun reloc(pkg: String, libName: String) = relocate(pkg, "de.nicklasmatzulla." + project.name.lowercase() + ".$libName")
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
            "name" to project.name,
            "version" to project.version,
            "description" to project.description,
            "apiVersion" to libs.versions.pluginApi.get(),
            "mainClass" to "de.nicklasmatzulla." + project.name.lowercase() + "." + project.name
        )
        inputs.properties(props)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}