//file:noinspection DependencyNotationArgument
// TODO remove when fixed in RFG ^


import com.gtnewhorizons.retrofuturagradle.MinecraftExtension
import org.jetbrains.gradle.ext.Gradle
import org.jetbrains.gradle.ext.RunConfigurationContainer
import java.util.*

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("eclipse");
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.7"
    id("com.gtnewhorizons.retrofuturagradle") version "1.3.12"
}


version = "${properties["mc_version"]}-${properties["phase"]}-${properties["major"]}.${properties["minor"]}.${properties["patch"]}"
group = properties["maven_group"]!!
//archivesBaseName = properties["archives_base_name"]!!

configure<BasePluginExtension> {
    //archivesName.setValue(properties["modBaseName"] as String)
    archivesBaseName = properties["archives_base_name"] as String
}

val java = the<JavaPluginExtension>()
val sourceSets = java.sourceSets
// Set the toolchain version to decouple the Java we run Gradle with from the Java used to compile and run the mod
configure<JavaPluginExtension> {

    // Azul covers the most platforms for Java 8 toolchains, crucially including MacOS arm64
    toolchain.vendor.set(JvmVendorSpec.AZUL)
    toolchain.languageVersion.set(JavaLanguageVersion.of(properties["java_version"] as String))

    // Generate sources and javadoc when building and publishing
    withJavadocJar()
    withSourcesJar()
}


tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}
val embed by configurations.creating
configurations  {
    embed

    implementation.configure {
        extendsFrom(embed)
    }
}

val minecraft = the<MinecraftExtension>()
minecraft {
    mcVersion.set(properties["mc_version"] as String)

    extraRunJvmArguments.add("-ea:${project.group}")
    //extraRunJvmArguments.add("-Dfml.coreMods.load=${coremod_plugin_class_name}")

    useDependencyAccessTransformers.set(true)

    injectedTags.put("VERSION", project.version)
}

// Generate the InternalTags class with the version number as a field
tasks.injectTags.configure {
    outputClassName.set("${project.group}.MBTInternalTags")
}

repositories {
    maven {
        // MixinBooter
        name = "Cleanroom Maven"
        url = uri("https://maven.cleanroommc.com")
    }
    maven {
        // JEI
        name = "Progwml6 Maven"
        url = uri("https://dvs1.progwml6.com/files/maven/")
    }
    maven {
        // CraftTweaker and JEI Backup
        name = "BlameJared Maven"
        url = uri("https://maven.blamejared.com")
    }
    maven {
        // TOP, CTM, GRS, AE2
        name = "Curse Maven"
        url = uri("https://www.cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
    maven {
        // Mixin
        name = "Sponge Maven"
        url = uri("https://repo.spongepowered.org/maven")
    }
    mavenLocal() // Must be last for caching to work
}

dependencies {

    val implementation: Configuration by configurations
    val compileOnly: Configuration by configurations
    val annotationProcessor: Configuration by configurations
    val api: Configuration by configurations

    // Hard Dependencies
    // the CCL deobf jar uses very old MCP mappings, making it error at runtime in runClient/runServer
    // therefore we manually deobf the regular jar
    implementation(rfg.deobf("curse.maven:codechicken-lib-1-8-${properties["ccl_pid"]}:${properties["ccl_fid"]}"))
    // manually deobf the jar to prevent extra configuration for handling obf/deobf separation
    implementation(rfg.deobf("curse.maven:gregtech-ce-unofficial-${properties["gt_pid"]}:${properties["gt_fid"]}-sources-${properties["gt_sources_fid"]}"))


    // Soft Dependencies
    implementation("mezz.jei:jei_1.12.2:${properties["jei_version"]}")
    implementation("CraftTweaker2:CraftTweaker2-MC1120-Main:1.12-${properties["crt_version"]}")
    implementation(rfg.deobf("curse.maven:top-${properties["top_pid"]}:${properties["top_fid"]}"))
    implementation(rfg.deobf("curse.maven:ctm-${properties["ctm_pid"]}:${properties["ctm_fid"]}"))

    // use a local jar for GroovyScript to avoid a bug from Mixin and ForgeGradle,
    // causing crashes at run-time when deobfuscated
    implementation(files("libs/groovyscript-0.4.0.jar"))
    compileOnly(rfg.deobf("curse.maven:ae2-extended-life-${properties["ae2_pid"]}:${properties["ae2_fid"]}"))


    // Compile-Time Dependencies

    // GroovyScript dependency
    implementation("zone.rong:mixinbooter:${properties["mixinbooter_version"]}")

    // Mixin dependencies
    api("org.spongepowered:mixin:${properties["mixin_version"]}") {
        isTransitive = false
    }
    annotationProcessor("org.spongepowered:mixin:${properties["mixin_version"]}") {
        isTransitive = false
    }

    compileOnly("org.jetbrains:annotations:23.0.0")
    annotationProcessor("org.jetbrains:annotations:23.0.0")

    annotationProcessor("org.ow2.asm:asm-debug-all:${properties["asm_debug_version"]}")
    annotationProcessor("com.google.guava:guava:${properties["guava_version"]}-jre")
    annotationProcessor("com.google.code.gson:gson:${properties["gson_version"]}")
}

//noinspection ConfigurationAvoidance
for (at in  sourceSets.getByName("main").resources.files) {
    if (at.name.lowercase(Locale.getDefault()).endsWith("_at.cfg")) {
        tasks.deobfuscateMergedJarToSrg.configure {
            accessTransformerFiles.from(at)
        }
        tasks.srgifyBinpatchedJar.configure {
            accessTransformerFiles.from(at)
        }
    }
}

//val processResources: ProcessResources by tasks
tasks.processResources.configure {
    // this will ensure that this task is redone when the versions change.
    inputs.property("version", project.version)
    inputs.property("mcversion", project.minecraft.mcVersion)
    // replace stuff in mcmod.info, nothing else
    filesMatching("mcmod.info") {
        expand(mapOf("version" to project.version))
        expand(mapOf("mcversion" to project.minecraft.mcVersion))
    }


    rename("(.+_at.cfg)", "META-INF/$1") // Access Transformers
}

val jar: Jar by tasks
jar.apply {
    manifest {
        val attribute_map = mapOf("FMLAT" to project.name + "_at.cfg")
        //attribute_map['FMLCorePlugin'] = coremod_plugin_class_name
        //attribute_map['FMLCorePluginContainsFMLMod'] = true
        //attribute_map['ForceLoadAsMod'] = project.gradle.startParameter.taskNames[0] == 'build'
        //attribute_map['FMLAT'] = archives_base_name + '_at.cfg'
        attributes(attribute_map)
    }

    // Add all embedded dependencies into the jar
    from(provider {
        /*embed.apply {
            embed.forEach {
                //it.isDirectory
                file
            }
        } */
        embed.map {
            if (it.isDirectory) {
                it
            }
            else {
                zipTree(it)
            }
        }
    })
}

idea {
    module {
        inheritOutputDirs = true
        isDownloadSources = true
        isDownloadJavadoc = true
    }
    project {
        this.withGroovyBuilder {
            "settings" {
                "runConfigurations" {
                    val self = this.delegate as RunConfigurationContainer
                    self.add(Gradle("1. Run Client").apply {
                        setProperty("taskNames", listOf("runClient"))
                    })
                    self.add(Gradle("2. Run Server").apply {
                        setProperty("taskNames", listOf("runServer"))
                    })
                    self.add(Gradle("3. Run Obfuscated Client").apply {
                        setProperty("taskNames", listOf("runObfClient"))
                    })
                    self.add(Gradle("4. Run Obfuscated Server").apply {
                        setProperty("taskNames", listOf("runObfServer"))
                    })
                }
                "compiler" {
                    val self = this.delegate as org.jetbrains.gradle.ext.IdeaCompilerConfiguration
                    afterEvaluate {
                        self.javac.javacAdditionalOptions = "-encoding utf8"
                        self.javac.moduleJavacAdditionalOptions = mapOf(
                                (project.name + ".main") to
                                        tasks.compileJava.get().options.compilerArgs.map { '"' + it + '"' }.joinToString(" ")
                        )
                    }
                }
            }
        }
    }
}

tasks.named("processIdeaSettings").configure {
    dependsOn("injectTags")
}

tasks.register("copyExamples") {
    doLast {
        val scriptsDir = file("$projectDir/run/scripts")
        val examplesDir = file("$projectDir/examples")
        scriptsDir.deleteRecursively()
        examplesDir.copyRecursively(scriptsDir)
    }
}

tasks.named("runClient").configure {
    dependsOn("copyExamples")
}

val javadoc: Javadoc by tasks
javadoc.apply {
    exclude { el ->
        el.file.isFile &&
                (el.file.name != "package-info.java" &&
                        el.file.useLines { lines -> !lines.contains("@ZenRegister") })
    }
    setDestinationDir(file("docs/gtceu"))
    (options as StandardJavadocDocletOptions).run {
        locale("en")
        tags!!.addAll(listOf(
                "zenClass:a:ZenClass:",
                "zenGetter:a:ZenGetter:",
                "zenSetter:a:ZenSetter:"
        ))
    }
}


