rootProject.name = "lab5"
include("dtomodule")
include("dao")
include("controllerlayer")
include("servicelayer")
include("servicelayer:catsmicroservice")
findProject(":servicelayer:catsmicroservice")?.name = "catsmicroservice"
include("servicelayer:catsmicroservice")
findProject(":servicelayer:catsmicroservice")?.name = "catsmicroservice"
include("servicelayer:ownersmicroservice")
findProject(":servicelayer:ownersmicroservice")?.name = "ownersmicroservice"
