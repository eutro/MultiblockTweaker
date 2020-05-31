import re

import sys


def get_stack(match: re.Match):
    return match.group("stack")


textIn = "".join(sys.stdin.readlines())

importPattern = re.compile("import mods\\.modularmachinery\\.RecipeBuilder(\\.newBuilder as (\\w+))?;")

search = importPattern.findall(textIn)

assert len(search) != 0

altName = "RecipeBuilder.newBuilder"

for s in search:
    if len(s) > 1:
        altName = s[1]

buildCallRegex = re.compile("\n(?P<comment>//.+\\W+)?" +
                            altName +
                            "\\((?P<qmark>[\\\"'])(?P<recipeId>[a-zA-Z1-9_\\-]*)(?P=qmark),\\W*"
                            "(?P<qmark2>[\\\"'])(?P<machineId>[a-zA-Z1-9_\\-]*)(?P=qmark2),\\W*"
                            "(?P<duration>\\d+)\\)(?P<extraCalls>(.|\\n)+?)\\.build\\(\\);")

regexes = (
    (re.compile("\\.addItemInput\\((?P<stack>.+?)\\)"), "addItemInput"),
    (re.compile("\\.addFluidInput\\((?P<stack>.+?)\\)"), "addFluidInput"),
    (re.compile("\\.addItemOutput\\((?P<stack>.+?)\\)"), "addItemOutput"),
    (re.compile("\\.addFluidOutput\\((?P<stack>.+?)\\)"), "addFluidOutput")
)

energyRegex = re.compile("\\.addEnergyPerTick(?P<direction>Input|Output)\\((?P<val>\\d+)\\)")

ourBuilds = []

for buildCall in buildCallRegex.finditer(textIn):
    comment = buildCall.group("comment")
    build = comment if comment is not None else ""

    build += buildCall.group("machineId") + (".recipeMap\n"
                                             "    .recipeBuilder()\n")

    build += "    .duration({0})\n".format(buildCall.group("duration"))

    calls = buildCall.group("extraCalls")

    energyM = list(energyRegex.finditer(calls))[-1]
    energy = int(energyM.group("val")) / 4 * (1 if energyM.group("direction") == "Input" else -1)

    build += "    .EUt({0})\n".format(str(int(energy)))

    for regex, function in regexes:
        spaces = len(function) * " "
        params = ",\n    {0}  ".format(spaces).join(map(get_stack, regex.finditer(calls)))
        if params != "":
            build += "    .{0}({1})\n".format(function, params)

    build += "    .buildAndRegister()\n"

    ourBuilds.append(build)

print("\n".join(ourBuilds))
