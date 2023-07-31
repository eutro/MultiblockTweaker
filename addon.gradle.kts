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