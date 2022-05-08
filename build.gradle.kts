import net.minecraftforge.gradle.user.UserBaseExtension

buildscript {
    repositories {
        jcenter()
        maven {
            name = "jitpack"
            setUrl("https://jitpack.io")
        }
        maven {
            name = "forge"
            setUrl("https://maven.minecraftforge.net/")
        }
    }
    dependencies {
        classpath("com.github.GregTechCE:ForgeGradle:FG_2.3-SNAPSHOT")
    }
}

apply(plugin = "net.minecraftforge.gradle.forge")

version = "1.12.2-${properties["phase"]}-${properties["major"]}.${properties["minor"]}.${properties["patch"]}"
group = properties["modGroup"]!!

configure<BasePluginConvention> {
    archivesBaseName = properties["modBaseName"] as String
}

val java = the<JavaPluginConvention>()
val sourceSets = java.sourceSets
configure<JavaPluginConvention> {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = targetCompatibility
}

val minecraft = the<UserBaseExtension>()
configure<UserBaseExtension> {
    version = properties["forge_ver"] as String
    runDir = "run"
    mappings = "stable_39"

    makeObfSourceJar = false
    isUseDepAts = true

    accessTransformer("src/main/resources/META-INF/mbtweaker_at.cfg")

    replace("@GRADLE:VERSION@", project.version)
}

val jar: Jar by tasks
jar.apply {
    from(sourceSets["main"].output)
    manifest {
        attributes(mapOf("FMLAT" to "mbtweaker_at.cfg"))
    }
}

repositories {
    maven { // CraftTweaker
        url = uri("https://maven.blamejared.com/")
    }
    maven { // GTCE
        url = uri("https://minecraft.curseforge.com/api/maven")
    }
    maven { // CCL
        url = uri("http://chickenbones.net/maven/")
    }
    maven { // JEI
        url = uri("https://dvs1.progwml6.com/files/maven/")
    }
    maven { // JEI fallback
        url = uri("https://modmaven.k-4u.nl")
    }
    maven { // CTM
        url = uri("http://maven.tterrag.com/")
    }
}

dependencies {
    val deobfCompile: Configuration by configurations
    val deobfProvided: Configuration by configurations
    val runtime: Configuration by configurations
    val implementation: Configuration by configurations

    deobfCompile("CraftTweaker2:CraftTweaker2-MC1120-Main:${properties["ct_ver"]}")

    deobfProvided("mezz.jei:jei_1.12.2:${properties["jei_ver"]}:api")
    runtime("mezz.jei:jei_1.12.2:${properties["jei_ver"]}")

//    deobfCompile("gregtechce:gregtech:1.12.2:${properties["gt_ver"]}")
    "provided"(files("libs/gregtech-1.12.2-2.2.2-beta.jar"))

    "deobfCompile"("codechicken:ChickenASM:1.12-1.0.2.9")
    "deobfCompile"("codechicken-lib-1-8:CodeChickenLib-1.12.2:3.2.3.358:universal")

    deobfCompile("team.chisel.ctm:CTM:${properties["ctm_ver"]}")

    implementation("org.jetbrains:annotations:15.0")
}

val processResources: ProcessResources by tasks
processResources.apply {
    // this will ensure that this task is redone when the versions change.
    inputs.property("version", project.version)
    inputs.property("mcversion", minecraft.version)

    // replace stuff in mcmod.info, nothing else
    from(sourceSets["main"].resources.srcDirs) {
        include("mcmod.info")

        // replace version and mcversion
        expand(mapOf("version" to project.version, "mcversion" to minecraft.version))
    }

    // copy everything else, thats not the mcmod.info
    from(sourceSets["main"].resources.srcDirs) {
        exclude("mcmod.info")
    }
}

tasks.register("copyExamples") {
    doLast {
        val scriptsDir = file("${minecraft.runDir}/scripts")
        val examplesDir = file("$projectDir/examples")
        scriptsDir.deleteRecursively()
        examplesDir.copyRecursively(scriptsDir)
    }
}
tasks["runClient"].dependsOn("copyExamples")

val javadoc: Javadoc by tasks
javadoc.apply {
    exclude { el ->
        el.file.isFile &&
                (el.file.name != "package-info.java" &&
                        el.file.useLines { lines -> !lines.contains("@ZenRegister") })
    }
    setDestinationDir(file("docs"))
    (options as StandardJavadocDocletOptions).run {
        locale("en")
        tags!!.addAll(listOf(
                "zenClass:a:ZenClass:",
                "zenGetter:a:ZenGetter:",
                "zenSetter:a:ZenSetter:"
        ))
    }
}
